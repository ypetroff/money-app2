package com.example.moneyapp2.service;

import com.example.moneyapp2.exception.NoAvailableDataException;
import com.example.moneyapp2.model.dto.income.CreateIncomeDTO;
import com.example.moneyapp2.model.dto.income.EditIncomeDTO;
import com.example.moneyapp2.model.dto.income.IncomeDetailsDTO;
import com.example.moneyapp2.model.dto.income.IncomeInfoDTO;
import com.example.moneyapp2.model.entity.IncomeEntity;
import com.example.moneyapp2.model.entity.user.UserEntity;
import com.example.moneyapp2.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeService {

  private final IncomeRepository incomeRepository;

  private final IncomeCategoryService incomeCategoryService;
  private final UserService userService;
  private final ModelMapper modelMapper;

  public BigDecimal getTotalIncomeOnTheApp() {
    return this.incomeRepository.allIncomeSum().orElse(BigDecimal.ZERO);
  }

  public BigDecimal getIncomeOfUser(String username) {
    verifyUser(username);
    return totalAmountBigDecimal(username);
  }

  private BigDecimal totalAmountBigDecimal(String username) {
    return getByOwnerUsername(username).stream()
        .map(IncomeEntity::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private List<IncomeEntity> getByOwnerUsername(String username) {
    return this.incomeRepository
        .findByOwnerUsername(username)
        .orElseThrow(() -> new NoAvailableDataException("Username based on Principal not found"));
  }

  public List<IncomeInfoDTO> addNewIncomeAndReturnAllIncomeOfUser(
      CreateIncomeDTO addIncomeDTO, String username) {
    verifyUser(username);
    createEntityAndSaveIt(addIncomeDTO, username);
    return listOfIncomeInfoDTO(username);
  }

  public void createEntityAndSaveIt(CreateIncomeDTO addIncomeDTO, String username) {
    verifyUser(username);
    IncomeEntity entity = this.modelMapper.map(addIncomeDTO, IncomeEntity.class);
    entity.setOwner(this.modelMapper.map(this.userService.findUser(username), UserEntity.class));
    entity.setIncomeCategory(
        this.incomeCategoryService.addCategory(addIncomeDTO.getIncomeCategory()));

    this.incomeRepository.saveAndFlush(entity);
  }

  private List<IncomeInfoDTO> listOfIncomeInfoDTO(String username) {
    verifyUser(username);
    return getByOwnerUsername(username).stream()
        .map(i -> this.modelMapper.map(i, IncomeInfoDTO.class))
        .toList();
  }

  public List<IncomeInfoDTO> getAllIncomeOfUser(String username) {
    verifyUser(username);
    return listOfIncomeInfoDTO(username);
  }

  public IncomeDetailsDTO getDetailsOfIncome(Long id) {
    return this.modelMapper.map(
        this.incomeRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new NoAvailableDataException(
                        String.format("Income with id: %d not found", id))),
        IncomeDetailsDTO.class);
  }

  public IncomeDetailsDTO editIncome(Long id, CreateIncomeDTO changedIncomeInfo) {
    IncomeEntity entity =
        this.incomeRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new NoAvailableDataException(
                        String.format("Income with id: %d not found", id)));

    this.modelMapper.map(changedIncomeInfo, entity);

    this.incomeRepository.saveAndFlush(entity);

    return this.modelMapper.map(entity, IncomeDetailsDTO.class);
  }

  public boolean IncomeNotPresent(Long id) {
    return !this.incomeRepository.existsById(id);
  }

  public void deleteIncome(Long id) {
    this.incomeRepository.deleteById(id);
  }

  public boolean unauthorizedUser(Long id, String username) {
    verifyUser(username);
    IncomeEntity entity =
        this.incomeRepository
            .findById(id)
            .orElseThrow(() -> new NoAvailableDataException("Income not found"));

    return !entity.getOwner().getUsername().equals(username);
  }

  public EditIncomeDTO getSingleIncome(Long id) {
    return this.modelMapper.map(
        this.incomeRepository
            .findById(id)
            .orElseThrow(() -> new NoAvailableDataException("Non existent income")),
        EditIncomeDTO.class);
  }

  public void maintenance() {
    this.incomeRepository.findAll().stream()
        .filter(x -> x.getCreatedOn().isBefore(LocalDateTime.now().minusYears(2)))
        .forEach(this.incomeRepository::delete);
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
