package com.hvt.booking_lux.unitTesting;

import com.hvt.booking_lux.model.*;
import com.hvt.booking_lux.model.enumeration.BedType;
import com.hvt.booking_lux.model.enumeration.Category;
import com.hvt.booking_lux.model.enumeration.Role;
import com.hvt.booking_lux.model.enumeration.Status;
import com.hvt.booking_lux.model.exceptions.CityNotFoundException;
import com.hvt.booking_lux.model.exceptions.CountryNotFoundException;
import com.hvt.booking_lux.model.exceptions.ResObjectNotFoundException;
import com.hvt.booking_lux.repository.CityRepository;
import com.hvt.booking_lux.repository.CountryRepository;
import com.hvt.booking_lux.repository.ResObjectRepository;
import com.hvt.booking_lux.repository.UnitRepository;
import com.hvt.booking_lux.service.ReservationService;
import com.hvt.booking_lux.service.impl.ReservationObjectServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SpringBootTest
public class ReservationObjectServiceTests {

    @Mock
    private ResObjectRepository resObjectRepository;

    @Mock
    private UnitRepository unitRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationObjectServiceImpl reservationObjectService;

    User user = new User("user@user.com", "pass", "User", "User", Role.ROLE_USER, "address", "000555888");
    User user1 = new User("user1@user.com", "pass1", "User1", "User1", Role.ROLE_USER, "address1", "111555888");

    Country macedonia = new Country("Macedonia", "MKD", "flag");
    Country serbia = new Country("Serbia", "SRB", "flag");
    Country england = new Country("England", "ENG", "flag");

    City skopje = new City("Skopje", macedonia);
    City belgrade = new City("Belgrade", serbia);
    City london = new City("London", england);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a z");

    ResObject skHotel = new ResObject("Hotel Makedonija", "Skopje 8/50","Beautiful Hotel In center of Skopje with view of the river Vardar.", Category.HOTEL, user, skopje);
    ResObject belHouse = new ResObject("House in Belgrade", "Belgradska ulica 11/11","Unwind at this stunning French Provencal beachside cottage. The house was lovingly built with stone floors, high-beamed ceilings, and antique details for a luxurious yet charming feel. Enjoy the sea and mountain views from the pool and lush garden.", Category.HOUSE, user, belgrade);
    ResObject lonApartment = new ResObject("Apartment for 2 people", "Manchesterska","Pretend you are lost in a magical forest as you perch on a log or curl up in the swinging chair. Soak in the tub, then fall asleep in a heavenly bedroom with cloud-painted walls and twinkling lights. And when you wake up, the espresso machine awaits.", Category.APARTMENT, user1, london);
    ResObject belHotel = new ResObject("Belgrade hotel", "Belgrade","Description", Category.HOTEL, user1, belgrade);

    ResObject resObject = new ResObject("Name", "address", "description", Category.HOTEL, user, london);
    ResObject resObject1 = new ResObject("Name1", "address1", "description1", Category.APARTMENT, user, london);

    List<ResObject> resObjects = List.of(
            skHotel, belHouse, lonApartment, belHotel
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
            ZonedDateTime.parse("2021-03-25T00:00:00+02:00"), ZonedDateTime.parse("2021-03-31T00:00:00+02:00"));
    Reservation reservation3 = new Reservation(user, units.get(2), units.get(2).getPrice(), 1,
            ZonedDateTime.parse("2021-07-23T00:00:00+02:00"), ZonedDateTime.parse("2021-07-24T00:00:00+02:00"));

