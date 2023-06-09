package com.example.moneyapp2.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.example.moneyapp2.exception.NoAvailableDataException;
import com.example.moneyapp2.model.dto.saving.CreateSavingDTO;
import com.example.moneyapp2.model.dto.saving.SavingDetailsDTO;
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
    when(this.mockModelMapper.map(savingEntity, SavingInfoDTO.class))
        .thenReturn(
            SavingInfoDTO.builder()
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
  void getAllSavingOfUserWithoutSavings() {
    String username = "test-username";
    UserEntity userEntity = new UserEntity();
    UserForServicesDTO userDTO = new UserForServicesDTO();

    when(this.mockUserServive.findUser(username)).thenReturn(userDTO);
    when(this.mockModelMapper.map(userDTO, UserEntity.class)).thenReturn(userEntity);

    NoAvailableDataException exception =
        assertThrows(NoAvailableDataException.class, () -> toTest.getAllSavingOfUser(username));

    assertEquals(
        String.format("User with username %s does not have any savings", username),
        exception.getMessage());
  }

  @Test
  void addNewSavingAndReturnAllSavingsOfUser() {
    String username = "test-username";
    UserEntity userEntity = new UserEntity();

    CreateSavingDTO saving =
        CreateSavingDTO.builder()
            .amount(BigDecimal.ONE)
            .goal("test-goal")
            .owners(List.of(username))
            .contributors(List.of(username))
            .dateOfCreation(LocalDateTime.of(2000, 3, 3, 10, 0, 0))
            .endDate(LocalDate.of(2000, 3, 22))
            .build();

    SavingEntity savingEntity =
        SavingEntity.builder()
            .contributors(List.of(new UserEntity()))
            .owners(List.of(new UserEntity()))
            .amount(BigDecimal.ONE)
            .goal("test-goal")
            .dateOfCreation(LocalDateTime.of(1999, 3, 23, 12, 0, 0))
            .endDate(LocalDate.of(2000, 3, 22))
            .build();
    savingEntity.setId(1L);

    when(this.mockUserServive.findUser(username)).thenReturn(new UserForServicesDTO());
    when((this.mockModelMapper.map(new UserForServicesDTO(), UserEntity.class)))
        .thenReturn(UserEntity.builder().username(username).build());
    when(this.mockModelMapper.map(new UserForServicesDTO(), UserEntity.class))
        .thenReturn(userEntity);
    when(this.mockSavingsRepository.findAllByOwnersContaining(userEntity))
        .thenReturn(Optional.of(List.of(savingEntity)));
    when(this.mockModelMapper.map(savingEntity, SavingInfoDTO.class))
        .thenReturn(
            SavingInfoDTO.builder()
                .id(savingEntity.getId())
                .amount(savingEntity.getAmount())
                .goal(savingEntity.getGoal())
                .endDate(savingEntity.getEndDate())
                .build());

    List<SavingInfoDTO> actualSavings =
        toTest.addNewSavingAndReturnAllSavingsOfUser(saving, username);

    assertEquals(1, actualSavings.size());

    SavingInfoDTO actual = actualSavings.get(0);

    assertEquals(savingEntity.getId(), actual.getId());
    assertEquals(savingEntity.getAmount(), actual.getAmount());
    assertEquals(savingEntity.getEndDate(), actual.getEndDate());
    assertEquals(savingEntity.getGoal(), actual.getGoal());
  }

  @Test
  void savingNotPresentReturnsTrue() {
    assertTrue(toTest.SavingNotPresent(1L));
  }

  @Test
  void savingNotPresentReturnsFalse() {
    when(this.mockSavingsRepository.existsById(1L)).thenReturn(true);
    assertFalse(toTest.SavingNotPresent(1L));
  }

  @Test
  void getDetailsOfSavingThrows() {
    long id = 1;

    NoAvailableDataException exception =
        assertThrows(NoAvailableDataException.class, () -> toTest.getDetailsOfSaving(id));

    assertEquals(String.format("Saving with id: %d not found", id), exception.getMessage());
  }

  @Test
  void getDetailsOfSaving() {
    long id = 1;
    SavingEntity savingEntity =
        SavingEntity.builder()
            .contributors(List.of(new UserEntity()))
            .owners(List.of(new UserEntity()))
            .amount(BigDecimal.ONE)
            .goal("test-goal")
            .dateOfCreation(LocalDateTime.of(1999, 3, 23, 12, 0, 0))
            .endDate(LocalDate.of(2000, 3, 22))
            .build();
    savingEntity.setId(id);

    when(this.mockSavingsRepository.findById(id)).thenReturn(Optional.of(savingEntity));
    when(this.mockModelMapper.map(savingEntity, SavingDetailsDTO.class))
        .thenReturn(
            SavingDetailsDTO.builder()
                .id(savingEntity.getId())
                .amount(savingEntity.getAmount())
                .goal(savingEntity.getGoal())
                .owners(savingEntity.getOwners().stream().map(UserEntity::getUsername).toList())
                .contributors(
                    savingEntity.getContributors().stream().map(UserEntity::getUsername).toList())
                .dateOfCreation(savingEntity.getDateOfCreation())
                .endDate(savingEntity.getEndDate())
                .build());

    SavingDetailsDTO actual = toTest.getDetailsOfSaving(id);

    assertEquals(savingEntity.getId(), actual.getId());
    assertEquals(savingEntity.getGoal(), actual.getGoal());
    assertEquals(savingEntity.getAmount(), actual.getAmount());
    assertEquals(savingEntity.getDateOfCreation(), actual.getDateOfCreation());
    assertEquals(savingEntity.getEndDate(), actual.getEndDate());
  }

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
