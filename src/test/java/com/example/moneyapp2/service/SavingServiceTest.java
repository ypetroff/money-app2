package com.example.moneyapp2.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.example.moneyapp2.model.dto.saving.SavingInfoDTO;
import com.example.moneyapp2.model.dto.user.UserForServicesDTO;
import com.example.moneyapp2.model.entity.SavingEntity;
import com.example.moneyapp2.model.entity.user.UserEntity;
import com.example.moneyapp2.repository.SavingsRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class SavingServiceTest {

  @Mock private SavingsRepository mockSavingsRepository;

  @Mock private UserService mockUserServive;

  @Mock private IncomeService mockIncomeService;

  @Mock private ModelMapper mockModelMapper;

  private SavingService toTest;

  @BeforeEach
  void setUp() {
    toTest =
        new SavingService(
            this.mockSavingsRepository,
            this.mockUserServive,
            this.mockIncomeService,
            this.mockModelMapper);
  }

  @Test
  void getAllSavingOfUser() {
    String username = "test-username";
    UserEntity userEntity = new UserEntity();
    UserForServicesDTO userDTO = new UserForServicesDTO();
    SavingEntity savingEntity =
        SavingEntity.builder()
            .contributors(List.of(userEntity))
            .owners(List.of(userEntity))
            .amount(BigDecimal.ONE)
            .goal("test-goal")
            .dateOfCreation(LocalDateTime.of(1999, 3, 23, 12, 0, 0))
            .endDate(LocalDate.of(2000, 3, 22))
            .build();
    savingEntity.setId(1L);

    when(this.mockUserServive.findUser(username)).thenReturn(userDTO);
    when(this.mockModelMapper.map(userDTO, UserEntity.class)).thenReturn(userEntity);
    when(this.mockSavingsRepository.findAllByOwnersContaining(userEntity))
        .thenReturn(Optional.of(List.of(savingEntity)));
    when(this.mockModelMapper.map(savingEntity, SavingInfoDTO.class)).thenReturn(SavingInfoDTO.builder()
                                                                             .id(savingEntity.getId())
                                                                             .amount(savingEntity.getAmount())
                                                                             .goal(savingEntity.getGoal())
                                                                             .endDate(savingEntity.getEndDate())
                                                                                      .build());

    List<SavingInfoDTO> actualList = toTest.getAllSavingOfUser(username);

    assertEquals(1, actualList.size());

    SavingInfoDTO actual = actualList.get(0);

    assertEquals(savingEntity.getAmount(), actual.getAmount());
    assertEquals(savingEntity.getGoal(), actual.getGoal());
    assertEquals(savingEntity.getEndDate(), actual.getEndDate());
  }

  @Test
  void addNewSavingAndReturnAllSavingsOfUser() {}

  @Test
  void createEntityAndSaveIt() {}

  @Test
  void savingNotPresent() {}

  @Test
  void getDetailsOfSaving() {}

  @Test
  void editSaving() {}

  @Test
  void deleteSaving() {}

  @Test
  void unauthorizedView() {}

  @Test
  void unauthorizedToModify() {}

  @Test
  void getSingleSaving() {}

  @Test
  void maintenance() {}
}
