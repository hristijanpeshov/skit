package com.hvt.booking_lux.intergrationTesting;

import com.hvt.booking_lux.intergrationTesting.customAuthentication.WithMockCustomUser;
import com.hvt.booking_lux.model.*;
import com.hvt.booking_lux.model.enumeration.Category;
import com.hvt.booking_lux.security.CreatorCheck;
import com.hvt.booking_lux.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("h2")
@AutoConfigureMockMvc()
public class AccommodationControllerTests {
    @Autowired
    public MockMvc mockMvc;
    @MockBean
    public ReservationObjectService reservationObjectService;
    @MockBean(name = "creatorCheck")
    public CreatorCheck creatorCheck;
    @MockBean
    public UnitService unitService;
    @MockBean
    public ReservationService reservationService;
    @MockBean
    public LocationService locationService;
    @MockBean
    public UserService userService;
    private List<ResObject> resObjectList;
    private List sk;
    private ResObject resObject;
    private List<Unit> unitList;
    private User user;
    private List<City> cities;

    @BeforeEach
    public void setup() throws IllegalAccessException, NoSuchFieldException {
        Country country1 = new Country("MKD", "MKD", "Image");
        City city1 = new City("Skopje", country1);
        Country country2 = new Country("SRB", "SRB", "Image");
        City city2 = new City("Belgrade", country2);
        Country country3 = new Country("BG", "BG", "Image");
        City city3 = new City("Sofia", country3);
        cities = new ArrayList<City>();
        cities.add(city1);
        cities.add(city2);
        cities.add(city3);
        resObjectList = new ArrayList<>();
        user = userService.register("user", "user", "user", "user", "user");
        ResObject resObject1 = new ResObject("Name1", "Address1", "Desc1", Category.HOTEL, user, city1);
        resObjectList.add(resObject1);
        List<String> images = new ArrayList<>();
        images.add("xD");
        resObject1.setObjectImages(images);
        resObject = resObject1;
        Field field = ResObject.class.getDeclaredField("id");
        field.setAccessible(true);
        field.set(resObject1, 1L);
        List<Unit> units = new ArrayList<>();
        Unit unit1 = new Unit(resObject1, "t", 123, 123, "Desc");
        Unit unit2 = new Unit(resObject1, "t", 123, 123, "Desc");
        Unit unit3 = new Unit(resObject1, "t", 123, 123, "Desc");
        units.add(unit1);
        units.add(unit2);
        units.add(unit3);
        unitList = units;
        ResObject resObject2 = new ResObject("Name2", "Address2", "Desc2", Category.APARTMENT, user, city2);
        resObjectList.add(resObject2);
        resObject2.setObjectImages(images);
        ResObject resObject3 = new ResObject("Name3", "Address3", "Desc3", Category.HOUSE, user, city3);
        resObjectList.add(resObject3);
        resObject3.setObjectImages(images);
        Mockito.when(reservationObjectService.listAll()).thenReturn(resObjectList);
        sk = new ArrayList<ResObject>();
        sk.add(resObject1);
        Mockito.doReturn(true).when(creatorCheck).check(Mockito.anyLong(), Mockito.any(Authentication.class));
        Mockito.when(reservationObjectService.listByCityName("Skopje")).thenReturn(sk);
        Mockito.when(reservationObjectService.findAllAvailable(Mockito.any(ZonedDateTime.class), Mockito.any(ZonedDateTime.class), Mockito.anyInt(), eq("Skopje"))).thenReturn(sk);
        Mockito.when(reservationObjectService.listUserAccommodationListings(Mockito.any(User.class))).thenReturn(resObjectList);
        Mockito.when(reservationObjectService.findResObjectById(Mockito.anyLong())).thenReturn(resObject1);
        Mockito.when(reservationObjectService.listAllAvailableUnitsForResObject(Mockito.anyLong(), Mockito.any(ZonedDateTime.class), Mockito.any(ZonedDateTime.class), Mockito.anyInt())).thenReturn(unitList);
        Mockito.when(unitService.listAllMoreThan(Mockito.anyLong(), Mockito.anyInt())).thenReturn(unitList);
        Mockito.when(unitService.listAll(Mockito.anyLong())).thenReturn(unitList);
        Mockito.when(reservationObjectService.save(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any(Category.class), Mockito.any(User.class), Mockito.anyLong(), Mockito.any(List.class))).thenReturn(resObject1);
        Mockito.when(locationService.listAllCities()).thenReturn(cities);
    }