    @BeforeEach
    void setUp() {

        skopje.setId(1L);
        belgrade.setId(2L);
        london.setId(3L);

        skHotel.setId(1L);
        belHouse.setId(2L);
        lonApartment.setId(3L);
        resObject.setId(4L);
        belHotel.setId(5L);

        units.get(0).setId(1L);
        units.get(1).setId(2L);
        units.get(2).setId(3L);
        units.get(3).setId(4L);
        units.get(4).setId(5L);

        macedonia.setCityList(List.of(skopje));
        serbia.setCityList(List.of(belgrade));
        england.setCityList(List.of(london));

        skHotel.setUnits(units.subList(0, 3));
        belHouse.setUnits(units.subList(3, 4));
        lonApartment.setUnits(units.subList(4, 5));


        skHotel.setLowestPrice(22.0);
        belHouse.setLowestPrice(150.0);
        lonApartment.setLowestPrice(55.0);

        Mockito.when(resObjectRepository.findAll())
                .thenReturn(resObjects);

        Mockito.when(resObjectRepository.findById(1L))
                .thenReturn(Optional.of(resObjects.get(0)));

        Mockito.when(resObjectRepository.findById(2L))
                .thenReturn(Optional.of(resObjects.get(1)));

        Mockito.when(resObjectRepository.findById(3L))
                .thenReturn(Optional.of(resObjects.get(2)));

        Mockito.when(resObjectRepository.findById(4L))
                .thenReturn(Optional.of(resObject));

        Mockito.when(resObjectRepository.findById(5L))
                .thenReturn(Optional.of(resObjects.get(3)));

        Mockito.when(resObjectRepository.findAllByCity(skopje))
                .thenReturn(List.of(resObjects.get(0)));

        Mockito.when(resObjectRepository.findAllByCity(belgrade))
                .thenReturn(List.of(resObjects.get(1), resObjects.get(3)));

        Mockito.when(resObjectRepository.findAllByCity(london))
                .thenReturn(List.of(resObjects.get(2)));

        Mockito.when(resObjectRepository.findAllByCreator(user))
                .thenReturn(resObjects.subList(0, 2));

        Mockito.when(resObjectRepository.save(resObject))
                .thenReturn(resObject);

        Mockito.when(resObjectRepository.save(resObject1))
                .thenReturn(resObject1);


        Mockito.when(cityRepository.findById(1L))
                .thenReturn(Optional.of(skopje));

        Mockito.when(cityRepository.findById(2L))
                .thenReturn(Optional.of(belgrade));

        Mockito.when(cityRepository.findById(3L))
                .thenReturn(Optional.of(london));

        Mockito.when(cityRepository.findByNameContains("Skopje"))
                .thenReturn(Optional.of(skopje));

        Mockito.when(cityRepository.findByNameContains("Belgrade"))
                .thenReturn(Optional.of(belgrade));

        Mockito.when(cityRepository.findByNameContains("London"))
                .thenReturn(Optional.of(london));


        Mockito.when(countryRepository.findById(1L))
                .thenReturn(Optional.of(macedonia));

        Mockito.when(countryRepository.findById(2L))
                .thenReturn(Optional.of(serbia));

        Mockito.when(countryRepository.findById(3L))
                .thenReturn(Optional.of(england));


        Mockito.when(reservationService.listAll())
                .thenReturn(List.of(reservation1, reservation2, reservation3));

        Mockito.when(reservationService.findAllResObjectsThatAreNotReservedAtThatTime(
                ZonedDateTime.parse("2021-03-23T00:00:00+02:00"), ZonedDateTime.parse("2021-03-30T00:00:00+02:00"), 1))
                .thenReturn(List.of(skHotel, lonApartment));

        Mockito.when(reservationService.findAllResObjectsThatAreNotReservedAtThatTime(
                ZonedDateTime.parse("2021-03-21T00:00:00+02:00"), ZonedDateTime.parse("2021-03-24T00:00:00+02:00"), 5))
                .thenReturn(List.of(skHotel, belHouse));

        Mockito.when(reservationService.findAllResObjectsThatAreNotReservedAtThatTime(
                ZonedDateTime.parse("2022-03-23T00:00:00+02:00"), ZonedDateTime.parse("2022-03-30T00:00:00+02:00"), 2))
                .thenReturn(List.of(skHotel, belHouse, lonApartment));
    }

    @Test
    public void testListAll(){
        Assertions.assertEquals(resObjects.subList(0, 3), reservationObjectService.listAll());
    }

    @Test
    public void testListByCountry(){
        Assertions.assertEquals(List.of(resObjects.get(0)), reservationObjectService.listByCountry(1));
        Assertions.assertEquals(List.of(resObjects.get(1), resObjects.get(3)), reservationObjectService.listByCountry(2));
        Assertions.assertEquals(List.of(resObjects.get(2)), reservationObjectService.listByCountry(3));
    }

