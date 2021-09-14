package com.hvt.booking_lux.unitTesting;

import com.hvt.booking_lux.model.*;
import com.hvt.booking_lux.model.enumeration.BedType;
import com.hvt.booking_lux.model.enumeration.Category;
import com.hvt.booking_lux.model.enumeration.Role;
import com.hvt.booking_lux.model.enumeration.Sentiment;
import com.hvt.booking_lux.model.exceptions.ReservationNotFoundException;
import com.hvt.booking_lux.model.exceptions.ReviewNotFoundException;
import com.hvt.booking_lux.model.exceptions.UserNotCreatorOfReservationException;
import com.hvt.booking_lux.repository.ReviewRepository;
import com.hvt.booking_lux.service.ReservationService;
import com.hvt.booking_lux.service.UnitService;
import com.hvt.booking_lux.service.UserService;
import com.hvt.booking_lux.service.impl.ReviewServiceImpl;
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.times;

@SpringBootTest
public class ReviewServiceTests {
    @Mock
    ReviewRepository reviewRepository;
    @Mock
    UserService userService;
    @Mock
    UnitService unitService;
    @Mock
    ReservationService reservationService;


    @InjectMocks
    ReviewServiceImpl reviewService;

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

    List<Unit> units = List.of(
            new Unit(skHotel, "Room with pool view for 2", 22.0, 30.0, "Enjoy the elegance of a by-gone era while staying in this Art Deco home. Beautifully decorated and featuring a sweeping staircase, original stained-glass windows, period furniture, and a stunningly unique black-and-white tiled bathroom.", List.of(new BedTypes(BedType.DOUBLE, 1))),
            new Unit(skHotel, "Room with mountain view for 4", 50.0, 100.0, "Enjoy the elegance of a by-gone era while staying in this Art Deco home. Beautifully decorated and featuring a sweeping staircase, original stained-glass windows, period furniture, and a stunningly unique black-and-white tiled bathroom.", List.of(new BedTypes(BedType.DOUBLE, 2))),
            new Unit(skHotel, "Big apartment for 10 people", 100.0, 300.0, "Retreat to the deck of this sustainable getaway and gaze at the twinkling constellations under a cosy tartan blanket. AirShip 2 is an iconic, insulated aluminium pod designed by Roderick James with views of the Sound of Mull from dragonfly windows.", List.of(new BedTypes(BedType.DOUBLE, 1), new BedTypes(BedType.KING, 4))),
            new Unit(belHouse, "Beach side House for 8 people", 150.0, 500.0, "The house is located in the enclave of Llandudno Beach, a locals-only spot with unspoilt, fine white sand and curling surfing waves. Although shops and restaurants are only a five-minute drive away, the area feels peaceful and secluded.", List.of(new BedTypes(BedType.DOUBLE, 2), new BedTypes(BedType.QUEEN, 2))),
            new Unit(lonApartment, "Apartment for 2", 30.0, 55.0, "Pretend you are lost in a magical forest as you perch on a log or curl up in the swinging chair. Soak in the tub, then fall asleep in a heavenly bedroom with cloud-painted walls and twinkling lights. And when you wake up, the espresso machine awaits.", List.of(new BedTypes(BedType.TWIN, 2)))
    );

    Reservation reservation1 = new Reservation(user1, units.get(0), units.get(0).getPrice(), 2,
            ZonedDateTime.parse("2021-03-23T00:00:00+02:00"), ZonedDateTime.parse("2021-03-24T00:00:00+02:00"));
    Reservation reservation2 = new Reservation(user, units.get(3), units.get(3).getPrice(), 6,
            ZonedDateTime.parse("2021-08-15T00:00:00+02:00"), ZonedDateTime.parse("2021-08-21T00:00:00+02:00"));
    Reservation reservation3 = new Reservation(user, units.get(2), units.get(2).getPrice(), 5,
            ZonedDateTime.parse("2021-10-15T00:00:00+02:00"), ZonedDateTime.parse("2021-10-20T00:00:00+02:00"));



    Review review = new Review(reservation1, "comment", user);
    Review review1 = new Review(reservation2, "comment", user1);
    Review review2 = new Review(reservation3, "comment", user1);