    @Test
    public void listResObjectsTest() throws Exception {
        mockMvc.perform(get("/accommodation"))
                .andExpect(status().isOk())
                .andExpect(view().name("master-template"))
                .andExpect(model().attribute("searchForm", "searchForm"))
                .andExpect(model().attribute("reservationObjects", resObjectList))
                .andExpect(model().attribute("bodyContent", "rooms"));
    }

    @Test
    public void listResObjectsSkopjeTest() throws Exception {
        mockMvc.perform(get("/accommodation")
                .param("city", "Skopje"))
                .andExpect(status().isOk())
                .andExpect(view().name("master-template"))
                .andExpect(model().attribute("searchForm", "searchForm"))
                .andExpect(model().attribute("reservationObjects", sk))
                .andExpect(model().attribute("bodyContent", "rooms"));
    }

    @Test
    public void listResObjectsSkopjeWithPeopleNumberTest() throws Exception {
        mockMvc.perform(get("/accommodation")
                .param("city", "Skopje")
                .param("numPeople", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("master-template"))
                .andExpect(model().attribute("searchForm", "searchForm"))
                .andExpect(model().attribute("reservationObjects", resObjectList))
                .andExpect(model().attribute("bodyContent", "rooms"))
                .andExpect(request().sessionAttribute("numPeople", Integer.valueOf(1)));
    }

    @Test
    public void listResObjectsAllArgumentsSuccessTest() throws Exception {
        mockMvc.perform(get("/accommodation")
                .param("city", "Skopje")
                .param("numPeople", "1")
                .param("checkInDate", "2020-12-03")
                .param("checkOutDate", "2020-12-13"))
                .andExpect(status().isOk())
                .andExpect(view().name("master-template"))
                .andExpect(model().attribute("searchForm", "searchForm"))
                .andExpect(model().attribute("reservationObjects", sk))
                .andExpect(model().attribute("bodyContent", "rooms"))
                .andExpect(request().sessionAttribute("numPeople", Integer.valueOf(1)))
                .andExpect(request().sessionAttribute("cityName", "Skopje"))
                .andExpect(request().sessionAttribute("checkIn", ZonedDateTime.of(LocalDate.parse("2020-12-03"), LocalTime.parse("00:00"), ZoneId.systemDefault())))
                .andExpect(request().sessionAttribute("checkOut", ZonedDateTime.of(LocalDate.parse("2020-12-13"), LocalTime.parse("00:00"), ZoneId.systemDefault())));
    }

    @Test
    public void listResObjectsAllArgumentsFailureTest() throws Exception {
        mockMvc.perform(get("/accommodation")
                .param("city", "Skopje")
                .param("numPeople", "1")
                .param("checkInDate", "2020-12-13")
                .param("checkOutDate", "2020-12-03"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home?error=Check in date should be before check out date!"))
                .andExpect(request().sessionAttribute("numPeople", Integer.valueOf(1)))
                .andExpect(request().sessionAttribute("cityName", "Skopje"))
                .andExpect(request().sessionAttribute("checkIn", ZonedDateTime.of(LocalDate.parse("2020-12-13"), LocalTime.parse("00:00"), ZoneId.systemDefault())))
                .andExpect(request().sessionAttribute("checkOut", ZonedDateTime.of(LocalDate.parse("2020-12-03"), LocalTime.parse("00:00"), ZoneId.systemDefault())));
    }

    @Test
    @WithMockCustomUser
    public void listAllUserAccommodations() throws Exception {

        mockMvc.perform(get("/accommodation/myListings"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("resObjects", resObjectList))
                .andExpect(model().attribute("bodyContent", "myListings"));
    }

    @Test
    public void getSpecificAccommodationAllAttributesPresentTest() throws Exception {
        HashMap<String, Object> sessionAttrs = new HashMap<String, Object>();
        sessionAttrs.put("numPeople", 1);
        sessionAttrs.put("checkIn", ZonedDateTime.of(LocalDate.parse("2020-12-03"), LocalTime.parse("00:00"), ZoneId.systemDefault()));
        sessionAttrs.put("checkOut", ZonedDateTime.of(LocalDate.parse("2020-12-13"), LocalTime.parse("00:00"), ZoneId.systemDefault()));
        mockMvc.perform(get("/accommodation/{resObjectId}", 1).sessionAttrs(sessionAttrs))
                .andExpect(status().isOk())
                .andExpect(view().name("master-template"))
                .andExpect(model().attribute("resObject", resObject))
                .andExpect(model().attribute("units", unitList))
                .andExpect(model().attribute("bodyContent", "AccommodationDetails"));

    }

    @Test
    public void getSpecificAccommodationOnlyNumberPeopleTest() throws Exception {
        HashMap<String, Object> sessionAttrs = new HashMap<String, Object>();
        sessionAttrs.put("numPeople", 1);
        mockMvc.perform(get("/accommodation/{resObjectId}", 1).sessionAttrs(sessionAttrs))
                .andExpect(status().isOk())
                .andExpect(view().name("master-template"))
                .andExpect(model().attribute("resObject", resObject))
                .andExpect(model().attribute("units", unitList))
                .andExpect(model().attribute("bodyContent", "AccommodationDetails"));

    }

    @Test
    public void getSpecificAccommodationNoAttributesPresentTest() throws Exception {
        mockMvc.perform(get("/accommodation/{resObjectId}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("master-template"))
                .andExpect(model().attribute("resObject", resObject))
                .andExpect(model().attribute("units", unitList))
                .andExpect(model().attribute("bodyContent", "AccommodationDetails"));

    }

    @Test
    @WithMockCustomUser
    public void addAccommodationFormTest() throws Exception {

        mockMvc.perform(get("/accommodation/add"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("edit", false))
                .andExpect(model().attribute("bodyContent", "add-accommodation"))
                .andExpect(model().attribute("cities", cities))
                .andExpect(view().name("master-template"));
    }

    @Test
    @WithMockCustomUser
    public void saveAccommodationTest() throws Exception {

        mockMvc.perform(post("/accommodation/add")
                .param("name", "name")
                .param("address", "address")
                .param("description", "description")
                .param("category", Category.APARTMENT.toString())
                .param("cityId", "1")
                .param("images", "image1", "image2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/accommodation"));
    }

    @Test
    public void editAccommodationFormTest() throws Exception {
        mockMvc.perform(get("/accommodation/edit/{resObjectId}", 1))
                .andExpect(status().isOk())
                .andExpect(model().attribute("reservationObject", resObjectList.get(0)))
                .andExpect(model().attribute("edit", true))
                .andExpect(model().attribute("bodyContent", "add-accommodation"))
                .andExpect(view().name("master-template"));
    }

    @Test
    public void editAccommodationTest() throws Exception {
        mockMvc.perform(post("/accommodation/edit/{resObjectId}", 1)
                .param("name", "name")
                .param("address", "address")
                .param("description", "description")
                .param("category", Category.APARTMENT.toString())
                .param("images", "image1", "image2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/accommodation/1"));
    }

    @Test
    public void deleteAccommodationTest() throws Exception {
        mockMvc.perform(post("/accommodation/delete/{resObjectId}", 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/accommodation/myListings"));
    }


}
