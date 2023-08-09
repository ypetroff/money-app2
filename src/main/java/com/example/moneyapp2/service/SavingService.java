package com.example.moneyapp2.service;

import com.example.moneyapp2.exception.NoAvailableDataException;
import com.example.moneyapp2.model.dto.income.CreateIncomeDTO;
import com.example.moneyapp2.model.dto.saving.CreateSavingDTO;
import com.example.moneyapp2.model.dto.saving.EditSavingDTO;
import com.example.moneyapp2.model.dto.saving.SavingDetailsDTO;
import com.example.moneyapp2.model.dto.saving.SavingInfoDTO;
import com.example.moneyapp2.model.entity.SavingEntity;
import com.example.moneyapp2.model.entity.user.UserEntity;
import com.example.moneyapp2.model.enums.IncomeCategory;
import com.example.moneyapp2.repository.SavingsRepository;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SavingService {

  private final SavingsRepository savingsRepository;

  private final UserService userService;

  private final IncomeService incomeService;

  private final ModelMapper modelMapper;
  
  public List<SavingInfoDTO> getAllSavingOfUser(String username) {
    verifyUser(username);
    return listOfSavingInfoDTO(username);
  }

  private List<SavingInfoDTO> listOfSavingInfoDTO(String username) {
    UserEntity userEntity =
        this.modelMapper.map(this.userService.findUser(username), UserEntity.class);

    return this.savingsRepository
        .findAllByOwnersContaining(userEntity)
        .orElseThrow(
            () ->
                new NoAvailableDataException(
                    String.format("User with username %s does not have any savings", username)))
        .stream()
        .map(s -> this.modelMapper.map(s, SavingInfoDTO.class))
        .toList();
  }

  public List<SavingInfoDTO> addNewSavingAndReturnAllSavingsOfUser(
      CreateSavingDTO savingDTO, String username) {

    createEntityAndSaveIt(savingDTO);

    return listOfSavingInfoDTO(username);
  }

  private void createEntityAndSaveIt(CreateSavingDTO addSavingDTO) {
    SavingEntity entity =
        SavingEntity.builder()
            .goal(addSavingDTO.getGoal())
            .amount(addSavingDTO.getAmount())
            .dateOfCreation(addSavingDTO.getDateOfCreation())
            .endDate(addSavingDTO.getEndDate())
            .owners(new ArrayList<>())
            .contributors(new ArrayList<>())
            .build();

    setOwners(entity, addSavingDTO.getOwners());
    setContributors(entity, addSavingDTO.getContributors());

    this.savingsRepository.saveAndFlush(entity);
  }

  private void setOwners(SavingEntity entity, List<String> owners) {
    owners.stream()
        .map(this.userService::findUser)
        .map(o -> this.modelMapper.map(o, UserEntity.class))
        .forEach(entity::addOwner);
  }

  private void setContributors(SavingEntity entity, List<String> contributors) {
    contributors.stream()
        .map(this.userService::findUser)
        .map(o -> this.modelMapper.map(o, UserEntity.class))
        .forEach(entity::addContributor);
  }

  public boolean SavingNotPresent(Long id) {
    return !this.savingsRepository.existsById(id);
  }

  public SavingDetailsDTO getDetailsOfSaving(Long id) {
    SavingEntity entity =
        this.savingsRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new NoAvailableDataException(
                        String.format("Saving with id: %d not found", id)));

    return this.modelMapper.map(entity, SavingDetailsDTO.class);
  }

  public SavingDetailsDTO editSaving(Long id, CreateSavingDTO changedSavingInfo) {
    SavingEntity entity =
        this.savingsRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new NoAvailableDataException(
                        String.format("Saving with id: %d not found", id)));

    this.modelMapper.map(changedSavingInfo, entity);

    this.savingsRepository.saveAndFlush(entity);

    return this.modelMapper.map(entity, SavingDetailsDTO.class);
  }

  public void deleteSaving(Long id) {
    SavingEntity entity =
        this.savingsRepository
            .findById(id)
            .orElseThrow(() -> new NoAvailableDataException("Cannot delete missing entity"));

    BigDecimal amount = entity.getAmount();

    if (amount.compareTo(BigDecimal.ZERO) > 0) {

      MathContext mc = new MathContext(2);

      BigDecimal amountPerUser = amount.divide(BigDecimal.valueOf(entity.getOwners().size()), mc);

      CreateIncomeDTO closedSavingDTO =
          CreateIncomeDTO.builder()
              .amount(amountPerUser)
              .description(
                  entity.getGoal().equals("not provided by user")
                      ? "Closed saving"
                      : String.format("Saved for %s", entity.getGoal()))
              .createdOn(LocalDateTime.now())
              .incomeCategory(IncomeCategory.SAVINGS.name())
              .build();

      for (UserEntity owner : entity.getOwners()) {
        this.incomeService.createEntityAndSaveIt(closedSavingDTO, owner.getUsername());
      }
    }

    this.savingsRepository.deleteById(id);
  }

  public boolean unauthorizedView(Long id, String username) {
    SavingEntity entity =
        this.savingsRepository
            .findById(id)
            .orElseThrow(() -> new NoAvailableDataException("User not found!"));

    return !isOwner(username, entity) && !isContributor(username, entity);
  }

  private boolean isContributor(String username, SavingEntity entity) {
    Optional<UserEntity> contributor =
        entity.getContributors().stream().filter(c -> c.getUsername().equals(username)).findFirst();

    return contributor.isPresent();
  }

  private boolean isOwner(String username, SavingEntity entity) {
    Optional<UserEntity> owner =
        entity.getOwners().stream().filter(o -> o.getUsername().equals(username)).findFirst();

    return owner.isPresent();
  }

  public boolean unauthorizedToModify(Long id, String username) {
    SavingEntity entity =
        this.savingsRepository
            .findById(id)
            .orElseThrow(() -> new NoAvailableDataException("User not found!"));

    return !isOwner(username, entity);
  }

  public EditSavingDTO getSingleSaving(Long id) {
    SavingEntity entity =
        this.savingsRepository
            .findById(id)
            .orElseThrow(() -> new NoAvailableDataException("Non existent saving"));

    return EditSavingDTO.builder()
        .id(entity.getId())
        .amount(entity.getAmount())
        .dateOfCreation(entity.getDateOfCreation())
        .endDate(entity.getEndDate())
        .owners(listOfUsernames(entity.getOwners()))
        .contributors(listOfUsernames(entity.getContributors()))
        .build();
  }

  private List<String> listOfUsernames(List<UserEntity> entityList) {
    return entityList.stream().map(UserEntity::getUsername).toList();
  }

  public void maintenance() {
    this.savingsRepository.findAll().stream()
        .filter(s -> s.getEndDate().equals(LocalDate.now()))
        .forEach(s -> deleteSaving(s.getId()));
  }
  
  private void verifyUser(String username) {
    if (this.userService.findUser(username) == null) {
      throw new UsernameNotFoundException(
          String.format(
              "%s is not present in the database. The variable was extracted from the Principal",
              username));
    }
  }
}
