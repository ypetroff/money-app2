package com.example.moneyapp2.service;

import com.example.moneyapp2.model.dto.saving.CreateSavingDTO;
import com.example.moneyapp2.model.dto.user.UserForServicesDTO;
import com.example.moneyapp2.model.entity.ExpenseEntity;
import com.example.moneyapp2.model.entity.SavingEntity;
import com.example.moneyapp2.model.entity.user.UserEntity;
import com.example.moneyapp2.repository.SavingsRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SavingService {

    private final SavingsRepository savingsRepository;
    private final UserService userService;
    private  final ModelMapper modelMapper;

    public void createEntityAndSaveIt(CreateSavingDTO savingDTO) {

        SavingEntity entity = this.modelMapper.map(savingDTO, SavingEntity.class);

        addOwnersAsUserEntityToSavingEntity(savingDTO, entity);

        addContributorsAsUserEntityToSavingEntity(savingDTO, entity);

        this.savingsRepository.saveAndFlush(entity);
    }

    private void addContributorsAsUserEntityToSavingEntity(CreateSavingDTO savingDTO, SavingEntity entity) {
        savingDTO.getContributors().stream()
                .map(c -> this.modelMapper.map(getUser(c), UserEntity.class))
                .forEach(entity::addContributor);
    }

    private void addOwnersAsUserEntityToSavingEntity(CreateSavingDTO savingDTO, SavingEntity entity) {

//        for (String owner : savingDTO.getOwners()) {
//            UserForServicesDTO user = getUser(owner);
//            System.out.println(user.getUsername());
//
//            UserEntity entity1 = this.modelMapper.map(user, UserEntity.class);
//            System.out.println(entity1.getUsername());
//
//            UserEntity entity2 = this.modelMapper.map(getUser(owner), UserEntity.class);
//
//            entity.addOwner(entity1);
//            entity.addOwner(entity2);
//
//            System.out.println();
//        }


        savingDTO.getOwners().stream()
                .map(o -> this.modelMapper.map(getUser(o), UserEntity.class))
                        .forEach(entity::addOwner);
    }

    private UserForServicesDTO getUser(String o) {
        return this.userService.findUser(o);
    }
}