    @BeforeEach
    public void setUp(){

        review.setSentiment(Sentiment.POSITIVE);
        review1.setSentiment(Sentiment.NEGATIVE);
        review2.setSentiment(Sentiment.NEGATIVE);


        Mockito.when(reviewRepository.findById(1L)).
                thenReturn(Optional.of(review));

        Mockito.when(reviewRepository.findById(2L)).
                thenReturn(Optional.of(review1));

        Mockito.doNothing().when(reviewRepository).delete(review);

        Mockito.when(reviewRepository.save(review)).thenReturn(review);

        Mockito.when(reviewRepository.save(review1)).thenReturn(review1);

        Mockito.when(reviewRepository.save(review2)).thenReturn(review2);

        Mockito.when(reviewRepository.findByUser_UsernameAndReservationId("user1@user.com", 1))
                .thenReturn(Optional.of(review2));

        Mockito.when(reviewRepository.findAll())
                .thenReturn(List.of(review, review1, review2));



        Mockito.when(reservationService.findReservationById(1L))
                .thenReturn(reservation1);
        Mockito.when(reservationService.findReservationById(2L))
                .thenReturn(reservation2);
        Mockito.when(reservationService.findReservationById(3L))
                .thenReturn(reservation3);

        Mockito.when(reservationService.findReservationById(11L))
                .thenThrow(new ReservationNotFoundException(11L));


        Mockito.when(userService.loadUserByUsername("user@user.com"))
                .thenReturn(user);

        Mockito.when(userService.loadUserByUsername("user1@user.com"))
                .thenReturn(user1);

        Mockito.when(userService.loadUserByUsername("notUser"))
                .thenThrow(new UsernameNotFoundException("notUser"));

    }


    @Test
    void testDeleteReview(){
        Assertions.assertEquals(review, reviewService.deleteReview(1L));
    }

    @Test
    void testDeleteReviewWithInvalidId(){
        Assertions.assertThrows(ReviewNotFoundException.class,
                () -> reviewService.deleteReview(11L));
    }

    @Test
    void testSaveReview(){
        Assertions.assertEquals(review,
                reviewService.saveReview(review.getUser().getUsername(), review.getComment(), 1L, true));


        Assertions.assertEquals(review1,
                reviewService.saveReview(review1.getUser().getUsername(), review1.getComment(), 2L, false));


        Assertions.assertEquals(review2,
                reviewService.saveReview(review2.getUser().getUsername(), review2.getComment(), 3L, false));
    }


    @Test
    void testSaveReviewReservationNotFound(){
        Assertions.assertThrows(ReservationNotFoundException.class,
                () -> reviewService.saveReview(review.getUser().getUsername(), review.getComment(), 11, true));
    }

    @Test
    void testSaveReviewUsernameNotFound(){
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> reviewService.saveReview("notUser", review.getComment(), 1, true));
    }

    @Test
    void testEditReview(){
        review.setComment("new comment");
        Assertions.assertEquals(review,
                reviewService.editReview(review.getComment(), 1));
    }

    @Test
    void testEditReviewNotFound(){
        Assertions.assertThrows(ReviewNotFoundException.class,
                () -> reviewService.editReview("", 11L));
    }

    @Test
    void testCanUserWriteReview(){
        Assertions.assertTrue(reviewService.canUserWriteReview(2L, "user@user.com"));
    }

    @Test
    void testCanUserWriteReviewBeforeTheDate(){
        Assertions.assertFalse(reviewService.canUserWriteReview(3L, "user@user.com"));
    }

    @Test
    void testCanUserWriteReviewAfterMoreThan15Days(){
        Assertions.assertFalse(reviewService.canUserWriteReview(1L, "user1@user.com"));
    }

    @Test
    void testCanUserWriteReviewUserIsNotCreator(){
        Assertions.assertThrows(UserNotCreatorOfReservationException.class,
                () -> reviewService.canUserWriteReview(1L, "user@user.com"));
    }

    @Test
    void testAlreadyWrite(){
        Assertions.assertTrue(reviewService.alreadyWrite(1L, "user1@user.com"));
    }

    @Test
    void testNotAlreadyWrite(){
        Assertions.assertFalse(reviewService.alreadyWrite(2L, "user@user.com"));
    }

    @Test
    void testFindById(){
        Assertions.assertEquals(review,
                reviewService.findById(1L));
    }

    @Test
    void testFindByIdNotFound(){
        Assertions.assertThrows(ReviewNotFoundException.class,
                () -> reviewService.findById(112L));
    }


    @Test
    void testListAll(){
        Assertions.assertEquals(List.of(review, review1, review2),
                                                        reviewService.listAll());
    }

}
