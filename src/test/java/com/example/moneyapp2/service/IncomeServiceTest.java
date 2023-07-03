package com.example.moneyapp2.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.moneyapp2.exception.NoAvailableDataException;
import com.example.moneyapp2.model.dto.income.CreateIncomeDTO;
import com.example.moneyapp2.model.dto.income.EditIncomeDTO;
import com.example.moneyapp2.model.dto.income.IncomeDetailsDTO;
import com.example.moneyapp2.model.dto.income.IncomeInfoDTO;
import com.example.moneyapp2.model.dto.user.UserForServicesDTO;
import com.example.moneyapp2.model.entity.IncomeCategoryEntity;
import com.example.moneyapp2.model.entity.IncomeEntity;
import com.example.moneyapp2.model.entity.user.UserEntity;
import com.example.moneyapp2.model.enums.IncomeCategory;
import com.example.moneyapp2.repository.IncomeRepository;
import java.math.BigDecimal;
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
class IncomeServiceTest {

  @Mock private IncomeRepository mockIncomeRepository;

  @Mock private IncomeCategoryService mockIncomeCategoryService;

  @Mock private UserService mockUserService;

  @Mock private ModelMapper mockModelMapper;

  private IncomeService toTest;

  @BeforeEach
  void setUp() {
    toTest =
        new IncomeService(
            this.mockIncomeRepository,
            this.mockIncomeCategoryService,
            this.mockUserService,
            this.mockModelMapper);
  }

  @Test
  void getTotalIncomeOnTheAppWithUser() {
    Optional<BigDecimal> expectedSum = Optional.of(BigDecimal.TEN);

    when(this.mockIncomeRepository.allIncomeSum()).thenReturn(expectedSum);

    BigDecimal actualSum = toTest.getTotalIncomeOnTheApp();
    assertEquals(expectedSum.get(), actualSum);
  }

  @Test
  void getTotalIncomeOnTheAppWithoutUsers() {
    BigDecimal actualSum = toTest.getTotalIncomeOnTheApp();

    assertEquals(BigDecimal.ZERO, actualSum);
  }

  @Test
  void getIncomeOfUserWithIncome() {
    UserEntity user = UserEntity.builder().username("test").build();
    IncomeEntity income = IncomeEntity.builder().amount(BigDecimal.TEN).build();

    when(this.mockUserService.findUser(user.getUsername())).thenReturn(new UserForServicesDTO());
    when(this.mockIncomeRepository.findByOwnerUsername(user.getUsername()))
        .thenReturn(Optional.of(List.of(income)));

    BigDecimal incomeOfUser = toTest.getIncomeOfUser(user.getUsername());
    assertEquals(income.getAmount(), incomeOfUser);
  }

  @Test
  void getIncomeOfUserWithoutIncome() {
    String username = "test";

    when(this.mockUserService.findUser(username)).thenReturn(new UserForServicesDTO());
    when(this.mockIncomeRepository.findByOwnerUsername(username))
        .thenReturn(Optional.of(List.of()));

    BigDecimal incomeOfUser = toTest.getIncomeOfUser(username);
    assertEquals(BigDecimal.ZERO, incomeOfUser);
  }

  @Test
  void getIncomeOfInvalidUsername() {
    String username = "test";
    assertThrows(
        UsernameNotFoundException.class,
        () -> toTest.createEntityAndSaveIt(new CreateIncomeDTO(), username));
  }

