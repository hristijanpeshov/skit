package com.hvt.booking_lux.intergrationTesting;

import com.hvt.booking_lux.model.*;
import com.hvt.booking_lux.model.enumeration.BedType;
import com.hvt.booking_lux.model.enumeration.Category;
import com.hvt.booking_lux.model.enumeration.Role;
import com.hvt.booking_lux.model.exceptions.UnitNotFoundException;
import com.hvt.booking_lux.security.CreatorCheck;
import com.hvt.booking_lux.service.ReservationObjectService;
import com.hvt.booking_lux.service.UnitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.util.ArrayList;
import java.util.List;

@AutoConfigureMockMvc
@SpringBootTest
public class UnitControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UnitService unitService;
    @MockBean
    private ReservationObjectService reservationObjectService;
    @MockBean
    private CreatorCheck creatorCheck;

    User user = new User("user@user.com", "pass", "User", "User", Role.ROLE_USER, "address", "000555888");
    User user1 = new User("user1@user.com", "pass1", "User1", "User1", Role.ROLE_USER, "address1", "111555888");

    Country macedonia = new Country("Macedonia", "MKD", "flag");
    Country serbia = new Country("Serbia", "SRB", "flag");
    Country england = new Country("England", "ENG", "flag");

    City skopje = new City("Skopje", macedonia);
    City belgrade = new City("Belgrade", serbia);
    City london = new City("London", england);

    ResObject skHotel = new ResObject("Hotel Makedonija", "Skopje 8/50","Beautiful Hotel In center of Skopje with view of the river Vardar.", Category.HOTEL, user, skopje);
    ResObject belHouse = new ResObject("House in Belgrade", "Belgradska ulica 11/11","Unwind at this stunning French Provencal beachside cottage. The house was lovingly built with stone floors, high-beamed ceilings, and antique details for a luxurious yet charming feel. Enjoy the sea and mountain views from the pool and lush garden.", Category.HOUSE, user, belgrade);
    ResObject lonApartment = new ResObject("Apartment for 2 people", "Manchesterska","Pretend you are lost in a magical forest as you perch on a log or curl up in the swinging chair. Soak in the tub, then fall asleep in a heavenly bedroom with cloud-painted walls and twinkling lights. And when you wake up, the espresso machine awaits.", Category.APARTMENT, user1, london);

    ResObject resObject = new ResObject("Name", "address", "description", Category.HOTEL, user, london);
    ResObject resObject1 = new ResObject("Name1", "address1", "description1", Category.APARTMENT, user, london);

    List<ResObject> resObjects = List.of(
            skHotel, belHouse, lonApartment
    );

    BedTypes bedTypes1 = new BedTypes(BedType.DOUBLE, 2);
    BedTypes bedTypes2 = new BedTypes(BedType.QUEEN, 2);
    BedTypes bedTypes3 = new BedTypes(BedType.DOUBLE, 1);

    List<Unit> units = List.of(
            new Unit(skHotel, "Room with pool view for 2", 22.0, 30.0, "Enjoy the elegance of a by-gone era while staying in this Art Deco home. Beautifully decorated and featuring a sweeping staircase, original stained-glass windows, period furniture, and a stunningly unique black-and-white tiled bathroom.", List.of(bedTypes3)),
            new Unit(skHotel, "Room with mountain view for 4", 50.0, 100.0, "Enjoy the elegance of a by-gone era while staying in this Art Deco home. Beautifully decorated and featuring a sweeping staircase, original stained-glass windows, period furniture, and a stunningly unique black-and-white tiled bathroom.", List.of(new BedTypes(BedType.DOUBLE, 2))),
            new Unit(skHotel, "Big apartment for 10 people", 100.0, 300.0, "Retreat to the deck of this sustainable getaway and gaze at the twinkling constellations under a cosy tartan blanket. AirShip 2 is an iconic, insulated aluminium pod designed by Roderick James with views of the Sound of Mull from dragonfly windows.", List.of(new BedTypes(BedType.DOUBLE, 1), new BedTypes(BedType.KING, 4))));


    @BeforeEach
    void setUp(){
        skHotel.setId(1L);
        units.get(0).setId(1L);
        units.get(0).setReservations(new ArrayList<>());
        units.get(0).setUnitImages(new ArrayList<>());
    }

    @Test
    public void getUnitTest() throws Exception {

        Mockito.when(unitService.findById(1))
                .thenReturn(units.get(0));
        Mockito.when(creatorCheck.check(eq(1L), any()))
                .thenReturn(false);

        mockMvc.perform(get("/accommodation/{resObjectId}/unit/{unitId}", 1, 1))
                .andExpect(status().isOk())
                .andExpect(view().name("master-template"))
                .andExpect(model().attribute("unit", is(units.get(0))))
                .andExpect(model().attribute("resObjectId", is(1L)))
                .andExpect(model().attribute("bodyContent", is("unitDetails")));
    }

    @Test
    public void getUnitTestWithError() throws Exception {

        Mockito.when(unitService.findById(1))
                .thenReturn(units.get(0));
        Mockito.when(creatorCheck.check(eq(1L), any()))
                .thenReturn(true);

        mockMvc.perform(get("/accommodation/{resObjectId}/unit/{unitId}", 1, 1).param("error", "There was an error."))
                .andExpect(status().isOk())
                .andExpect(view().name("master-template"))
                .andExpect(model().attribute("error", is("There was an error.")))
                .andExpect(model().attribute("unit", is(units.get(0))))
                .andExpect(model().attribute("resObjectId", is(1L)))
                .andExpect(model().attribute("bodyContent", is("unitDetails")));
    }

    @Test
    public void getUnitTestUnitIdNotFound() throws Exception {

        Mockito.when(unitService.findById(1L))
                .thenReturn(units.get(0));
        Mockito.when(unitService.findById(12L))
                .thenThrow(new UnitNotFoundException(12L));

        mockMvc.perform(get("/accommodation/{resObjectId}/unit/{unitId}", 1, 12).param("error", "There was an error."))
                .andExpect(status().isOk())
                .andExpect(view().name("master-template"))
                .andExpect(model().attribute("bodyContent", is("notFound")));
    }

    @Test
    public void getUnitFromReservationTest() throws Exception {

        Mockito.when(unitService.findByIdFromReservation(1L))
                .thenReturn(units.get(0));
        Mockito.when(unitService.findById(12L))
                .thenThrow(new UnitNotFoundException(12L));

        mockMvc.perform(get("/accommodation/{resObjectId}/unit/reservation/{unitId}", 1, 1))
                .andExpect(status().isOk())
                .andExpect(view().name("master-template"))
                .andExpect(model().attribute("unit", is(units.get(0))))
                .andExpect(model().attribute("resObjectId", is(1L)))
                .andExpect(model().attribute("bodyContent", is("unitDetails")));
    }

    @Test
    public void addFormTestNotAuthenticated() throws Exception {
        Mockito.when(unitService.findByIdFromReservation(1L))
                .thenReturn(units.get(0));

        mockMvc.perform(get("/accommodation/{resObjectId}/unit/add", 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/user/login"));
    }

    @Test
    public void addFormTest() throws Exception {
        Mockito.doReturn(true).when(creatorCheck).check(eq(1L), anyObject());
        Mockito.doReturn(units.get(0)).when(unitService).findByIdFromReservation(1L);
        Mockito.doReturn(skHotel).when(reservationObjectService).findResObjectById(1L);

        mockMvc.perform(get("/accommodation/{resObjectId}/unit/add", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("master-template"))
                .andExpect(model().attribute("edit", is(false)))
                .andExpect(model().attribute("resObjectId", is(1L)))
                .andExpect(model().attribute("bodyContent", is("add-unit")));

    }

    @Test
    public void addUnitTestNotAuthenticated() throws Exception {
        Mockito.when(unitService.findByIdFromReservation(1L))
                .thenReturn(units.get(0));

        mockMvc.perform(post("/accommodation/{resObjectId}/unit/add", 1)
                .param("resObjectId", String.valueOf(1L))
                .param("size", String.valueOf(25))
                .param("description", "Description")
                .param("price", String.valueOf(50.0))
                .param("title", "Title")
                .param("bedType", "DOUBLE")
                .param("count", "1")
                .param("images", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/user/login"));
    }

    @Test
    public void addUnitTest() throws Exception {
        Mockito.doReturn(true).when(creatorCheck).check(eq(1L), anyObject());
        Mockito.doReturn(units.get(0)).when(unitService).save(1L, "Title", 25, 50.0, "Description", List.of(BedType.DOUBLE), List.of(1), new ArrayList<>());
        Mockito.doReturn(skHotel).when(reservationObjectService).findResObjectById(1L);

        mockMvc.perform(post("/accommodation/{resObjectId}/unit/add", 1)
                .param("resObjectId", String.valueOf(1L))
                .param("size", String.valueOf(25))
                .param("description", "Description")
                .param("price", String.valueOf(50.0))
                .param("title", "Title")
                .param("bedType", "DOUBLE")
                .param("count", "1")
                .param("images", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/accommodation/1"));

    }

    @Test
    public void deleteTestNotAuthenticated() throws Exception {
        Mockito.doReturn(false).when(creatorCheck).check(eq(1L), any());

        mockMvc.perform(get("/accommodation/{resObjectId}/unit/delete/{unitId}", 1, 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/user/login"));
    }

    @Test
    public void deleteTest() throws Exception {
        Mockito.doReturn(true).when(creatorCheck).check(eq(1L), anyObject());
        Mockito.doReturn(units.get(0)).when(unitService).delete(1L);
        Mockito.doReturn(skHotel).when(reservationObjectService).findResObjectById(1L);

        mockMvc.perform(get("/accommodation/{resObjectId}/unit/delete/{unitId}", 1, 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/accommodation/1"));
    }

    @Test
    public void editFormTestNotAuthenticated() throws Exception {
        Mockito.doReturn(false).when(creatorCheck).check(eq(1L), any());

        mockMvc.perform(get("/accommodation/{resObjectId}/unit/edit/{unitId}", 1, 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/user/login"));
    }

    @Test
    public void editFormTest() throws Exception {
        Mockito.doReturn(true).when(creatorCheck).check(eq(1L), any());
        Mockito.when(reservationObjectService.findResObjectById(1L))
                .thenReturn(skHotel);
        Mockito.when(unitService.findById(1L))
                .thenReturn(units.get(0));

        mockMvc.perform(get("/accommodation/{resObjectId}/unit/edit/{unitId}", 1, 1))
                .andExpect(status().isOk())
                .andExpect(view().name("master-template"))
                .andExpect(model().attribute("edit", is(true)))
                .andExpect(model().attribute("unit", is(units.get(0))))
                .andExpect(model().attribute("resObjectId", is(1L)))
                .andExpect(model().attribute("bodyContent", is("add-unit")));
    }

    @Test
    public void editUnitTestNotAuthenticated() throws Exception {
        Mockito.doReturn(false).when(creatorCheck).check(eq(1L), any());

        mockMvc.perform(post("/accommodation/{resObjectId}/unit/edit/{unitId}", 1, 1)
                .param("title", "Title").param("resObjectId", String.valueOf(1L))
                .param("size", String.valueOf(25))
                .param("description", "Description")
                .param("price", String.valueOf(50.0))
                .param("bedType", "DOUBLE")
                .param("count", "1")
                .param("images", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/user/login"));
    }

    @Test
    public void editUnitTest() throws Exception {
        Mockito.doReturn(true).when(creatorCheck).check(eq(1L), any());
        Mockito.doReturn(units.get(0)).when(unitService).save(1L, "Title", 25, 50.0, "Description", List.of(BedType.DOUBLE), List.of(1), new ArrayList<>());
        Mockito.doReturn(skHotel).when(reservationObjectService).findResObjectById(1L);


        mockMvc.perform(post("/accommodation/{resObjectId}/unit/edit/{unitId}", 1, 1)
                .param("title", "Title").param("resObjectId", String.valueOf(1L))
                .param("size", String.valueOf(25))
                .param("description", "Description")
                .param("price", String.valueOf(50.0))
                .param("bedType", "DOUBLE")
                .param("count", "1")
                .param("images", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/accommodation/1/unit/1"));
    }

}
