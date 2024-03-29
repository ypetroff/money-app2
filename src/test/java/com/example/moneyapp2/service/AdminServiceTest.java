package com.example.moneyapp2.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.example.moneyapp2.model.dto.AdminDashboardDTO;
import com.example.moneyapp2.model.dto.user.UserForAdminPanelDTO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UserService mockUserService;

    @Mock
    private IncomeService mockIncomeService;

    @Mock
    private ExpenseService mockExpenseService;

    private AdminService toTest;

    @BeforeEach
    void setUp() {
        toTest = new AdminService(mockUserService, mockIncomeService, mockExpenseService);
    }

    @Test
    public void testProvideAdminDashboardData_DisplaysDataWithUsersInDatabase() {

        UserForAdminPanelDTO user = UserForAdminPanelDTO.builder()
                .id(1L)
                .username("Test")
                .roles(List.of("USER"))
                .build();

        when(mockUserService.getTotalNumberOfAppUsers())
                .thenReturn(1L);
        when(mockUserService.getAllUsersForAdminPanel())
                .thenReturn(List.of(user));
        when(mockIncomeService.getTotalIncomeOnTheApp())
                .thenReturn(BigDecimal.TEN);
        when(mockExpenseService.getTotalExpenseOnTeApp())
                .thenReturn(BigDecimal.ONE);

        AdminDashboardDTO adminDashboardDTO = toTest.provideAdminDashboardData();

        assertNotNull(adminDashboardDTO);
        assertEquals(user.getId(), adminDashboardDTO.getUsers().get(0).getId());
        assertEquals(BigDecimal.TEN, adminDashboardDTO.getTotalIncome());
        assertEquals(1, (long) adminDashboardDTO.getTotalUsersCount());
    }

    @Test
    public void testProvideAdminDashboardData_DisplaysDataWithoutUsersInDatabase() {

        when(mockUserService.getTotalNumberOfAppUsers())
                .thenReturn(0L);
        when(mockUserService.getAllUsersForAdminPanel())
                .thenReturn(new ArrayList<>());
        when(mockIncomeService.getTotalIncomeOnTheApp())
                .thenReturn(BigDecimal.ZERO);
        when(mockExpenseService.getTotalExpenseOnTeApp())
                .thenReturn(BigDecimal.ZERO);

        AdminDashboardDTO adminDashboardDTO = toTest.provideAdminDashboardData();

        assertEquals(BigDecimal.ZERO, adminDashboardDTO.getTotalIncome());
        assertEquals(BigDecimal.ZERO, adminDashboardDTO.getTotalExpenses());
        assertEquals(0, adminDashboardDTO.getUsers().size());
    }
}