  @Test
  void addNewIncomeAndReturnAllIncomeOfUser() {
    String username = "test";
    CreateIncomeDTO incomeDTO =
        CreateIncomeDTO.builder()
            .incomeCategory(IncomeCategory.BANK.name())
            .amount(BigDecimal.TEN)
            .createdOn(LocalDateTime.of(2022, 2, 22, 22, 22, 22))
            .build();
    IncomeEntity income =
        IncomeEntity.builder()
            .incomeCategory(new IncomeCategoryEntity(IncomeCategory.BANK))
            .amount(BigDecimal.TEN)
            .owner(new UserEntity())
            .createdOn(LocalDateTime.of(2022, 2, 22, 22, 22, 22))
            .build();
    income.setId(1L);
    UserForServicesDTO userForServicesDTO =
        UserForServicesDTO.builder().id(1L).username(username).build();

    doReturn(income).when(this.mockModelMapper).map(incomeDTO, IncomeEntity.class);
    doReturn(userForServicesDTO).when(this.mockUserService).findUser(username);
    doReturn(new UserEntity()).when(this.mockModelMapper).map(userForServicesDTO, UserEntity.class);
    when(this.mockIncomeCategoryService.addCategory(IncomeCategory.BANK.name()))
        .thenReturn(new IncomeCategoryEntity(IncomeCategory.BANK));
    when(this.mockIncomeRepository.findByOwnerUsername(username))
        .thenReturn(Optional.of(List.of(income)));
    doReturn(
            IncomeInfoDTO.builder()
                .id(income.getId())
                .amount(income.getAmount())
                .incomeCategory(income.getIncomeCategory().getCategory().name())
                .build())
        .when(this.mockModelMapper)
        .map(income, IncomeInfoDTO.class);

    List<IncomeInfoDTO> incomeInfoDTOS =
        toTest.addNewIncomeAndReturnAllIncomeOfUser(incomeDTO, username);
    assertEquals(1, incomeInfoDTOS.size());
    IncomeInfoDTO actual = incomeInfoDTOS.get(0);
    assertEquals(income.getId(), actual.getId());
    assertEquals(income.getAmount(), actual.getAmount());
    assertEquals(income.getIncomeCategory().getCategory().name(), actual.getIncomeCategory());
  }

  @Test
  void createEntityAndSaveIt() {
    String username = "test";
    CreateIncomeDTO incomeDTO =
        CreateIncomeDTO.builder()
            .incomeCategory(IncomeCategory.BANK.name())
            .amount(BigDecimal.TEN)
            .createdOn(LocalDateTime.of(2022, 2, 22, 22, 22, 22))
            .build();
    IncomeEntity income =
        IncomeEntity.builder()
            .incomeCategory(new IncomeCategoryEntity(IncomeCategory.BANK))
            .amount(BigDecimal.TEN)
            .owner(new UserEntity())
            .createdOn(LocalDateTime.of(2022, 2, 22, 22, 22, 22))
            .build();
    income.setId(1L);

    when(this.mockModelMapper.map(incomeDTO, IncomeEntity.class)).thenReturn(income);
    when(this.mockUserService.findUser(username)).thenReturn(new UserForServicesDTO());

    toTest.createEntityAndSaveIt(incomeDTO, username);

    verify(this.mockIncomeRepository).saveAndFlush(income);
  }

  @Test
  void createEntityAndSaveItThrowsUsernameNotFound() {
    String username = "test";
    assertThrows(
        UsernameNotFoundException.class,
        () -> toTest.createEntityAndSaveIt(new CreateIncomeDTO(), username));
  }

  @Test
  void getAllIncomeOfUser() {
    String username = "test";
    IncomeEntity income =
        IncomeEntity.builder()
            .incomeCategory(new IncomeCategoryEntity(IncomeCategory.BANK))
            .amount(BigDecimal.TEN)
            .owner(new UserEntity())
            .createdOn(LocalDateTime.of(2022, 2, 22, 22, 22, 22))
            .build();
    income.setId(1L);

    IncomeInfoDTO incomeDTO =
        IncomeInfoDTO.builder()
            .incomeCategory(IncomeCategory.BANK.name())
            .amount(BigDecimal.TEN)
            .id(1L)
            .build();

    when(this.mockUserService.findUser(username)).thenReturn(new UserForServicesDTO());
    when(this.mockIncomeRepository.findByOwnerUsername(username))
        .thenReturn(Optional.of(List.of(income)));
    when(this.mockModelMapper.map(income, IncomeInfoDTO.class)).thenReturn(incomeDTO);

    List<IncomeInfoDTO> allIncomeOfUser = toTest.getAllIncomeOfUser(username);

    assertEquals(1L, allIncomeOfUser.size());
    IncomeInfoDTO incomeInfoDTO = allIncomeOfUser.get(0);
    assertEquals(income.getAmount(), incomeInfoDTO.getAmount());
    assertEquals(
        income.getIncomeCategory().getCategory().name(), incomeInfoDTO.getIncomeCategory());
    assertEquals(income.getId(), incomeInfoDTO.getId());
  }

  @Test
  void getAllIncomeOfUserWithoutIncome() {
    String username = "test";

    when(this.mockUserService.findUser(username)).thenReturn(new UserForServicesDTO());
    when(this.mockIncomeRepository.findByOwnerUsername(username)).thenReturn(Optional.empty());

    assertThrows(NoAvailableDataException.class, () -> toTest.getAllIncomeOfUser(username));
  }

