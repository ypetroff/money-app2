package com.example.moneyapp2.service;

import com.example.moneyapp2.exception.NoAvailableDataException;
import com.example.moneyapp2.model.dto.expense.ExpenseDetailsDTO;
import com.example.moneyapp2.model.dto.expense.ExpenseMandatoryFieldsDetailsDTO;
import com.example.moneyapp2.model.dto.income.CreateIncomeDTO;
import com.example.moneyapp2.model.dto.saving.CreateSavingDTO;
import com.example.moneyapp2.model.dto.saving.SavingDetailsDTO;
import com.example.moneyapp2.model.dto.saving.SavingInfoDTO;
import com.example.moneyapp2.model.dto.user.UserForServicesDTO;
import com.example.moneyapp2.model.entity.ExpenseEntity;
import com.example.moneyapp2.model.entity.IncomeEntity;
import com.example.moneyapp2.model.entity.SavingEntity;
import com.example.moneyapp2.model.entity.user.UserEntity;
import com.example.moneyapp2.model.enums.IncomeCategory;
import com.example.moneyapp2.repository.SavingsRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SavingService {

    private final SavingsRepository savingsRepository;
    private final UserService userService;

    private final IncomeService incomeService;
    private final ModelMapper modelMapper;


    private UserForServicesDTO getUser(String o) {
        return this.userService.findUser(o);
    }

    public List<SavingInfoDTO> getAllSavingOfUser(String username) {

        return listOfSavingInfoDTO(username);
    }

    private List<SavingInfoDTO> listOfSavingInfoDTO(String username) {

        return getByOwnerUsername(username);
    }

    private List<SavingInfoDTO> getByOwnerUsername(String username) {

        UserEntity userEntity = this.modelMapper.map(this.userService.findUser(username), UserEntity.class);

        return this.savingsRepository.findAllByOwnersContaining(userEntity)
                .orElseThrow(() -> new NoAvailableDataException(
                        String.format("User with username %s does not have any savings", username)))
                .stream()
                .map(s -> this.modelMapper.map(s, SavingInfoDTO.class))
                .toList();

    }

    public List<SavingInfoDTO> addNewSavingAndReturnAllSavingsOfUser(CreateSavingDTO savingDTO, String username) {

        createEntityAndSaveIt(savingDTO, username);

        return listOfSavingInfoDTO(username);
    }

    public void createEntityAndSaveIt(CreateSavingDTO addSavingDTO, String username) {

        SavingEntity entity = this.modelMapper.map(addSavingDTO, SavingEntity.class);

        setOwners(entity, addSavingDTO.getOwners()); //todo: optimise
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

        SavingEntity entity = this.savingsRepository.findById(id)
                .orElseThrow(() -> new NoAvailableDataException(
                        String.format("Saving with id: %d not found", id)));

        return this.modelMapper.map(entity,
                SavingDetailsDTO.class);
    }

    public SavingDetailsDTO editSaving(Long id, CreateSavingDTO changedSavingInfo) {

        SavingEntity entity = this.savingsRepository.findById(id)
                .orElseThrow(() -> new NoAvailableDataException(String.format("Saving with id: %d not found", id)));

        this.modelMapper.map(changedSavingInfo, entity);

        this.savingsRepository.saveAndFlush(entity);

        return this.modelMapper.map(entity, SavingDetailsDTO.class);
    }

    public void deleteSaving(Long id) {

        SavingEntity entity = this.savingsRepository.findById(id)
                .orElseThrow(() -> new NoAvailableDataException("Cannot delete missing entity"));

        BigDecimal amount = entity.getAmount();

        if (amount.compareTo(BigDecimal.ZERO) > 0) {

            MathContext mc = new MathContext(2);

            BigDecimal amountPerUser = amount.divide(BigDecimal.valueOf(entity.getOwners().size()), mc);

            CreateIncomeDTO closedSavingDTO = CreateIncomeDTO.builder()
                    .amount(amountPerUser)
                    .description("Closed saving")
                    .createdOn(LocalDateTime.now())
                    .incomeCategory(IncomeCategory.SAVINGS.name())
                    .build();

            IncomeEntity closedSaving = this.modelMapper.map(closedSavingDTO, IncomeEntity.class);

            entity.getOwners().forEach(o -> o.addIncome(closedSaving));

        }

            this.savingsRepository.deleteById(id);
    }
}
