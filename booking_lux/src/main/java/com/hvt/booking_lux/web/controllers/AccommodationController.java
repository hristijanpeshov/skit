package com.hvt.booking_lux.web.controllers;

import com.hvt.booking_lux.model.enumeration.Category;
import com.hvt.booking_lux.model.ResObject;
import com.hvt.booking_lux.model.User;
import com.hvt.booking_lux.model.exceptions.ResObjectNotFoundException;
import com.hvt.booking_lux.security.CreatorCheck;
import com.hvt.booking_lux.service.LocationService;
import com.hvt.booking_lux.service.ReservationObjectService;
import com.hvt.booking_lux.service.ReservationService;
import com.hvt.booking_lux.service.UnitService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Controller
@RequestMapping("/accommodation")
public class AccommodationController {

    private final ReservationObjectService reservationObjectService;
    private final CreatorCheck creatorCheck;
    private final UnitService unitService;
    private final ReservationService reservationService;
    private final LocationService locationService;

    public AccommodationController(ReservationObjectService reservationObjectService, CreatorCheck creatorCheck, UnitService unitService, ReservationService reservationService, LocationService locationService) {
        this.reservationObjectService = reservationObjectService;
        this.creatorCheck = creatorCheck;
        this.unitService = unitService;
        this.reservationService = reservationService;
        this.locationService = locationService;
    }


    @GetMapping
    public String listBySearchParams(HttpServletRequest request, @RequestParam(required = false) String city, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkInDate, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkOutDate, @RequestParam(required = false) Integer numPeople , Model model){
        List<ResObject> resObjectList = null;
        request.getSession().setAttribute("numPeople",numPeople);
        if(checkInDate != null && checkOutDate!=null)
        {
            ZonedDateTime checkIn = ZonedDateTime.of(checkInDate, LocalTime.parse("00:00"), ZoneId.systemDefault());
            ZonedDateTime checkOut = ZonedDateTime.of(checkOutDate, LocalTime.parse("00:00"), ZoneId.systemDefault());
            request.getSession().setAttribute("checkIn",checkIn);
            request.getSession().setAttribute("checkOut",checkOut);
        }
        if(city!=null && !city.equals("") && checkInDate != null && checkOutDate!=null && numPeople!=null)
        {
            ZonedDateTime checkIn = ZonedDateTime.of(checkInDate, LocalTime.parse("00:00"), ZoneId.systemDefault());
            ZonedDateTime checkOut = ZonedDateTime.of(checkOutDate, LocalTime.parse("00:00"), ZoneId.systemDefault());
            request.getSession().setAttribute("cityName",city);
//            resObjectList = reservationObjectService.listByCityName(city);
            if(checkOut.isBefore(checkIn)){
                return "redirect:/home?error=Check in date should be before check out date!";
            }
//            reservationObjectService.listAllAvailableUnitsForResObject(2, checkIn, checkOut, numPeople);
            resObjectList = reservationObjectService.findAllAvailable(checkIn, checkOut, numPeople, city);
        }else if(city!=null && !city.equals("") && numPeople!=null)
        {
            resObjectList = reservationObjectService.listAll();
        } else if(city!=null && !city.equals(""))
        {
            resObjectList = reservationObjectService.listByCityName(city);
        }
        else{
            resObjectList = reservationObjectService.listAll();
        }
        model.addAttribute("searchForm","searchForm");
        model.addAttribute("reservationObjects", resObjectList);
        model.addAttribute("bodyContent", "rooms");
        return "master-template";
    }

    @GetMapping("/myListings")
    @PreAuthorize("isAuthenticated()")
    public String getMyListings(Authentication authentication,Model model)
    {
        List<ResObject>  resObjects = reservationObjectService.listUserAccommodationListings((User)authentication.getPrincipal());
        model.addAttribute("resObjects",resObjects);
        model.addAttribute("bodyContent","myListings");
        return "master-template";
    }

    @GetMapping("/{resObjectId}")
    public String getSpecificAccommodation(@PathVariable long resObjectId, Model model, HttpServletRequest request)
    {
        ResObject resObject;
        try {
            resObject = reservationObjectService.findResObjectById(resObjectId);
        }
        catch (ResObjectNotFoundException ex){
            model.addAttribute("bodyContent","notFound");
            return "master-template";
        }
        model.addAttribute("resObject", resObject);
        Integer numPeople = (Integer) request.getSession().getAttribute("numPeople");
        ZonedDateTime fromDate = (ZonedDateTime) request.getSession().getAttribute("checkIn");
        ZonedDateTime toDate = (ZonedDateTime) request.getSession().getAttribute("checkOut");
        if(numPeople!=null && fromDate!=null && toDate!=null)
        {

            model.addAttribute("units", reservationObjectService.listAllAvailableUnitsForResObject(resObjectId, fromDate, toDate, numPeople));
        }
        else if(numPeople!=null)
        {
            model.addAttribute("units",unitService.listAllMoreThan(resObjectId,numPeople));
        }
        else
        {
            model.addAttribute("units",unitService.listAll(resObjectId));
        }
        model.addAttribute("bodyContent", "AccommodationDetails");
        return "master-template";
    }

    @GetMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String addForm(Model model){
        model.addAttribute("bodyContent", "add-accommodation");
        model.addAttribute("edit",false);
        model.addAttribute("cities",locationService.listAllCities());
        return "master-template";
    }

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String save(Model model,Authentication authentication, @RequestParam String name, @RequestParam String address, @RequestParam String description, @RequestParam Category category, @RequestParam long cityId, @RequestParam List<String> images)
    {
        reservationObjectService.save(name, address, description, category, (User) authentication.getPrincipal(), cityId, images);
        return "redirect:/accommodation";
    }

    @GetMapping("/edit/{resObjectId}")
    @PreAuthorize("@creatorCheck.check(#resObjectId,authentication)")
    public String edit(Authentication authentication,@PathVariable long resObjectId, Model model)
    {
        ResObject resObject = reservationObjectService.findResObjectById(resObjectId);
        model.addAttribute("reservationObject",resObject);
        model.addAttribute("edit",true);
        model.addAttribute("bodyContent","add-accommodation");
        return "master-template";
    }

    @PostMapping("/edit/{resObjectId}")
    @PreAuthorize("@creatorCheck.check(#resObjectId,authentication)")
    public String edit(Model model,Authentication authentication,@PathVariable long resObjectId, @RequestParam String name, @RequestParam String address, @RequestParam String description, @RequestParam Category category, @RequestParam List<String> images)
    {
        ResObject resObject = reservationObjectService.findResObjectById(resObjectId);
        reservationObjectService.edit(resObjectId,name,address,description,category,images);
        return "redirect:/accommodation/"+resObjectId;
    }

    @PostMapping("/delete/{resObjectId}")
    @PreAuthorize("@creatorCheck.check(#resObjectId,authentication)")
    public String delete(@PathVariable long resObjectId)
    {
        reservationObjectService.delete(resObjectId);
        return "redirect:/accommodation/myListings";
    }
}