  @Test
  void getDetailsOfIncomeThatDoesNotExist() {
    Long id = 1L;
    assertThrows(NoAvailableDataException.class, () -> toTest.getDetailsOfIncome(id));
  }

  @Test
  void getDetailsOfIncome() {
    Long id = 1L;
    IncomeEntity income =
        IncomeEntity.builder()
            .incomeCategory(new IncomeCategoryEntity(IncomeCategory.BANK))
            .amount(BigDecimal.TEN)
            .owner(new UserEntity())
            .createdOn(LocalDateTime.of(2022, 2, 22, 22, 22, 22))
            .build();
    income.setId(id);
    IncomeDetailsDTO incomeDetailsDTO =
        IncomeDetailsDTO.builder()
            .id(income.getId())
            .incomeCategory(income.getIncomeCategory().getCategory().name())
            .amount(income.getAmount())
            .createdOn(income.getCreatedOn().toString())
            .description(income.getDescription())
            .build();

    when(this.mockIncomeRepository.findById(id)).thenReturn(Optional.of(income));
    when(this.mockModelMapper.map(income, IncomeDetailsDTO.class)).thenReturn(incomeDetailsDTO);

    IncomeDetailsDTO actual = toTest.getDetailsOfIncome(id);

    assertEquals(incomeDetailsDTO.getId(), actual.getId());
    assertEquals(incomeDetailsDTO.getIncomeCategory(), actual.getIncomeCategory());
    assertEquals(incomeDetailsDTO.getAmount(), actual.getAmount());
    assertEquals(incomeDetailsDTO.getCreatedOn(), actual.getCreatedOn());
    assertEquals(incomeDetailsDTO.getDescription(), actual.getDescription());
  }

  @Test
  void editIncomeValidId() {
    Long id = 1L;
    IncomeEntity income =
        IncomeEntity.builder()
            .incomeCategory(new IncomeCategoryEntity(IncomeCategory.BANK))
            .amount(BigDecimal.TEN)
            .owner(new UserEntity())
            .description("original_description")
            .createdOn(LocalDateTime.of(2022, 2, 22, 22, 22, 22))
            .build();
    income.setId(id);
    CreateIncomeDTO incomeDTO =
        CreateIncomeDTO.builder()
            .incomeCategory(IncomeCategory.CASH.name())
            .amount(BigDecimal.ONE)
            .description("new_description")
            .createdOn(LocalDateTime.of(2022, 2, 22, 22, 22, 22))
            .build();
    IncomeDetailsDTO detailsDTO =
        IncomeDetailsDTO.builder()
            .id(income.getId())
            .incomeCategory(incomeDTO.getIncomeCategory())
            .amount(incomeDTO.getAmount())
            .description(incomeDTO.getDescription())
            .createdOn(incomeDTO.getCreatedOn().toString())
            .build();

    when(this.mockIncomeRepository.findById(id)).thenReturn(Optional.of(income));
    doAnswer(
            invocation -> {
              IncomeEntity e = invocation.getArgument(1);
              e.setId(income.getId());
              e.setIncomeCategory(
                  new IncomeCategoryEntity(IncomeCategory.valueOf(incomeDTO.getIncomeCategory())));
              e.setOwner(income.getOwner());
              e.setAmount(incomeDTO.getAmount());
              e.setDescription(incomeDTO.getDescription());
              e.setCreatedOn(incomeDTO.getCreatedOn());
              return null;
            })
        .when(this.mockModelMapper)
        .map(incomeDTO, income);
    doReturn(detailsDTO).when(this.mockModelMapper).map(income, IncomeDetailsDTO.class);

    IncomeDetailsDTO actual = toTest.editIncome(id, incomeDTO);

    verify(this.mockIncomeRepository).findById(1L);
    verify(this.mockModelMapper).map(incomeDTO, income);
    verify(this.mockIncomeRepository).saveAndFlush(income);
    verify(this.mockModelMapper).map(income, IncomeDetailsDTO.class);

    assertEquals(incomeDTO.getIncomeCategory(), actual.getIncomeCategory());
    assertEquals(incomeDTO.getAmount(), actual.getAmount());
    assertEquals(incomeDTO.getDescription(), actual.getDescription());
    assertEquals(incomeDTO.getCreatedOn().toString(), actual.getCreatedOn());
  }