    @Test
    public void testListByCountryIdNotFound(){
        Assertions.assertThrows(CountryNotFoundException.class, () -> reservationObjectService.listByCountry(12));
    }

    @Test
    public void testListUserAccommodationListings(){
        Assertions.assertEquals(List.of(resObjects.get(0), resObjects.get(1)), reservationObjectService.listUserAccommodationListings(user));
    }

    @Test
    public void testListAllNotAvailableWithoutNumberConstraint(){
        Assertions.assertEquals(List.of(units.get(1), units.get(2)), reservationObjectService.listAllAvailableUnitsForResObject(1,
                ZonedDateTime.parse("2021-03-23T00:00:00+02:00"), ZonedDateTime.parse("2021-03-30T00:00:00+02:00"), 1));
    }

    @Test
    public void testListAllNotAvailable(){
        Assertions.assertEquals(List.of(units.get(2)), reservationObjectService.listAllAvailableUnitsForResObject(1,
                ZonedDateTime.parse("2021-03-23T00:00:00+02:00"), ZonedDateTime.parse("2021-03-30T00:00:00+02:00"), 5));
    }

    @Test
    public void testListAllNotAvailableWhereAllAreAvailable(){
        Assertions.assertEquals(units.subList(0, 3), reservationObjectService.listAllAvailableUnitsForResObject(1,
                ZonedDateTime.parse("2022-01-20T00:00:00+02:00"), ZonedDateTime.parse("2022-01-24T00:00:00+02:00"), 1));
    }

    @Test
    public void testListAllNotAvailableLondon(){
        Assertions.assertEquals(Collections.emptyList(), reservationObjectService.listAllAvailableUnitsForResObject(3,
                ZonedDateTime.parse("2022-01-20T00:00:00+02:00"), ZonedDateTime.parse("2022-01-24T00:00:00+02:00"), 3));
    }

    @Test
    public void testListAllNotAvailableLondonNumberConstrainSatisfied(){
        Assertions.assertEquals(List.of(units.get(4)), reservationObjectService.listAllAvailableUnitsForResObject(3,
                ZonedDateTime.parse("2022-01-20T00:00:00+02:00"), ZonedDateTime.parse("2022-01-24T00:00:00+02:00"), 2));
    }

    @Test
    public void testListAllNotAvailableBelgrade(){
        Assertions.assertEquals(List.of(units.get(3)), reservationObjectService.listAllAvailableUnitsForResObject(2,
                ZonedDateTime.parse("2021-03-20T00:00:00+02:00"), ZonedDateTime.parse("2021-03-22T00:00:00+02:00"), 7));
    }

    @Test
    public void testListAllNotAvailableBelgradeAllNotAvailable(){
        Assertions.assertEquals(List.of(units.get(3)), reservationObjectService.listAllAvailableUnitsForResObject(2,
                ZonedDateTime.parse("2021-03-20T00:00:00+02:00"), ZonedDateTime.parse("2021-03-24T00:00:00+02:00"), 3));
    }

    @Test
    public void testListAllNotAvailableBelgradeWhereAllAreAvailable(){
        Assertions.assertEquals(List.of(units.get(3)), reservationObjectService.listAllAvailableUnitsForResObject(2,
                ZonedDateTime.parse("2022-01-20T00:00:00+02:00"), ZonedDateTime.parse("2022-01-24T00:00:00+02:00"), 3));
    }

    @Test
    public void testListAllNotAvailableForIdNotFound(){
        Assertions.assertThrows(ResObjectNotFoundException.class, () -> reservationObjectService.listAllAvailableUnitsForResObject(11,
                ZonedDateTime.parse("2022-01-20T00:00:00+02:00"), ZonedDateTime.parse("2022-01-24T00:00:00+02:00"), 3));
    }

    @Test
    public void testListAllNotAvailableForResObjectWithoutUnits(){
        Assertions.assertEquals(Collections.emptyList(), reservationObjectService.listAllAvailableUnitsForResObject(5,
                ZonedDateTime.parse("2022-01-20T00:00:00+02:00"), ZonedDateTime.parse("2022-01-24T00:00:00+02:00"), 3));
    }


