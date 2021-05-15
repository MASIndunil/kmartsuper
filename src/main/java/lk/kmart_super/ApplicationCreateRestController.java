package lk.kmart_super;


import lk.kmart_super.asset.common_asset.model.enums.CivilStatus;
import lk.kmart_super.asset.common_asset.model.enums.Gender;
import lk.kmart_super.asset.common_asset.model.enums.Title;
import lk.kmart_super.asset.employee.entity.Employee;
import lk.kmart_super.asset.employee.entity.enums.Designation;
import lk.kmart_super.asset.employee.entity.enums.EmployeeStatus;
import lk.kmart_super.asset.employee.service.EmployeeService;
import lk.kmart_super.asset.user_management.role.entity.Role;
import lk.kmart_super.asset.user_management.role.service.RoleService;
import lk.kmart_super.asset.user_management.user.entity.User;
import lk.kmart_super.asset.user_management.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.stream.Collectors;

@RestController
public class ApplicationCreateRestController {
    private final RoleService roleService;
    private final UserService userService;
    private final EmployeeService employeeService;


    @Autowired
    public ApplicationCreateRestController(RoleService roleService, UserService userService,
                                           EmployeeService employeeService) {
        this.roleService = roleService;
        this.userService = userService;
        this.employeeService = employeeService;
    }

    @GetMapping("/select/user")
    public String saveUser() {
        //roles list start
        String[] roles = {"ADMIN","PROCUREMENT_MANAGER","CASHIER","MANAGER"};
        for (String s : roles) {
            Role role = new Role();
            role.setRoleName(s);
            roleService.persist(role);
        }

//Employee
        Employee employee = new Employee();
        employee.setCode("EMP111111");
        employee.setTitle(Title.MR);
        employee.setName("Prasanna Karunarathna");
        employee.setCallingName("Prasanna");
        employee.setNic("801142122V");
        employee.setMobileOne("0714931346");
        employee.setMobileTwo("0760870052");
        employee.setLand("0332232699");
        employee.setOfficeEmail("prasannajg@gmail.com");
        employee.setGender(Gender.MALE);
        employee.setAddress("409/A, Colombo Rd, Bandiyamulle, Gampaha");
        employee.setDesignation(Designation.ADMIN);
        employee.setCivilStatus(CivilStatus.MARRIED);
        employee.setEmployeeStatus(EmployeeStatus.WORKING);
        employee.setDateOfBirth(LocalDate.now().minusYears(18));
        employee.setDateOfAssignment(LocalDate.now());
        Employee employeeDb = employeeService.persist(employee);


        //admin user one
        User user = new User();
        user.setEmployee(employeeDb);
        user.setUsername("admin");
        user.setPassword("admin");
        String message = "Username:- " + user.getUsername() + "\n Password:- " + user.getPassword();
        user.setEnabled(true);
        user.setRoles(roleService.findAll()
                .stream()
                .filter(role -> role.getRoleName().equals("ADMIN"))
                .collect(Collectors.toList()));
        userService.persist(user);

        return message;
    }


}