  @Test
  void editIncomeInvalidId() {
    Long id = 1L;
    assertThrows(
        NoAvailableDataException.class, () -> toTest.editIncome(id, new CreateIncomeDTO()));
  }

  @Test
  void incomeNotPresentReturnsFalse() {
    long id = 1L;
    when(this.mockIncomeRepository.existsById(id)).thenReturn(true);
    assertFalse(toTest.IncomeNotPresent(id));
  }

  @Test
  void incomeNotPresentReturnsTrue() {
    long id = 1L;
    assertTrue(toTest.IncomeNotPresent(id));
  }

  @Test
  void deleteIncome() {
    toTest.deleteIncome(1L);
    verify(this.mockIncomeRepository).deleteById(1L);
  }

  @Test
  void unauthorizedUserReturnsFalse() {
    String username = "username";
    long id = 1L;

    IncomeEntity entity =
        IncomeEntity.builder().owner(UserEntity.builder().username(username).build()).build();

    when(this.mockUserService.findUser(username)).thenReturn(new UserForServicesDTO());
    when(this.mockIncomeRepository.findById(id)).thenReturn(Optional.of(entity));

    assertFalse(toTest.unauthorizedUser(id, username));
  }

  @Test
  void unauthorizedUserReturnsTrue() {
    String username = "username";
    long id = 1L;

    UserEntity user = UserEntity.builder().username("test").build();

    IncomeEntity entity = IncomeEntity.builder().owner(user).build();

    when(this.mockUserService.findUser(username)).thenReturn(new UserForServicesDTO());
    when(this.mockIncomeRepository.findById(id)).thenReturn(Optional.of(entity));

    assertTrue(toTest.unauthorizedUser(id, username));
  }

  @Test
  void unauthorizedUserThrowsUsernameNotFound() {
    String username = "username";
    long id = 1L;
    assertThrows(UsernameNotFoundException.class, () -> toTest.unauthorizedUser(id, username));
  }

  @Test
  void unauthorizedUserThrowsNoAvlData() {
    String username = "username";
    long id = 1L;

    when(this.mockUserService.findUser(username)).thenReturn(new UserForServicesDTO());

    assertThrows(NoAvailableDataException.class, () -> toTest.unauthorizedUser(id, username));
  }

  @Test
  void getSingleIncomeWithValidId() {
    long id = 1L;
    IncomeEntity entity =
        IncomeEntity.builder()
            .amount(BigDecimal.ONE)
            .description("test-description")
            .createdOn(LocalDateTime.of(2000, 2, 22, 22, 22, 22))
            .incomeCategory(new IncomeCategoryEntity(IncomeCategory.BANK))
            .owner(new UserEntity())
            .build();

    entity.setId(id);
    EditIncomeDTO dto =
        EditIncomeDTO.builder()
            .id(entity.getId())
            .amount(entity.getAmount())
            .description(entity.getDescription())
            .createdOn(entity.getCreatedOn())
            .incomeCategory(entity.getIncomeCategory().getCategory().name())
            .build();

    when(this.mockIncomeRepository.findById(id)).thenReturn(Optional.of(entity));
    when(this.mockModelMapper.map(entity, EditIncomeDTO.class)).thenReturn(dto);

    EditIncomeDTO actual = toTest.getSingleIncome(id);

    verify(this.mockIncomeRepository).findById(id);

    assertEquals(entity.getAmount(), actual.getAmount());
    assertEquals(entity.getDescription(), actual.getDescription());
    assertEquals(entity.getCreatedOn(), actual.getCreatedOn());
    assertEquals(entity.getIncomeCategory().getCategory().name(), actual.getIncomeCategory());
  }

  @Test
  void getSingleIncomeThrowsWithInvalidId() {
    assertThrows(NoAvailableDataException.class, () -> toTest.getSingleIncome(1L));
  }

  @Test
  void maintenance() {
    IncomeEntity entityToRemain =
        IncomeEntity.builder().createdOn(LocalDateTime.of(2022, 2, 22, 22, 22, 22)).build();
    IncomeEntity entityToBeDeleted =
        IncomeEntity.builder().createdOn(LocalDateTime.of(2000, 2, 22, 22, 22, 22)).build();

    when(this.mockIncomeRepository.findAll())
        .thenReturn(List.of(entityToRemain, entityToBeDeleted));

    toTest.maintenance();

    verify(this.mockIncomeRepository).findAll();
  }
}
