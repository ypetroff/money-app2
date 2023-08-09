package com.example.moneyapp2.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.moneyapp2.exception.NoAvailableDataException;
import com.example.moneyapp2.model.dto.saving.CreateSavingDTO;
import com.example.moneyapp2.model.dto.saving.EditSavingDTO;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class SavingServiceTest {

  @Mock private SavingsRepository mockSavingsRepository;

  @Mock private UserService mockUserService;

  @Mock private IncomeService mockIncomeService;

  @Mock private ModelMapper mockModelMapper;

  private SavingService toTest;

  @BeforeEach
  void setUp() {
    toTest =
        new SavingService(
            this.mockSavingsRepository,
            this.mockUserService,
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

    when(this.mockUserService.findUser(username)).thenReturn(userDTO);
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

    when(this.mockUserService.findUser(username)).thenReturn(userDTO);
    when(this.mockModelMapper.map(userDTO, UserEntity.class)).thenReturn(userEntity);

    NoAvailableDataException exception =
        assertThrows(NoAvailableDataException.class, () -> toTest.getAllSavingOfUser(username));

    assertEquals(
        String.format("User with username %s does not have any savings", username),
        exception.getMessage());
  }
  @Test
  void getAllSavingOfUnexistingUser() {
    String username = "test-username";

    when(this.mockUserService.findUser(username)).thenReturn(null);

    UsernameNotFoundException exception =
            assertThrows(UsernameNotFoundException.class, () -> toTest.getAllSavingOfUser(username));

    assertEquals(
            String.format(
                    "%s is not present in the database. The variable was extracted from the Principal",
                    username),
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

    when(this.mockUserService.findUser(username)).thenReturn(new UserForServicesDTO());
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
  void editSaving() {
    long id = 1;
    CreateSavingDTO savingDTO =
        CreateSavingDTO.builder()
            .goal("test-goal")
            .amount(BigDecimal.ONE)
            .dateOfCreation(LocalDateTime.of(1999, 3, 23, 12, 0, 0))
            .endDate(LocalDate.of(2000, 3, 22))
            .build();

    SavingEntity entity =
        SavingEntity.builder()
            .dateOfCreation(savingDTO.getDateOfCreation())
            .endDate(savingDTO.getEndDate())
            .goal(savingDTO.getGoal())
            .amount(BigDecimal.TEN)
            .build();
    entity.setId(id);

    when(this.mockSavingsRepository.findById(id)).thenReturn(Optional.of(entity));
    doAnswer(
            invocation -> {
              SavingEntity e = invocation.getArgument(1);
              e.setId(entity.getId());
              e.setDateOfCreation(savingDTO.getDateOfCreation());
              e.setEndDate(savingDTO.getEndDate());
              e.setAmount(savingDTO.getAmount());

              return null;
            })
        .when(this.mockModelMapper)
        .map(savingDTO, entity);
    doReturn(
            SavingDetailsDTO.builder()
                .id(entity.getId())
                .endDate(savingDTO.getEndDate())
                .dateOfCreation(savingDTO.getDateOfCreation())
                .goal(savingDTO.getGoal())
                .amount(savingDTO.getAmount())
                .build())
        .when(this.mockModelMapper)
        .map(entity, SavingDetailsDTO.class);

    SavingDetailsDTO actual = toTest.editSaving(id, savingDTO);

    verify(this.mockSavingsRepository).saveAndFlush(entity);

    assertEquals(id, actual.getId());
    assertEquals(savingDTO.getAmount(), actual.getAmount());
    assertEquals(savingDTO.getDateOfCreation(), actual.getDateOfCreation());
    assertEquals(savingDTO.getEndDate(), actual.getEndDate());
  }

  @Test
  void editSavingThrows() {
    long id = 1;

    NoAvailableDataException exception =
        assertThrows(
            NoAvailableDataException.class, () -> toTest.editSaving(id, new CreateSavingDTO()));

    assertEquals(String.format("Saving with id: %d not found", id), exception.getMessage());
  }

  @Test
  void deleteSavingWithGoalName() {
    long id = 1;

    SavingEntity entity =
        SavingEntity.builder()
            .goal("test-goal")
            .amount(BigDecimal.ONE)
            .dateOfCreation(LocalDateTime.of(1999, 3, 23, 12, 0, 0))
            .endDate(LocalDate.of(2000, 3, 22))
            .owners(List.of(new UserEntity(), new UserEntity()))
            .build();
    entity.setId(id);

    when(this.mockSavingsRepository.findById(id)).thenReturn(Optional.of(entity));

    toTest.deleteSaving(id);

    verify(mockSavingsRepository).findById(id);
    verify(mockIncomeService, times(2)).createEntityAndSaveIt(any(), any());
    verify(mockSavingsRepository).deleteById(id);
  }

  @Test
  void deleteSavingWithoutGoalNameProvidedByUser() {
    long id = 1;

    SavingEntity entity =
        SavingEntity.builder()
            .goal("not provided by user")
            .amount(BigDecimal.ONE)
            .dateOfCreation(LocalDateTime.of(1999, 3, 23, 12, 0, 0))
            .endDate(LocalDate.of(2000, 3, 22))
            .owners(List.of(new UserEntity(), new UserEntity()))
            .build();
    entity.setId(id);

    when(this.mockSavingsRepository.findById(id)).thenReturn(Optional.of(entity));

    toTest.deleteSaving(id);

    verify(mockSavingsRepository).findById(id);
    verify(mockIncomeService, times(2)).createEntityAndSaveIt(any(), any());
    verify(mockSavingsRepository).deleteById(id);
  }

  @Test
  void unauthorizedViewUserIsOwner() {
    long id = 1;
    String username = "owner";

    UserEntity owner = new UserEntity();
    owner.setUsername("owner");

    UserEntity contributor = new UserEntity();
    contributor.setUsername("contributor");

    SavingEntity entity =
        SavingEntity.builder().owners(List.of(owner)).contributors(List.of(contributor)).build();
    entity.setId(id);

    when(this.mockSavingsRepository.findById(id)).thenReturn(Optional.of(entity));

    assertFalse(toTest.unauthorizedView(id, username));
  }

  @Test
  void unauthorizedViewUserIsContributor() {
    long id = 1;
    String username = "contributor";

    UserEntity owner = new UserEntity();
    owner.setUsername("owner");

    UserEntity contributor = new UserEntity();
    contributor.setUsername("contributor");

    SavingEntity entity =
        SavingEntity.builder().owners(List.of(owner)).contributors(List.of(contributor)).build();
    entity.setId(id);

    when(this.mockSavingsRepository.findById(id)).thenReturn(Optional.of(entity));

    assertFalse(toTest.unauthorizedView(id, username));
  }

  @Test
  void unauthorizedViewUserIsNotOwnerAndNotContributor() {
    long id = 1;
    String username = "another-user";

    UserEntity owner = new UserEntity();
    owner.setUsername("owner");

    UserEntity contributor = new UserEntity();
    contributor.setUsername("contributor");

    SavingEntity entity =
        SavingEntity.builder().owners(List.of(owner)).contributors(List.of(contributor)).build();
    entity.setId(id);

    when(this.mockSavingsRepository.findById(id)).thenReturn(Optional.of(entity));

    assertTrue(toTest.unauthorizedView(id, username));
  }

  @Test
  void unauthorizedToModifyWhenUserIsOwner() {
    long id = 1;
    String username = "owner";

    UserEntity owner = new UserEntity();
    owner.setUsername("owner");

    SavingEntity entity = SavingEntity.builder().owners(List.of(owner)).build();
    entity.setId(id);

    when(this.mockSavingsRepository.findById(id)).thenReturn(Optional.of(entity));

    assertFalse(toTest.unauthorizedToModify(id, username));
  }

  @Test
  void unauthorizedToModifyUserIsContributor() {
    long id = 1;
    String username = "contributor";

    UserEntity owner = new UserEntity();
    owner.setUsername("owner");

    UserEntity contributor = new UserEntity();
    contributor.setUsername("contributor");

    SavingEntity entity =
        SavingEntity.builder().owners(List.of(owner)).contributors(List.of(contributor)).build();
    entity.setId(id);

    when(this.mockSavingsRepository.findById(id)).thenReturn(Optional.of(entity));

    assertTrue(toTest.unauthorizedToModify(id, username));
  }

  @Test
  void unauthorizedToModifyUserIsNotOwnerAndIsNotContributor() {
    long id = 1;
    String username = "another-user";

    UserEntity owner = new UserEntity();
    owner.setUsername("owner");

    UserEntity contributor = new UserEntity();
    contributor.setUsername("contributor");

    SavingEntity entity =
        SavingEntity.builder().owners(List.of(owner)).contributors(List.of(contributor)).build();
    entity.setId(id);

    when(this.mockSavingsRepository.findById(id)).thenReturn(Optional.of(entity));

    assertTrue(toTest.unauthorizedToModify(id, username));
  }

  @Test
  void getSingleSaving() {
    long id = 1;

    UserEntity owner = new UserEntity();
    owner.setUsername("owner");

    UserEntity contributor = new UserEntity();
    contributor.setUsername("contributor");

    SavingEntity entity =
        SavingEntity.builder()
            .amount(BigDecimal.ONE)
            .owners(List.of(owner))
            .contributors(List.of(contributor))
            .build();
    entity.setId(id);

    when(this.mockSavingsRepository.findById(id)).thenReturn(Optional.of(entity));

    EditSavingDTO actual = toTest.getSingleSaving(id);

    assertEquals(entity.getAmount(), actual.getAmount());
    assertEquals(entity.getOwners().get(0).getUsername(), actual.getOwners().get(0));
    assertEquals(entity.getContributors().get(0).getUsername(), actual.getContributors().get(0));
  }

  @Test
  void getSingleSavingThrowsException() {
    long id = 1;

    NoAvailableDataException exception =
        assertThrows(NoAvailableDataException.class, () -> toTest.getSingleSaving(id));

    assertEquals("Non existent saving", exception.getMessage());
  }

  @Test
  void maintenance() {
    SavingEntity toRemain = SavingEntity.builder()
            .endDate(LocalDate.now().plusDays(10))
                                        .build();
    toRemain.setId(1L);

    SavingEntity forDeletion =
        SavingEntity.builder()
                    .goal("test-goal")
                    .endDate(LocalDate.now())
            .amount(BigDecimal.TEN)
            .owners(List.of(new UserEntity()))
            .build();
    forDeletion.setId(2L);

    when(mockSavingsRepository.findAll())
            .thenReturn(List.of(toRemain, forDeletion));
    when(this.mockSavingsRepository.findById(forDeletion.getId()))
            .thenReturn(Optional.of(forDeletion));


    toTest.maintenance();

    verify(mockSavingsRepository).findAll();
    verify(mockSavingsRepository).findById(forDeletion.getId());
    verify(mockIncomeService).createEntityAndSaveIt(any(), any());
    verify(mockSavingsRepository).deleteById(forDeletion.getId());
  }
}