    @Test
    public void testLowestPriceForUnit(){

        Assertions.assertEquals(30.0, reservationObjectService.lowestPriceForUnit(1,
                ZonedDateTime.parse("2022-03-23T00:00:00+02:00"), ZonedDateTime.parse("2022-03-30T00:00:00+02:00"), 1));

        Assertions.assertEquals(500.0, reservationObjectService.lowestPriceForUnit(2,
                ZonedDateTime.parse("2022-03-23T00:00:00+02:00"), ZonedDateTime.parse("2022-03-30T00:00:00+02:00"), 1));

        Assertions.assertEquals(55.0, reservationObjectService.lowestPriceForUnit(3,
                ZonedDateTime.parse("2022-03-23T00:00:00+02:00"), ZonedDateTime.parse("2022-03-30T00:00:00+02:00"), 1));


        Assertions.assertEquals(100.0, reservationObjectService.lowestPriceForUnit(1,
                ZonedDateTime.parse("2021-03-23T00:00:00+02:00"), ZonedDateTime.parse("2021-03-30T00:00:00+02:00"), 1));

        Assertions.assertEquals(30.0, reservationObjectService.lowestPriceForUnit(1,
                ZonedDateTime.parse("2021-07-23T00:00:00+02:00"), ZonedDateTime.parse("2021-03-24T00:00:00+02:00"), 1));

    }

    @Test
    public void testLowestPriceForUnitIdNotFound(){
        Assertions.assertThrows(ResObjectNotFoundException.class, () -> reservationObjectService.lowestPriceForUnit(11,
                ZonedDateTime.parse("2021-03-23T00:00:00+02:00"), ZonedDateTime.parse("2021-03-30T00:00:00+02:00"), 1));
    }

    @Test
    public void testLowestPriceForResObjectWithoutUnits(){
        Assertions.assertThrows(NoSuchElementException.class, () -> reservationObjectService.lowestPriceForUnit(5,
                ZonedDateTime.parse("2021-03-23T00:00:00+02:00"), ZonedDateTime.parse("2021-03-30T00:00:00+02:00"), 1));
    }

    @Test
    public void testLowestPriceForUnitResObjectWithNoAvailableUnits(){
        Assertions.assertThrows(NoSuchElementException.class, () -> reservationObjectService.lowestPriceForUnit(2,
                ZonedDateTime.parse("2021-03-23T00:00:00+02:00"), ZonedDateTime.parse("2021-03-30T00:00:00+02:00"), 1));
    }

    @Test
    public void testFindAllAvailable(){
        Assertions.assertEquals(List.of(skHotel), reservationObjectService.findAllAvailable(
                ZonedDateTime.parse("2021-03-23T00:00:00+02:00"), ZonedDateTime.parse("2021-03-30T00:00:00+02:00"), 1, "Skopje"));


        Assertions.assertEquals(Collections.emptyList(), reservationObjectService.findAllAvailable(
                ZonedDateTime.parse("2021-03-23T00:00:00+02:00"), ZonedDateTime.parse("2021-03-30T00:00:00+02:00"), 1, "Belgrade"));

        Assertions.assertEquals(List.of(lonApartment), reservationObjectService.findAllAvailable(
                ZonedDateTime.parse("2021-03-23T00:00:00+02:00"), ZonedDateTime.parse("2021-03-30T00:00:00+02:00"), 1, "London"));

        Assertions.assertEquals(List.of(skHotel), reservationObjectService.findAllAvailable(
                ZonedDateTime.parse("2021-03-21T00:00:00+02:00"), ZonedDateTime.parse("2021-03-24T00:00:00+02:00"), 5, "Skopje"));

        Assertions.assertEquals(List.of(belHouse), reservationObjectService.findAllAvailable(
                ZonedDateTime.parse("2021-03-21T00:00:00+02:00"), ZonedDateTime.parse("2021-03-24T00:00:00+02:00"), 5, "Belgrade"));

        Assertions.assertEquals(List.of(skHotel), reservationObjectService.findAllAvailable(
                ZonedDateTime.parse("2022-03-23T00:00:00+02:00"), ZonedDateTime.parse("2022-03-30T00:00:00+02:00"), 2, "Skopje"));

        Assertions.assertEquals(List.of(belHouse), reservationObjectService.findAllAvailable(
                ZonedDateTime.parse("2022-03-23T00:00:00+02:00"), ZonedDateTime.parse("2022-03-30T00:00:00+02:00"), 2, "Belgrade"));

        Assertions.assertEquals(List.of(lonApartment), reservationObjectService.findAllAvailable(
                ZonedDateTime.parse("2022-03-23T00:00:00+02:00"), ZonedDateTime.parse("2022-03-30T00:00:00+02:00"), 2, "London"));

    }

