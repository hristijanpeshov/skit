package com.hvt.booking_lux.unitTesting;

import com.hvt.booking_lux.model.*;
import com.hvt.booking_lux.model.enumeration.*;
import com.hvt.booking_lux.model.exceptions.ResObjectNotFoundException;
import com.hvt.booking_lux.model.exceptions.ReservationNotFoundException;
import com.hvt.booking_lux.model.exceptions.UnitHasNoBedsException;
import com.hvt.booking_lux.model.exceptions.UnitNotFoundException;
import com.hvt.booking_lux.repository.BedTypesRepository;
import com.hvt.booking_lux.repository.ResObjectRepository;
import com.hvt.booking_lux.repository.ReservationRepository;
import com.hvt.booking_lux.repository.UnitRepository;
import com.hvt.booking_lux.service.impl.UnitServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UnitServiceTests {

    @Mock
    UnitRepository unitRepository;
    @Mock
    ResObjectRepository resObjectRepository;
    @Mock
    ReservationRepository reservationRepository;
    @Mock
    BedTypesRepository bedTypesRepository;

    @InjectMocks
    UnitServiceImpl unitService;

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
            new Unit(skHotel, "Big apartment for 10 people", 100.0, 300.0, "Retreat to the deck of this sustainable getaway and gaze at the twinkling constellations under a cosy tartan blanket. AirShip 2 is an iconic, insulated aluminium pod designed by Roderick James with views of the Sound of Mull from dragonfly windows.", List.of(new BedTypes(BedType.DOUBLE, 1), new BedTypes(BedType.KING, 4))),
            new Unit(belHouse, "Beach side House for 8 people", 150.0, 500.0, "The house is located in the enclave of Llandudno Beach, a locals-only spot with unspoilt, fine white sand and curling surfing waves. Although shops and restaurants are only a five-minute drive away, the area feels peaceful and secluded.", List.of(bedTypes1, bedTypes2)),
            new Unit(lonApartment, "Apartment for 2", 30.0, 55.0, "Pretend you are lost in a magical forest as you perch on a log or curl up in the swinging chair. Soak in the tub, then fall asleep in a heavenly bedroom with cloud-painted walls and twinkling lights. And when you wake up, the espresso machine awaits.", List.of(new BedTypes(BedType.TWIN, 2)))
    );

    Unit unit = new Unit(resObject, "Apartment for 2", 30.0, 55.0, "Description", List.of(new BedTypes(BedType.TWIN, 2)));


    Reservation reservation1 = new Reservation(user1, units.get(0), units.get(0).getPrice(), 2,
            ZonedDateTime.parse("2021-03-23T00:00:00+02:00"), ZonedDateTime.parse("2021-03-24T00:00:00+02:00"));
    Reservation reservation2 = new Reservation(user, units.get(3), units.get(3).getPrice(), 6,
            ZonedDateTime.parse("2021-08-15T00:00:00+02:00"), ZonedDateTime.parse("2021-08-21T00:00:00+02:00"));
    Reservation reservation3 = new Reservation(user, units.get(2), units.get(2).getPrice(), 5,
            ZonedDateTime.parse("2021-10-15T00:00:00+02:00"), ZonedDateTime.parse("2021-10-20T00:00:00+02:00"));



    @BeforeEach
    public void setUp(){

        skopje.setId(1L);
        belgrade.setId(2L);
        london.setId(3L);
        resObject.setId(4L);

        resObject.setStatus(Status.DELETED);

        skHotel.setId(1L);
        belHouse.setId(2L);
        lonApartment.setId(3L);

        units.get(0).setId(1L);
        units.get(1).setId(2L);
        units.get(2).setId(3L);
        units.get(3).setId(4L);
        units.get(4).setId(5L);

        unit.setId(6L);
        unit.setStatus(Status.DELETED);

        macedonia.setCityList(List.of(skopje));
        serbia.setCityList(List.of(belgrade));
        england.setCityList(List.of(london));

        skHotel.setUnits(units.subList(0, 3));
        belHouse.setUnits(units.subList(3, 4));
        lonApartment.setUnits(units.subList(4, 5));


        skHotel.setLowestPrice(22.0);
        belHouse.setLowestPrice(150.0);
        lonApartment.setLowestPrice(55.0);


        Mockito.when(reservationRepository.findById(1L))
                .thenReturn(Optional.of(reservation1));

        Mockito.when(reservationRepository.findById(2L))
                .thenReturn(Optional.of(reservation2));

        Mockito.when(reservationRepository.findById(3L))
                .thenReturn(Optional.of(reservation3));

        Mockito.when(reservationRepository.findAll())
                .thenReturn(List.of(reservation1, reservation2, reservation3));



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

        Mockito.when(resObjectRepository.findAllByCity(skopje))
                .thenReturn(List.of(resObjects.get(0)));

        Mockito.when(resObjectRepository.findAllByCity(belgrade))
                .thenReturn(List.of(resObjects.get(1)));

        Mockito.when(resObjectRepository.findAllByCity(london))
                .thenReturn(List.of(resObjects.get(2)));

        Mockito.when(resObjectRepository.findAllByCreator(user))
                .thenReturn(resObjects.subList(0, 2));

        Mockito.when(resObjectRepository.save(resObject))
                .thenReturn(resObject);

        Mockito.when(resObjectRepository.save(resObject1))
                .thenReturn(resObject1);


        Mockito.when(unitRepository.findById(1L))
                .thenReturn(Optional.of(units.get(0)));

        Mockito.when(unitRepository.findById(2L))
                .thenReturn(Optional.of(units.get(1)));

        Mockito.when(unitRepository.findById(3L))
                .thenReturn(Optional.of(units.get(2)));

        Mockito.when(unitRepository.findById(4L))
                .thenReturn(Optional.of(units.get(3)));

        Mockito.when(unitRepository.findById(5L))
                .thenReturn(Optional.of(units.get(4)));

        Mockito.when(unitRepository.findById(6L))
                .thenReturn(Optional.of(unit));

        Mockito.when(unitRepository.findAllByResObject(skHotel))
                .thenReturn(skHotel.getUnits());

        Mockito.when(unitRepository.findAllByResObject(belHouse))
                .thenReturn(belHouse.getUnits());

        Mockito.when(unitRepository.findAllByResObject(lonApartment))
                .thenReturn(lonApartment.getUnits());

        Mockito.when(unitRepository.findAllByResObject(resObject))
                .thenReturn(Collections.emptyList());

        Mockito.when(unitRepository.findAll())
                .thenReturn(units);

        Mockito.when(unitRepository.save(unit))
                .thenReturn(unit);

        Mockito.when(unitRepository.save(units.get(0)))
                .thenReturn(units.get(0));

        Mockito.when(unitRepository.save(units.get(3)))
                .thenReturn(units.get(3));


        Mockito.when(bedTypesRepository.save(bedTypes1))
                .thenReturn(bedTypes1);

        Mockito.when(bedTypesRepository.save(bedTypes2))
                .thenReturn(bedTypes2);

        Mockito.when(bedTypesRepository.save(bedTypes3))
                .thenReturn(bedTypes3);

        Mockito.when(bedTypesRepository.save(new BedTypes(BedType.SOFA, 2)))
                .thenReturn(new BedTypes(BedType.SOFA, 2));

        Mockito.when(bedTypesRepository.save(new BedTypes(BedType.TWIN, 1)))
                .thenReturn(new BedTypes(BedType.TWIN, 1));

        Mockito.when(bedTypesRepository.save(new BedTypes(BedType.QUEEN, 1)))
                .thenReturn(new BedTypes(BedType.QUEEN, 1));

    }


    @Test
    void testIsUnitFree(){
        Assertions.assertTrue(
                unitService.isUnitFree(1L,  ZonedDateTime.parse("2021-04-01T00:00:00+02:00"),
                        ZonedDateTime.parse("2021-04-07T00:00:00+02:00"), 2)
        );
    }

    @Test
    void testIsUnitReserved(){
        Assertions.assertFalse(
                unitService.isUnitFree(3L,  ZonedDateTime.parse("2021-10-07T00:00:00+02:00"),
                        ZonedDateTime.parse("2021-10-17T00:00:00+02:00"), 2)
        );

        Assertions.assertFalse(
                unitService.isUnitFree(3L,  ZonedDateTime.parse("2021-10-14T00:00:00+02:00"),
                        ZonedDateTime.parse("2021-10-16T00:00:00+02:00"), 2)
        );

        Assertions.assertFalse(
                unitService.isUnitFree(3L,  ZonedDateTime.parse("2021-10-16T00:00:00+02:00"),
                        ZonedDateTime.parse("2021-10-18T00:00:00+02:00"), 2)
        );

        Assertions.assertFalse(
                unitService.isUnitFree(3L,  ZonedDateTime.parse("2021-10-14T00:00:00+02:00"),
                        ZonedDateTime.parse("2021-10-22T00:00:00+02:00"), 2)
        );

        Assertions.assertFalse(
                unitService.isUnitFree(3L,  ZonedDateTime.parse("2021-10-15T00:00:00+02:00"),
                        ZonedDateTime.parse("2021-10-20T00:00:00+02:00"), 2)
        );
    }

    @Test
    void testIsUnitFreeUnitNotFound(){
        Assertions.assertTrue (
                () -> unitService.isUnitFree(22L, ZonedDateTime.parse("2021-10-15T00:00:00+02:00"),
                        ZonedDateTime.parse("2021-10-20T00:00:00+02:00"), 2));
    }

    @Test
    void testListAll(){
        Assertions.assertEquals(skHotel.getUnits(), unitService.listAll(1L));

        Assertions.assertEquals(belHouse.getUnits(), unitService.listAll(2L));

        Assertions.assertEquals(lonApartment.getUnits(), unitService.listAll(3L));

        Assertions.assertEquals(Collections.emptyList(), unitService.listAll(4L));
    }

    @Test
    void testListAllResObjectNotFound(){
        Assertions.assertThrows(ResObjectNotFoundException.class, () -> unitService.listAll(11L));
    }

    @Test
    void testFindTheMostExpensive(){
        Assertions.assertEquals(units.get(3), unitService.findTheMostExpensive());
    }

    @Test
    void testFindTheMostExpensiveWhenThereAreNoUnits(){
        Mockito.when(unitRepository.findAll())
                .thenReturn(Collections.emptyList());
        Assertions.assertNull(unitService.findTheMostExpensive());
    }

    @Test
    void testFindTheLeastExpensive(){
        Assertions.assertEquals(units.get(0), unitService.findTheLeastExpensive());
    }

    @Test
    void testFindTheLeastExpensiveWhenThereAreNoUnits(){
        Mockito.when(unitRepository.findAll())
                .thenReturn(Collections.emptyList());
        Assertions.assertNull(unitService.findTheLeastExpensive());
    }

    @Test
    void testFindTheLargest(){
        Assertions.assertEquals(units.get(3), unitService.findTheLargest());
    }

    @Test
    void testFindTheLargestWhenThereAreNoUnits(){
        Mockito.when(unitRepository.findAll())
                .thenReturn(Collections.emptyList());
        Assertions.assertNull(unitService.findTheLargest());
    }

    @Test
    void testFindTheSmallest(){
        Assertions.assertEquals(units.get(0), unitService.findTheSmallest());
    }

    @Test
    void testFindTheSmallestWhenThereAreNoUnits(){
        Mockito.when(unitRepository.findAll())
                .thenReturn(Collections.emptyList());
        Assertions.assertNull(unitService.findTheSmallest());
    }

    @Test
    void testSave(){
        Unit u = units.get(3);

        Assertions.assertEquals(u,
                unitService.save(2L, u.getTitle(), u.getSize(), u.getPrice(), u.getDescription(), List.of(BedType.DOUBLE, BedType.QUEEN),  List.of(2, 2), Collections.emptyList()));

        Unit u1 = units.get(0);
        Assertions.assertEquals(u1,
                unitService.save(1L, u1.getTitle(), u1.getSize(), u1.getPrice(), u1.getDescription(), List.of(BedType.DOUBLE),  List.of(1), Collections.emptyList()));
    }

    @Test
    void testSaveResObjectIdNotFound(){
        Unit u = units.get(0);

        Assertions.assertThrows(ResObjectNotFoundException.class,
                () -> unitService.save(12, u.getTitle(), u.getSize(), u.getPrice(), u.getDescription(), List.of(BedType.DOUBLE),  List.of(1), Collections.emptyList()));
    }

    @Test
    void testSaveBedTypesEmpty(){
        Unit u = units.get(0);

        Assertions.assertThrows(UnitHasNoBedsException.class,
                () -> unitService.save(1L, u.getTitle(), u.getSize(), u.getPrice(), u.getDescription(), Collections.emptyList(),  List.of(2, 2), Collections.emptyList()));

        Assertions.assertThrows(UnitHasNoBedsException.class,
                () -> unitService.save(1L, u.getTitle(), u.getSize(), u.getPrice(), u.getDescription(), Collections.emptyList(),  Collections.emptyList(), Collections.emptyList()));

        Assertions.assertThrows(UnitHasNoBedsException.class,
                () -> unitService.save(1L, u.getTitle(), u.getSize(), u.getPrice(), u.getDescription(), List.of(BedType.DOUBLE),  List.of(0), Collections.emptyList()));

        Assertions.assertThrows(UnitHasNoBedsException.class,
                () -> unitService.save(1L, u.getTitle(), u.getSize(), u.getPrice(), u.getDescription(), List.of(BedType.DOUBLE, BedType.QUEEN),  List.of(-1, -3), Collections.emptyList()));

        Assertions.assertEquals(u,
                unitService.save(1L, u.getTitle(), u.getSize(), u.getPrice(), u.getDescription(), List.of(BedType.DOUBLE, BedType.QUEEN),  List.of(1, 0), Collections.emptyList()));
    }

    @Test
    void testEditTitle(){
        Unit unit = units.get(4);

        unit.setTitle("New Title");
        Mockito.when(unitRepository.save(unit))
                .thenReturn(unit);

        Assertions.assertEquals(unit,
                unitService.edit(unit.getId(), "New Title", unit.getSize(), unit.getPrice(), unit.getDescription(), List.of(BedType.TWIN), List.of(2), Collections.emptyList()));
    }

    @Test
    void testEditSize(){
        Unit unit = units.get(4);

        unit.setSize(15);
        Mockito.when(unitRepository.save(unit))
                .thenReturn(unit);

        Assertions.assertEquals(unit,
                unitService.edit(unit.getId(), unit.getTitle(), 15, unit.getPrice(), unit.getDescription(), List.of(BedType.TWIN), List.of(2), Collections.emptyList()));
    }

    @Test
    void testEditPrice(){
        Unit unit = units.get(4);

        unit.setPrice(150.0);
        Mockito.when(unitRepository.save(unit))
                .thenReturn(unit);

        Assertions.assertEquals(unit,
                unitService.edit(unit.getId(), unit.getTitle(), unit.getSize(), 150.0, unit.getDescription(), List.of(BedType.TWIN), List.of(2), Collections.emptyList()));
    }

    @Test
    void testEditDescription(){
        Unit unit = units.get(4);

        unit.setDescription("New Description");
        Mockito.when(unitRepository.save(unit))
                .thenReturn(unit);

        Assertions.assertEquals(unit,
                unitService.edit(unit.getId(), unit.getTitle(), unit.getSize(), unit.getPrice(), "New Description", List.of(BedType.TWIN), List.of(2), Collections.emptyList()));
    }

    @Test
    void testEditBedTypes(){
        Unit unit = units.get(4);

        unit.setBedTypes(List.of(new BedTypes(BedType.SOFA, 2), new BedTypes(BedType.TWIN, 1)));
        Mockito.when(unitRepository.save(unit))
                .thenReturn(unit);

        Assertions.assertEquals(unit,
                unitService.edit(unit.getId(), unit.getTitle(), unit.getSize(), unit.getPrice(), unit.getDescription(), List.of(BedType.SOFA, BedType.TWIN), List.of(2, 1), Collections.emptyList()));
    }

    @Test
    void testEditWholeUnit(){
        Unit unit = units.get(1);

        unit.setTitle("Room for 2");
        unit.setSize(35);
        unit.setPrice(50.0);
        unit.setDescription("New Description");
        unit.setBedTypes(List.of(new BedTypes(BedType.QUEEN, 1)));
        Mockito.when(unitRepository.save(unit))
                .thenReturn(unit);

        Assertions.assertEquals(unit,
                unitService.edit(unit.getId(), unit.getTitle(), unit.getSize(), unit.getPrice(), unit.getDescription(), List.of(BedType.QUEEN), List.of(1), Collections.emptyList()));
    }


    @Test
    void testFindById(){
        Assertions.assertEquals(units.get(0), unitService.findById(1L));

        Assertions.assertEquals(units.get(1), unitService.findById(2L));

        Assertions.assertEquals(units.get(2), unitService.findById(3L));

        Assertions.assertEquals(units.get(3), unitService.findById(4L));

        Assertions.assertEquals(units.get(4), unitService.findById(5L));
    }

    @Test
    void testFindByIdIdNotFound(){
        Assertions.assertThrows(UnitNotFoundException.class, () -> unitService.findById(11L));
    }


    @Test
    void testFindByIdFromReservation(){
        Assertions.assertThrows(UnitNotFoundException.class, () -> unitService.findById(11L));
    }

    @Test
    void testDelete(){
        unit.setStatus(Status.ACTIVE);
        Assertions.assertEquals(unit, unitService.delete(6L));
    }


    @Test
    void testDeleteIdIdNotFound(){
        Assertions.assertThrows(UnitNotFoundException.class, () -> unitService.delete(11L));
    }


    @Test
    void testListAllMoreThan1(){
        Assertions.assertEquals(units.subList(0, 3), unitService.listAllMoreThan(1L, 1));
        Assertions.assertEquals(units.subList(3, 4), unitService.listAllMoreThan(2L, 1));
        Assertions.assertEquals(units.subList(4, 5), unitService.listAllMoreThan(3L, 1));
    }

    @Test
    void testListAllMoreThan2(){
        Assertions.assertEquals(units.subList(1, 3), unitService.listAllMoreThan(1L, 2));
        Assertions.assertEquals(units.subList(3, 4), unitService.listAllMoreThan(2L, 2));
        Assertions.assertEquals(Collections.emptyList(), unitService.listAllMoreThan(3L, 2));
    }

    @Test
    void testListAllMoreThan3(){
        Assertions.assertEquals(units.subList(1, 3), unitService.listAllMoreThan(1L, 3));
        Assertions.assertEquals(units.subList(3, 4), unitService.listAllMoreThan(2L, 3));
        Assertions.assertEquals(Collections.emptyList(), unitService.listAllMoreThan(3L, 3));
    }

    @Test
    void testListAllMoreThan4(){
        Assertions.assertEquals(units.subList(2, 3), unitService.listAllMoreThan(1L, 4));
        Assertions.assertEquals(units.subList(3, 4), unitService.listAllMoreThan(2L, 4));
        Assertions.assertEquals(Collections.emptyList(), unitService.listAllMoreThan(3L, 4));
    }

    @Test
    void testListAllMoreThan7(){
        Assertions.assertEquals(units.subList(2, 3), unitService.listAllMoreThan(1L, 7));
        Assertions.assertEquals(units.subList(3, 4), unitService.listAllMoreThan(2L, 7));
        Assertions.assertEquals(Collections.emptyList(), unitService.listAllMoreThan(3L, 7));
    }

    @Test
    void testListAllMoreThan8(){
        Assertions.assertEquals(units.subList(2, 3), unitService.listAllMoreThan(1L, 8));
        Assertions.assertEquals(Collections.emptyList(), unitService.listAllMoreThan(2L, 8));
        Assertions.assertEquals(Collections.emptyList(), unitService.listAllMoreThan(3L, 8));
    }

    @Test
    void testListAllMoreThan10(){
        Assertions.assertEquals(Collections.emptyList(), unitService.listAllMoreThan(1L, 10));
        Assertions.assertEquals(Collections.emptyList(), unitService.listAllMoreThan(2L, 10));
        Assertions.assertEquals(Collections.emptyList(), unitService.listAllMoreThan(3L, 10));
    }

}
