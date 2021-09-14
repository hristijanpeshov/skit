package com.hvt.booking_lux.web.rest;

import com.hvt.booking_lux.model.ResObject;
import com.hvt.booking_lux.model.ResObjectDto;
import com.hvt.booking_lux.model.User;
import com.hvt.booking_lux.service.ReservationObjectService;
import com.hvt.booking_lux.service.ReservationService;
import com.hvt.booking_lux.service.UserService;
import com.hvt.booking_lux.service.UserStatisticsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsRestController {

    private final UserStatisticsService userStatisticsService;
    private final ReservationService reservationService;
    private final ReservationObjectService reservationObjectService;
    private final UserService userService;

    public StatisticsRestController(UserStatisticsService userStatisticsService, ReservationService reservationService, ReservationObjectService reservationObjectService, UserService userService) {
        this.userStatisticsService = userStatisticsService;
        this.reservationService = reservationService;
        this.reservationObjectService = reservationObjectService;
        this.userService = userService;
    }

    @GetMapping("/{id}/pie")
    @PreAuthorize("isAuthenticated() && @creatorCheck.check(#id,authentication)")
    public Map<String,Integer> sentimentAnalysis(@PathVariable Long id){
        return userStatisticsService.findSentimentForResObject(id);
//        return reservationService.lastYearIncomeForCreatorsAccommodations((User) authentication.getPrincipal());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated() && @creatorCheck.check(#id,authentication)")
    public List<ResObjectDto> sentimentAnalysis(@PathVariable String id){
        User user = (User) userService.loadUserByUsername(id);
        List<ResObjectDto> list = new ArrayList<>();
        reservationObjectService.listUserAccommodationListings(user).forEach(s-> list.add(new ResObjectDto(s)));
        return list;
//        return reservationService.lastYearIncomeForCreatorsAccommodations((User) authentication.getPrincipal());
    }

}
