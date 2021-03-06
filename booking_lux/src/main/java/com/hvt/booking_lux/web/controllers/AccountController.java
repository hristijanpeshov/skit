package com.hvt.booking_lux.web.controllers;

import com.hvt.booking_lux.model.User;
import com.hvt.booking_lux.service.ReservationObjectService;
import com.hvt.booking_lux.service.ReservationService;
import com.hvt.booking_lux.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/user")
public class AccountController {

    private final UserService userService;
    private final ReservationObjectService reservationObjectService;

    public AccountController(UserService userService,ReservationObjectService reservationObjectService) {
        this.userService = userService;
        this.reservationObjectService = reservationObjectService;
    }

    @GetMapping("/manage")
    @PreAuthorize("isAuthenticated()")
    public String accountPage(Authentication authentication,Model model, @RequestParam(required = false) String message){
        model.addAttribute("user",(User)authentication.getPrincipal());
        model.addAttribute("message",message);
        model.addAttribute("resObjects", reservationObjectService.listUserAccommodationListings((User) authentication.getPrincipal()));
        model.addAttribute("bodyContent", "manage-account");
        return "master-template";
    }

    @PostMapping("/manage")
    @PreAuthorize("isAuthenticated()")
    public String editAccount(Authentication authentication, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String email){
        User user = userService.edit(authentication.getName(),firstName,lastName,email);
        Authentication auth = new PreAuthenticatedAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        return "redirect:/user/manage";
    }
    @GetMapping("/login")
    public String getLoginPage(Model model,@RequestParam(required = false) String badLogin){
        model.addAttribute("bodyContent", "login");
        model.addAttribute("message",badLogin);
        return "master-template";
    }
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAdminPage(Model model, Authentication authentication){
        model.addAttribute("resObjects", reservationObjectService.listAll());
        model.addAttribute("bodyContent", "adminPage");
        return "master-template";
    }

}
