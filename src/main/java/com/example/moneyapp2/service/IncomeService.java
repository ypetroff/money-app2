package com.example.moneyapp2.service;

import com.example.moneyapp2.exception.NoAvailableDataException;
import com.example.moneyapp2.model.dto.income.CreateIncomeDTO;
import com.example.moneyapp2.model.dto.income.IncomeDetailsDTO;
import com.example.moneyapp2.model.dto.income.IncomeInfoDTO;
import com.example.moneyapp2.model.entity.IncomeCategoryEntity;
import com.example.moneyapp2.model.entity.IncomeEntity;
import com.example.moneyapp2.model.entity.user.UserEntity;
import com.example.moneyapp2.model.enums.IncomeCategory;
import com.example.moneyapp2.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IncomeService {
    
    private final IncomeRepository incomeRepository;

    private final IncomeCategoryService incomeCategoryService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public boolean isIncomeRepositoryEmpty() {
        return this.incomeRepository.count() == 0;
    }

    public BigDecimal getTotalIncomeOnTheApp() {
        return this.incomeRepository.allIncomeSum().orElse(BigDecimal.ZERO);
    }

    public BigDecimal getIncomeOfUser(String username) {
        return totalAmountBigDecimal(username);
    }

    private BigDecimal totalAmountBigDecimal(String username) {
        return getByOwnerUsername(username)
                .orElseThrow(() -> new NoAvailableDataException("Username based on Principal not found"))
                .stream()
                .map(IncomeEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Optional<List<IncomeEntity>> getByOwnerUsername(String username) {
        return this.incomeRepository.findByOwnerUsername(username);
    }

    public   List<IncomeInfoDTO> addNewIncomeAndReturnAllIncomeOfUser(CreateIncomeDTO addIncomeDTO, String username) {

        createEntityAndSaveIt(addIncomeDTO, username);

        return listOfIncomeInfoDTO(username);
    }

    public void createEntityAndSaveIt(CreateIncomeDTO addIncomeDTO, String username) {
        IncomeEntity entity = this.modelMapper.map(addIncomeDTO, IncomeEntity.class);
        entity.setOwner(this.modelMapper.map(this.userService.findUser(username), UserEntity.class));
        entity.setIncomeCategory(this.incomeCategoryService.addCategory(addIncomeDTO.getIncomeCategory()));

        this.incomeRepository.saveAndFlush(entity);
    }

    private List<IncomeInfoDTO> listOfIncomeInfoDTO(String username) {

        return getByOwnerUsername(username).stream()
                .map(i -> this.modelMapper.map(i, IncomeInfoDTO.class))
                .toList();
    }

    public   List<IncomeInfoDTO> getAllIncomeOfUser(String username) {

        return listOfIncomeInfoDTO(username);
    }

    public IncomeDetailsDTO getDetailsOfIncome(Long id) {

        return this.modelMapper.map(this.incomeRepository.findById(id)
                .orElseThrow(() -> new NoAvailableDataException(String.format("Income with id: %d not found", id))),
                IncomeDetailsDTO.class);
    }

    public IncomeDetailsDTO editIncome(Long id, CreateIncomeDTO changedIncomeInfo) {

        IncomeEntity entity = this.incomeRepository.findById(id)
                .orElseThrow(() -> new NoAvailableDataException(String.format("Income with id: %d not found", id)));

        this.modelMapper.map(changedIncomeInfo, entity);

        this.incomeRepository.saveAndFlush(entity);

        return this.modelMapper.map(entity, IncomeDetailsDTO.class);
    }
}

