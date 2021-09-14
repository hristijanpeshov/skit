package com.hvt.booking_lux.intergrationTesting;

import com.hvt.booking_lux.intergrationTesting.customAuthentication.WithMockCustomUser;
import com.hvt.booking_lux.model.*;
import com.hvt.booking_lux.model.enumeration.BedType;
import com.hvt.booking_lux.model.enumeration.Category;
import com.hvt.booking_lux.model.enumeration.Role;
import com.hvt.booking_lux.service.ReservationService;
import com.hvt.booking_lux.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@AutoConfigureMockMvc
@SpringBootTest
public class ReviewControllerTests {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;
    @MockBean
    private ReservationService reservationService;
    @MockBean
    private RestTemplate restTemplate;

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


    Reservation reservation1 = new Reservation(user1, units.get(0), units.get(0).getPrice(), 2,
            ZonedDateTime.parse("2021-03-23T00:00:00+02:00"), ZonedDateTime.parse("2021-03-24T00:00:00+02:00"));
    Reservation reservation2 = new Reservation(user, units.get(1), units.get(1).getPrice(), 6,
            ZonedDateTime.parse("2021-03-25T00:00:00+02:00"), ZonedDateTime.parse("2021-03-31T00:00:00+02:00"));
    Reservation reservation3 = new Reservation(user, units.get(2), units.get(2).getPrice(), 1,
            ZonedDateTime.parse("2021-07-23T00:00:00+02:00"), ZonedDateTime.parse("2021-07-24T00:00:00+02:00"));

    Review review = new Review(reservation1, "comment", user);
    Review review1 = new Review(reservation2, "comment", user1);
    Review review2 = new Review(reservation3, "comment", user1);

    @BeforeEach
    void setUp(){
        skHotel.setId(1L);
        units.get(0).setId(1L);
        units.get(0).setReservations(new ArrayList<>());
        units.get(0).setUnitImages(new ArrayList<>());

        reservation1.setReview(review);
    }


    @Test
    void reviewPageTest() throws Exception {
        mockMvc.perform(get("/review/{reservationId}", 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/user/login"));
    }

    @Test
    @WithMockCustomUser(username="user")
    void reviewPageUserCanNotReviewTest() throws Exception {
        Mockito.when(reviewService.canUserWriteReview(1L, "user"))
                .thenReturn(false);

        mockMvc.perform(get("/review/{reservationId}", 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reserve/myReservations"));
    }

    @Test
    @WithMockCustomUser(username="user")
    void reviewPageUserCanReviewTest() throws Exception {
        Mockito.when(reviewService.canUserWriteReview(1L, "user"))
                .thenReturn(true);
        Mockito.when(reviewService.alreadyWrite(1L, "user"))
                .thenReturn(true);
        Mockito.when(reservationService.findReservationById(1L))
                .thenReturn(reservation1);

        mockMvc.perform(get("/review/{reservationId}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("master-template"))
                .andExpect(model().attribute("reservationId", is(1L)))
                .andExpect(model().attribute("edit", is(true)))
                .andExpect(model().attribute("review", is(review)))
                .andExpect(model().attribute("bodyContent", is("reviewPage")));
    }

    @Test
    @WithMockCustomUser(username="user")
    void reviewPageUserCanReviewTestNotWrittenYet() throws Exception {
        Mockito.when(reviewService.canUserWriteReview(1L, "user"))
                .thenReturn(true);
        Mockito.when(reviewService.alreadyWrite(1L, "user"))
                .thenReturn(false);

        mockMvc.perform(get("/review/{reservationId}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("master-template"))
                .andExpect(model().attribute("reservationId", is(1L)))
                .andExpect(model().attribute("edit", is(false)))
                .andExpect(model().attribute("bodyContent", is("reviewPage")));
    }


    @Test
    void addCommentTest() throws Exception {
        mockMvc.perform(post("/review/{reservationId}/add", 1)
                .queryParam("comment", "New Comment"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/user/login"));
    }

    @Test
    @WithMockCustomUser(username="user")
    void addCommentWithLoggedInUserTest() throws Exception {
        Mockito.when(restTemplate.postForEntity(Mockito.eq("http://localhost:9090/review"), Mockito.any(), Mockito.any()))
                .thenReturn(ResponseEntity.of(Optional.of("true")));
        Mockito.when(reviewService.saveReview("user", "New Comment", 1L, true))
                .thenReturn(new Review(reservation1, "New Comment", user));


        mockMvc.perform(post("/review/{reservationId}/add", 1)
                .queryParam("comment", "New Comment"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reserve/myReservations"));
    }

    @Test
    void editCommentTestUserNotLoggedIn() throws Exception {
        mockMvc.perform(post("/review/edit/{reviewId}", 1)
                .queryParam("comment", "New Comment"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/user/login"));
    }

    @Test
    @WithMockCustomUser(username="user")
    void editCommentTest() throws Exception {
        Mockito.when(reviewService.editReview("New Comment", 1L))
                .thenReturn(new Review(reservation1, "New Comment", user));


        mockMvc.perform(post("/review/edit/{reviewId}", 1)
                .queryParam("comment", "New Comment"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reserve/myReservations"));
    }


    @Test
    void showPercentageTest() throws Exception {
        mockMvc.perform(get("/review", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("master-template"))
                .andExpect(model().attribute("positive", is(100)))
                .andExpect(model().attribute("negative", is(50)))
                .andExpect(model().attribute("bodyContent", is("statistics")));
    }
}
