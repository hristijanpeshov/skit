package com.hvt.booking_lux.intergrationTesting;

import com.hvt.booking_lux.intergrationTesting.customAuthentication.WithMockCustomUser;
import com.hvt.booking_lux.model.*;
import com.hvt.booking_lux.model.enumeration.Category;
import com.hvt.booking_lux.model.enumeration.Role;
import com.hvt.booking_lux.model.exceptions.UnitIsReservedException;
import com.hvt.booking_lux.service.ReservationService;
import com.hvt.booking_lux.service.ReviewService;
import com.hvt.booking_lux.service.UnitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("h2")
@AutoConfigureMockMvc
public class ReservationControllerTests {

    @Autowired
    public MockMvc mockmvc;
    @MockBean
    public ReservationService reservationService;
    @MockBean
    public UnitService unitService;
    @MockBean
    public ReviewService reviewService;

    private List<ResObject> resObjectList;
    private List sk;
    private ResObject resObject;
    private List<Unit> unitList;
    private User user;
    private List<City> cities;
    private List<Reservation> reservations;

    @BeforeEach
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        Country country1 = new Country("MKD","MKD","Image");
        City city1 = new City("Skopje",country1);
        Country country2 = new Country("SRB","SRB","Image");
        City city2 = new City("Belgrade",country2);
        Country country3 = new Country("BG","BG","Image");
        City city3 = new City("Sofia",country3);
        cities = new ArrayList<City>();
        cities.add(city1);
        cities.add(city2);
        cities.add(city3);
        resObjectList = new ArrayList<>();
        user = new User("user","user","user","user", Role.ROLE_USER,"add","phone");
        //user = userService.register("user","user","user","user","user");
        ResObject resObject1 = new ResObject("Name1","Address1","Desc1", Category.HOTEL,user,city1);
        resObjectList.add(resObject1);
        List<String> images=new ArrayList<>();
        images.add("xD");
        resObject1.setObjectImages(images);
        resObject = resObject1;
        Field field = ResObject.class.getDeclaredField("id");
        field.setAccessible(true);
        field.set(resObject1,1L);
        List<Unit> units = new ArrayList<>();
        Unit unit1 = new Unit(resObject1,"t",123,123,"Desc");
        Unit unit2 = new Unit(resObject1,"t",123,123,"Desc");
        Unit unit3 = new Unit(resObject1,"t",123,123,"Desc");
        Field field3 = Unit.class.getDeclaredField("id");
        field3.setAccessible(true);
        field3.set(unit2,2L);
        units.add(unit1);
        units.add(unit2);
        units.add(unit3);
        unitList = units;
        ResObject resObject2 = new ResObject("Name2","Address2","Desc2",Category.APARTMENT,user,city2);
        resObjectList.add(resObject2);
        resObject2.setObjectImages(images);
        ResObject resObject3 = new ResObject("Name3","Address3","Desc3",Category.HOUSE,user,city3);
        resObjectList.add(resObject3);
        resObject3.setObjectImages(images);
        sk = new ArrayList<ResObject>();
        sk.add(resObject1);
        Reservation res = new Reservation(user,unit1,123,10,ZonedDateTime.of(LocalDate.parse("2020-12-03"), LocalTime.parse("00:00"), ZoneId.systemDefault()),ZonedDateTime.of(LocalDate.parse("2020-12-13"), LocalTime.parse("00:00"), ZoneId.systemDefault()));
        Field field1 = Reservation.class.getDeclaredField("id");
        field1.setAccessible(true);
        field1.set(res,1L);
        reservations = new ArrayList<>();
        reservations.add(res);
        Mockito.when(reservationService.findAllReservationsForUser(Mockito.any(User.class))).thenReturn(reservations);
        Mockito.when(reviewService.canUserWriteReview(Mockito.anyLong(),Mockito.anyString())).thenReturn(true);
        Mockito.when(reviewService.alreadyWrite(Mockito.anyLong(),Mockito.anyString())).thenReturn(false);
        Mockito.when(unitService.findById(eq(1L))).thenReturn(unit1);
        Mockito.when(reservationService.reserve(Mockito.any(User.class),eq(1L),Mockito.anyInt(),Mockito.any(ZonedDateTime.class),Mockito.any(ZonedDateTime.class))).thenReturn(res);
        Mockito.when(reservationService.reserve(Mockito.any(User.class),eq(2L),Mockito.anyInt(),Mockito.any(ZonedDateTime.class),Mockito.any(ZonedDateTime.class))).thenThrow(new UnitIsReservedException(2));
        Mockito.when(unitService.findById(eq(2l))).thenReturn(unit2);
    }

    @Test
    @WithMockCustomUser
    public void reserveUnitNoDatesTest() throws Exception {
        mockmvc.perform(get("/reserve/{unitId}",1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/accommodation/1/unit/1"));
    }
    @Test
    @WithMockCustomUser
    public void reserveUnitWithDatesTest() throws Exception {
        mockmvc.perform(get("/reserve/{unitId}",1)
                .param("checkInDate","2020-12-03")
                .param("checkOutDate","2020-12-13"))
                .andExpect(status().isOk())
                .andExpect(request().sessionAttribute("checkIn", ZonedDateTime.of(LocalDate.parse("2020-12-03"), LocalTime.parse("00:00"), ZoneId.systemDefault())))
                .andExpect(request().sessionAttribute("checkOut", ZonedDateTime.of(LocalDate.parse("2020-12-13"), LocalTime.parse("00:00"), ZoneId.systemDefault())))
                .andExpect(model().attribute("unit",unitList.get(0)))
                .andExpect(model().attribute("user",user))
                .andExpect(model().attribute("numNights",10))
                .andExpect(model().attribute("bodyContent","confirmReservation"))
                .andExpect(view().name("master-template"));
    }
    @Test
    @WithMockCustomUser
    public void reserveUnitWithSessionDatesTest() throws Exception {
        mockmvc.perform(get("/reserve/{unitId}",1)
                .sessionAttr("checkIn", ZonedDateTime.of(LocalDate.parse("2020-12-03"), LocalTime.parse("00:00"), ZoneId.systemDefault()))
                .sessionAttr("checkOut", ZonedDateTime.of(LocalDate.parse("2020-12-13"), LocalTime.parse("00:00"), ZoneId.systemDefault())))
                .andExpect(status().isOk())
                .andExpect(request().sessionAttribute("checkIn", ZonedDateTime.of(LocalDate.parse("2020-12-03"), LocalTime.parse("00:00"), ZoneId.systemDefault())))
                .andExpect(request().sessionAttribute("checkOut", ZonedDateTime.of(LocalDate.parse("2020-12-13"), LocalTime.parse("00:00"), ZoneId.systemDefault())))
                .andExpect(model().attribute("unit",unitList.get(0)))
                .andExpect(model().attribute("user",user))
                .andExpect(model().attribute("numNights",10))
                .andExpect(model().attribute("bodyContent","confirmReservation"))
                .andExpect(view().name("master-template"));
    }

    @Test
    @WithMockCustomUser
    public void confirmWithPdfReserveTest() throws Exception {
        mockmvc.perform(post("/reserve/{unitId}",1)
                .param("pdfRedir","true")
                .sessionAttr("checkIn", ZonedDateTime.of(LocalDate.parse("2020-12-03"), LocalTime.parse("00:00"), ZoneId.systemDefault()))
                .sessionAttr("checkOut", ZonedDateTime.of(LocalDate.parse("2020-12-13"), LocalTime.parse("00:00"), ZoneId.systemDefault())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/1/pdf"));
    }
    @Test
    @WithMockCustomUser
    public void confirmWithoutPdfReserveTest() throws Exception {
        mockmvc.perform(post("/reserve/{unitId}",1)
                .param("pdfRedir","false")
                .sessionAttr("checkIn", ZonedDateTime.of(LocalDate.parse("2020-12-03"), LocalTime.parse("00:00"), ZoneId.systemDefault()))
                .sessionAttr("checkOut", ZonedDateTime.of(LocalDate.parse("2020-12-13"), LocalTime.parse("00:00"), ZoneId.systemDefault())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reserve/myReservations"));
    }
    @Test
    @WithMockCustomUser
    public void unitAlreadyReservedTest() throws Exception {
        mockmvc.perform(post("/reserve/{unitId}",2)
                .param("pdfRedir","false")
                .sessionAttr("checkIn", ZonedDateTime.of(LocalDate.parse("2020-12-03"), LocalTime.parse("00:00"), ZoneId.systemDefault()))
                .sessionAttr("checkOut", ZonedDateTime.of(LocalDate.parse("2020-12-13"), LocalTime.parse("00:00"), ZoneId.systemDefault())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/accommodation/1/unit/2?error=This unit is reserved for these dates"));
    }

    @Test
    public void cancelReservationTest() throws Exception {
        mockmvc.perform(post("/reserve/cancel/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reserve/myReservations"));
    }
    @Test
    @WithMockCustomUser
    public void myReservationsTest() throws Exception {
        List<Boolean> list = new ArrayList<>();
        list.add(true);
        mockmvc.perform(get("/reserve/myReservations"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("reservations",reservations))
                .andExpect(model().attribute("canWriteList",list))
                .andExpect(model().attribute("bodyContent","myReservations"))
                .andExpect(view().name("master-template"));
    }
}
