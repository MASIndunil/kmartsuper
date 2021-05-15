package lk.kmart_super.asset.common_asset.controller;


import lk.kmart_super.asset.user_management.role.entity.Role;
import lk.kmart_super.asset.user_management.user.entity.User;
import lk.kmart_super.asset.user_management.user.service.UserService;
import lk.kmart_super.util.service.DateTimeAgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UiController {

    private final UserService userService;
    private final DateTimeAgeService dateTimeAgeService;

    @Autowired
    public UiController(UserService userService, DateTimeAgeService dateTimeAgeService) {
        this.userService = userService;
        this.dateTimeAgeService = dateTimeAgeService;
    }

    @GetMapping(value = {"/", "/index"})
    public String index() {
        return "index";
    }

    @GetMapping(value = {"/home", "/mainWindow"})
    public String getHome() {
       return "/mainWindow";
    }

    @GetMapping(value = {"/login"})
    public String getLogin() {
        return "login/login";
    }

    @GetMapping(value = {"/login/error10"})
    public String getLogin10(Model model) {
        model.addAttribute("err", "You already entered wrong credential more than 10 times. \n Please meet the system" +
                " admin");
        return "login/login";
    }

    @GetMapping(value = {"/login/noUser"})
    public String getLoginNoUser(Model model) {
        model.addAttribute("err", "There is no user according to the user name. \n Please try again !!");
        return "login/login";
    }




}