    @Test
    public void testListByCity(){
        Assertions.assertEquals(List.of(skHotel), reservationObjectService.listByCity(1));

        Assertions.assertEquals(List.of(belHouse, belHotel), reservationObjectService.listByCity(2));

        Assertions.assertEquals(List.of(lonApartment), reservationObjectService.listByCity(3));
    }

    @Test
    public void testListByCityIdNotFound(){
        Assertions.assertThrows(CityNotFoundException.class, () -> reservationObjectService.listByCity(12));
    }

    @Test
    public void testSave(){
        Assertions.assertEquals(resObject, reservationObjectService.save(
                resObject.getName(), resObject.getAddress(), resObject.getDescription(), resObject.getCategory(),
                user, 3, new ArrayList<>()
        ));
    }

    @Test
    public void testSaveIdNotFound(){
        Assertions.assertThrows(CityNotFoundException.class, () -> reservationObjectService.save(
                resObject.getName(), resObject.getAddress(), resObject.getDescription(), resObject.getCategory(),
                user, 12, new ArrayList<>()
        ));
    }

    @Test
    public void testEdit(){
        Assertions.assertEquals(resObject1, reservationObjectService.edit(4,
                resObject1.getName(), resObject1.getAddress(), resObject1.getDescription(), resObject1.getCategory(), new ArrayList<>()));
    }

    @Test
    public void testEditIdNotFound(){
        Assertions.assertThrows(ResObjectNotFoundException.class, () -> reservationObjectService.edit(11,
                resObject1.getName(), resObject1.getAddress(), resObject1.getDescription(), resObject1.getCategory(), new ArrayList<>()));
    }

    @Test
    public void testFindResObjectById(){
        Assertions.assertEquals(resObjects.get(0), reservationObjectService.findResObjectById(1));
        Assertions.assertEquals(resObjects.get(1), reservationObjectService.findResObjectById(2));
        Assertions.assertEquals(resObjects.get(2), reservationObjectService.findResObjectById(3));
        Assertions.assertEquals(resObjects.get(3), reservationObjectService.findResObjectById(5));
        Assertions.assertEquals(resObject, reservationObjectService.findResObjectById(4));
    }

    @Test
    public void testFindResObjectByIdIdNotFound(){
        Assertions.assertThrows(ResObjectNotFoundException.class, () -> reservationObjectService.findResObjectById(11));
    }

    @Test
    public void testDelete(){
        resObject.setStatus(Status.DELETED);
        resObject.getUnits().forEach(unit -> unit.setStatus(Status.DELETED));
        Assertions.assertEquals(resObject, reservationObjectService.delete(4));

        ResObject tmp = resObjects.get(0);
        tmp.setStatus(Status.DELETED);
        tmp.getUnits().forEach(unit -> unit.setStatus(Status.DELETED));
        Assertions.assertEquals(resObject, reservationObjectService.delete(4));
    }

    @Test
    public void testDeleteIdNotFound(){
        resObject.setStatus(Status.DELETED);
        resObject.getUnits().forEach(unit -> unit.setStatus(Status.DELETED));
        Assertions.assertThrows(ResObjectNotFoundException.class, () -> reservationObjectService.delete(11));
    }

    @Test
    public void testListByCityNotPresent(){
        Assertions.assertEquals(List.of(skHotel, belHouse, lonApartment, belHotel), reservationObjectService.listByCityName("Veles"));
    }

    @Test
    public void testListByCityName(){
        Assertions.assertEquals(List.of(skHotel), reservationObjectService.listByCityName("Skopje"));

        Assertions.assertEquals(List.of(belHouse, belHotel), reservationObjectService.listByCityName("Belgrade"));

        Assertions.assertEquals(List.of(lonApartment), reservationObjectService.listByCityName("London"));
    }

}
