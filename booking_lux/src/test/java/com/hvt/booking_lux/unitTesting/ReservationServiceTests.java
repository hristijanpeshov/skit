package com.hvt.booking_lux.unitTesting;

import com.hvt.booking_lux.bootstrap.DataHolder;
import com.hvt.booking_lux.model.*;
import com.hvt.booking_lux.model.enumeration.BedType;
import com.hvt.booking_lux.model.enumeration.Category;
import com.hvt.booking_lux.model.enumeration.Role;
import com.hvt.booking_lux.model.exceptions.ReservationNotFoundException;
import com.hvt.booking_lux.model.exceptions.UnitIsReservedException;
import com.hvt.booking_lux.model.statistics.ResObjectMonthlyVisitorCount;
import com.hvt.booking_lux.model.statistics.ResObjectYearStatistics;
import com.hvt.booking_lux.repository.ResObjectRepository;
import com.hvt.booking_lux.repository.ReservationRepository;
import com.hvt.booking_lux.repository.UserRepository;
import com.hvt.booking_lux.service.ReservationService;
import com.hvt.booking_lux.service.UnitService;
import com.hvt.booking_lux.service.impl.ReservationServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ReservationServiceTests {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private UnitService unitService;
    @Mock
    private ResObjectRepository resObjectRepository;

    private ReservationService reservationService;

    @Before
    public void initAll(){
        MockitoAnnotations.initMocks(this);
        reservationService = Mockito.spy(new ReservationServiceImpl(userRepository,reservationRepository,unitService,resObjectRepository));

    }

    @Test
    public void findReservationSuccessfullyTest(){
        User user = new User();
        User user2 = new User();
        City city = new City();
        ResObject resObject = new ResObject("Name","Address","desc", Category.APARTMENT,user,city);
        Unit unit = new Unit(resObject,"Title",12,123,"desc");
        Reservation reservation = new Reservation(user2,unit,123,1, ZonedDateTime.now().minusDays(1),ZonedDateTime.now());
        when(reservationRepository.findById(0L)).thenReturn(java.util.Optional.of(reservation));
        Reservation reservation1 = reservationService.findReservationById(0);
        Assert.assertEquals(reservation,reservation1);
    }

    @Test
    public void failToFindReservationTest(){
        Assert.assertThrows(ReservationNotFoundException.class,()->reservationService.findReservationById(1));
    }

    @Test
    public void checkIfReserved1Test() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        User user = new User();
        User user2 = new User();
        City city = new City();
        ResObject resObject = new ResObject("Name","Address","desc", Category.APARTMENT,user,city);
        Unit unit = new Unit(resObject,"title",123,123,"DEsc");
        Reservation reservation1 = new Reservation(user2,unit,123,12,ZonedDateTime.parse("2020-04-12T00:00:00.000+00:30[Asia/Calcutta]"),ZonedDateTime.parse("2020-04-24T00:00:00.000+00:30[Asia/Calcutta]"));
        Reservation reservation2 = new Reservation(user2,unit,123,12,ZonedDateTime.parse("2020-06-12T00:00:00.000+00:30[Asia/Calcutta]"),ZonedDateTime.parse("2020-06-24T00:00:00.000+00:30[Asia/Calcutta]"));
        List<Reservation> reservations = new ArrayList<>();
        Field field = Unit.class.getDeclaredField("id");
        field.setAccessible(true);
        field.set(unit,1L);
        reservations.add(reservation1);
        reservations.add(reservation2);
        when(reservationRepository.findAll()).thenReturn(reservations);
        Method method = ReservationServiceImpl.class.getDeclaredMethod("checkIfUnitIsReserved",ZonedDateTime.class,ZonedDateTime.class,long.class);
        method.setAccessible(true);
        Assert.assertTrue((Boolean) method.invoke(reservationService,ZonedDateTime.parse("2020-04-08T00:00:00.000+00:30[Asia/Calcutta]"),ZonedDateTime.parse("2020-04-16T00:00:00.000+00:30[Asia/Calcutta]"),1));
    }

    @Test
    public void checkIfReserved2Test() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        User user = new User();
        User user2 = new User();
        City city = new City();
        ResObject resObject = new ResObject("Name","Address","desc", Category.APARTMENT,user,city);
        Unit unit = new Unit(resObject,"title",123,123,"DEsc");
        Reservation reservation1 = new Reservation(user2,unit,123,12,ZonedDateTime.parse("2020-04-12T00:00:00.000+00:30[Asia/Calcutta]"),ZonedDateTime.parse("2020-04-24T00:00:00.000+00:30[Asia/Calcutta]"));
        Reservation reservation2 = new Reservation(user2,unit,123,12,ZonedDateTime.parse("2020-06-12T00:00:00.000+00:30[Asia/Calcutta]"),ZonedDateTime.parse("2020-06-24T00:00:00.000+00:30[Asia/Calcutta]"));
        List<Reservation> reservations = new ArrayList<>();
        Field field = Unit.class.getDeclaredField("id");
        field.setAccessible(true);
        field.set(unit,1L);
        reservations.add(reservation1);
        reservations.add(reservation2);
        when(reservationRepository.findAll()).thenReturn(reservations);
        Method method = ReservationServiceImpl.class.getDeclaredMethod("checkIfUnitIsReserved",ZonedDateTime.class,ZonedDateTime.class,long.class);
        method.setAccessible(true);
        Assert.assertFalse((Boolean) method.invoke(reservationService,ZonedDateTime.parse("2020-04-08T00:00:00.000+00:30[Asia/Calcutta]"),ZonedDateTime.parse("2020-04-11T00:00:00.000+00:30[Asia/Calcutta]"),1));
    }

    @Test
    public void checkIfReserved3Test() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        User user = new User();
        User user2 = new User();
        City city = new City();
        ResObject resObject = new ResObject("Name","Address","desc", Category.APARTMENT,user,city);
        Unit unit = new Unit(resObject,"title",123,123,"DEsc");
        Reservation reservation1 = new Reservation(user2,unit,123,12,ZonedDateTime.parse("2020-04-12T00:00:00.000+00:30[Asia/Calcutta]"),ZonedDateTime.parse("2020-04-24T00:00:00.000+00:30[Asia/Calcutta]"));
        Reservation reservation2 = new Reservation(user2,unit,123,12,ZonedDateTime.parse("2020-06-12T00:00:00.000+00:30[Asia/Calcutta]"),ZonedDateTime.parse("2020-06-24T00:00:00.000+00:30[Asia/Calcutta]"));
        List<Reservation> reservations = new ArrayList<>();
        Field field = Unit.class.getDeclaredField("id");
        field.setAccessible(true);
        field.set(unit,1L);
        reservations.add(reservation1);
        reservations.add(reservation2);
        when(reservationRepository.findAll()).thenReturn(reservations);
        Method method = ReservationServiceImpl.class.getDeclaredMethod("checkIfUnitIsReserved",ZonedDateTime.class,ZonedDateTime.class,long.class);
        method.setAccessible(true);
        Assert.assertTrue((Boolean) method.invoke(reservationService,ZonedDateTime.parse("2020-04-16T00:00:00.000+00:30[Asia/Calcutta]"),ZonedDateTime.parse("2020-04-20T00:00:00.000+00:30[Asia/Calcutta]"),1));
    }

    @Test
    public void checkIfReserved4Test() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        User user = new User();
        User user2 = new User();
        City city = new City();
        ResObject resObject = new ResObject("Name","Address","desc", Category.APARTMENT,user,city);
        Unit unit = new Unit(resObject,"title",123,123,"DEsc");
        Reservation reservation1 = new Reservation(user2,unit,123,12,ZonedDateTime.parse("2020-04-12T00:00:00.000+00:30[Asia/Calcutta]"),ZonedDateTime.parse("2020-04-24T00:00:00.000+00:30[Asia/Calcutta]"));
        Reservation reservation2 = new Reservation(user2,unit,123,12,ZonedDateTime.parse("2020-06-12T00:00:00.000+00:30[Asia/Calcutta]"),ZonedDateTime.parse("2020-06-24T00:00:00.000+00:30[Asia/Calcutta]"));
        List<Reservation> reservations = new ArrayList<>();
        Field field = Unit.class.getDeclaredField("id");
        field.setAccessible(true);
        field.set(unit,1L);
        reservations.add(reservation1);
        reservations.add(reservation2);
        when(reservationRepository.findAll()).thenReturn(reservations);
        Method method = ReservationServiceImpl.class.getDeclaredMethod("checkIfUnitIsReserved",ZonedDateTime.class,ZonedDateTime.class,long.class);
        method.setAccessible(true);
        Assert.assertTrue((Boolean) method.invoke(reservationService,ZonedDateTime.parse("2020-04-20T00:00:00.000+00:30[Asia/Calcutta]"),ZonedDateTime.parse("2020-04-28T00:00:00.000+00:30[Asia/Calcutta]"),1));
    }

    @Test
    public void reserveUnitSuccessTest() throws NoSuchFieldException, IllegalAccessException {
        User user = new User();
        User user2 = new User();
        City city = new City();
        ResObject resObject = new ResObject("Name","Address","desc", Category.APARTMENT,user,city);
        Unit unit = new Unit(resObject,"title",123,123,"DEsc");
        Reservation reservation1 = new Reservation(user2,unit,123,12,ZonedDateTime.parse("2020-04-12T00:00:00.000+00:30[Asia/Calcutta]"),ZonedDateTime.parse("2020-04-24T00:00:00.000+00:30[Asia/Calcutta]"));
        Reservation reservation2 = new Reservation(user2,unit,123,12,ZonedDateTime.parse("2020-06-12T00:00:00.000+00:30[Asia/Calcutta]"),ZonedDateTime.parse("2020-06-24T00:00:00.000+00:30[Asia/Calcutta]"));
        Reservation reservation3 = new Reservation(user2,unit,123,12,ZonedDateTime.parse("2020-08-12T00:00:00.000+00:30[Asia/Calcutta]"),ZonedDateTime.parse("2020-08-24T00:00:00.000+00:30[Asia/Calcutta]"));
        List<Reservation> reservations = new ArrayList<>();
        Field field = Unit.class.getDeclaredField("id");
        field.setAccessible(true);
        field.set(unit,1L);
        reservations.add(reservation1);
        reservations.add(reservation2);
        when(reservationRepository.findAll()).thenReturn(reservations);
        when(reservationRepository.save(Mockito.any(Reservation.class))).then(AdditionalAnswers.returnsFirstArg());
        when(unitService.findById(1L)).thenReturn(unit);
        Reservation actual = reservationService.reserve(user2,1,12,ZonedDateTime.parse("2020-08-12T00:00:00.000+00:30[Asia/Calcutta]"),ZonedDateTime.parse("2020-08-24T00:00:00.000+00:30[Asia/Calcutta]"));
        Assert.assertNotNull(actual);
    }

    @Test
    public void reserveUnitFailedTest() throws NoSuchFieldException, IllegalAccessException {
        User user = new User();
        User user2 = new User();
        City city = new City();
        ResObject resObject = new ResObject("Name","Address","desc", Category.APARTMENT,user,city);
        Unit unit = new Unit(resObject,"title",123,123,"DEsc");
        Reservation reservation1 = new Reservation(user2,unit,123,12,ZonedDateTime.parse("2020-04-12T00:00:00.000+00:30[Asia/Calcutta]"),ZonedDateTime.parse("2020-04-24T00:00:00.000+00:30[Asia/Calcutta]"));
        Reservation reservation2 = new Reservation(user2,unit,123,12,ZonedDateTime.parse("2020-06-12T00:00:00.000+00:30[Asia/Calcutta]"),ZonedDateTime.parse("2020-06-24T00:00:00.000+00:30[Asia/Calcutta]"));
        Reservation reservation3 = new Reservation(user2,unit,123,12,ZonedDateTime.parse("2020-08-12T00:00:00.000+00:30[Asia/Calcutta]"),ZonedDateTime.parse("2020-08-24T00:00:00.000+00:30[Asia/Calcutta]"));
        List<Reservation> reservations = new ArrayList<>();
        Field field = Unit.class.getDeclaredField("id");
        field.setAccessible(true);
        field.set(unit,1L);
        reservations.add(reservation1);
        reservations.add(reservation2);
        when(reservationRepository.findAll()).thenReturn(reservations);
//        when(reservationRepository.save(Mockito.any(Reservation.class))).then(AdditionalAnswers.returnsFirstArg());
        when(unitService.findById(1L)).thenReturn(unit);
        Assert.assertThrows(UnitIsReservedException.class,()->reservationService.reserve(user2,1,12,ZonedDateTime.parse("2020-06-12T00:00:00.000+00:30[Asia/Calcutta]"),ZonedDateTime.parse("2020-06-24T00:00:00.000+00:30[Asia/Calcutta]")));
    }

    @Test
    public void freeResObjectsTest() throws NoSuchFieldException, IllegalAccessException {
        User user = new User();
        User user2 = new User();
        City city = new City();

        ResObject resObject1 = new ResObject("Name1","Address1","desc1", Category.APARTMENT,user,city);
        ResObject resObject2 = new ResObject("Name2","Address1","desc2", Category.APARTMENT,user,city);
        ResObject resObject3 = new ResObject("Name3","Address2","desc3", Category.APARTMENT,user,city);
        List<ResObject> resObjects = new ArrayList<>();
        resObjects.add(resObject1);
        resObjects.add(resObject2);
        resObjects.add(resObject3);
        Long i=1L;
        for(ResObject resObject :resObjects){
            resObject.setId(i++);
            resObject.setUnits(new ArrayList<>());
        }
        List<BedTypes> bedTypes = new ArrayList<>();
        bedTypes.add(new BedTypes(BedType.KING,2));
        Unit unit11 = new Unit(resObject1,"title",123,123,"DEsc",bedTypes);
        Unit unit12 = new Unit(resObject1,"title",123,123,"DEsc",bedTypes);
        Unit unit13 = new Unit(resObject1,"title",123,123,"DEsc",bedTypes);
        Unit unit21 = new Unit(resObject2,"title",123,123,"DEsc",bedTypes);
        Unit unit22 = new Unit(resObject2,"title",123,123,"DEsc",bedTypes);
        Unit unit23 = new Unit(resObject2,"title",123,123,"DEsc",bedTypes);
        Unit unit31 = new Unit(resObject3,"title",123,123,"DEsc",bedTypes);
        Unit unit32 = new Unit(resObject3,"title",123,123,"DEsc",bedTypes);
        Unit unit33 = new Unit(resObject3,"title",123,123,"DEsc",bedTypes);
        List<Unit> units1 = resObject1.getUnits();
        List<Unit> units2 = resObject2.getUnits();
        List<Unit> units3 = resObject3.getUnits();
        units1.add(unit11);
        units1.add(unit12);
        units1.add(unit13);
        units2.add(unit21);
        units2.add(unit22);
        units2.add(unit23);
        units3.add(unit31);
        units3.add(unit32);
        units3.add(unit33);
        List<Unit> units = new ArrayList<>();
        units.add(unit11);
        units.add(unit12);
        units.add(unit13);
        units.add(unit21);
        units.add(unit22);
        units.add(unit23);
        units.add(unit31);
        units.add(unit32);
        units.add(unit33);
        i=1L;
        for(Unit unit : units){
            unit.setId(i++);
        }
        Reservation reservation1 = new Reservation(user2,unit11,123,12,ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"));
        Reservation reservation2 = new Reservation(user2,unit12,123,12,ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"));
        Reservation reservation3 = new Reservation(user2,unit13,123,12,ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"));
        Reservation reservation4 = new Reservation(user2,unit21,123,12,ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"));
        Reservation reservation5 = new Reservation(user2,unit22,123,12,ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"));
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation1);
        reservations.add(reservation2);
        reservations.add(reservation3);
        reservations.add(reservation4);
        reservations.add(reservation5);

        DataHolder.peopleNumberMap.put(BedType.TWIN.toString(), 1);
        DataHolder.peopleNumberMap.put(BedType.DOUBLE.toString(), 2);
        DataHolder.peopleNumberMap.put(BedType.QUEEN.toString(), 2);
        DataHolder.peopleNumberMap.put(BedType.KING.toString(), 2);
        DataHolder.peopleNumberMap.put(BedType.SOFA.toString(), 1);

        Mockito.when(reservationRepository.findAll()).thenReturn(reservations);
        Mockito.when(resObjectRepository.findAll()).thenReturn(resObjects);
        Assert.assertEquals(2,reservationService.findAllResObjectsThatAreNotReservedAtThatTime(ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"),0).stream().count());
    }

    @Test
    public void NoFreeResObjectsTest() throws NoSuchFieldException, IllegalAccessException {
        User user = new User();
        User user2 = new User();
        City city = new City();

        ResObject resObject1 = new ResObject("Name1","Address1","desc1", Category.APARTMENT,user,city);
        ResObject resObject2 = new ResObject("Name2","Address1","desc2", Category.APARTMENT,user,city);
        ResObject resObject3 = new ResObject("Name3","Address2","desc3", Category.APARTMENT,user,city);
        List<ResObject> resObjects = new ArrayList<>();
        resObjects.add(resObject1);
        resObjects.add(resObject2);
        resObjects.add(resObject3);
        Long i=1L;
        for(ResObject resObject :resObjects){
            Field field = ResObject.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(resObject,i);
            Field field2 = ResObject.class.getDeclaredField("units");
            field2.setAccessible(true);
            field2.set(resObject,new ArrayList<>());
            i++;
        }
        List<BedTypes> bedTypes = new ArrayList<>();
        bedTypes.add(new BedTypes(BedType.KING,2));
        Unit unit11 = new Unit(resObject1,"title",123,123,"DEsc",bedTypes);
        Unit unit12 = new Unit(resObject1,"title",123,123,"DEsc",bedTypes);
        Unit unit13 = new Unit(resObject1,"title",123,123,"DEsc",bedTypes);
        Unit unit21 = new Unit(resObject2,"title",123,123,"DEsc",bedTypes);
        Unit unit22 = new Unit(resObject2,"title",123,123,"DEsc",bedTypes);
        Unit unit23 = new Unit(resObject2,"title",123,123,"DEsc",bedTypes);
        Unit unit31 = new Unit(resObject3,"title",123,123,"DEsc",bedTypes);
        Unit unit32 = new Unit(resObject3,"title",123,123,"DEsc",bedTypes);
        Unit unit33 = new Unit(resObject3,"title",123,123,"DEsc",bedTypes);
        List<Unit> units1 = resObject1.getUnits();
        List<Unit> units2 = resObject2.getUnits();
        List<Unit> units3 = resObject3.getUnits();
        units1.add(unit11);
        units1.add(unit12);
        units1.add(unit13);
        units2.add(unit21);
        units2.add(unit22);
        units2.add(unit23);
        units3.add(unit31);
        units3.add(unit32);
        units3.add(unit33);
        List<Unit> units = new ArrayList<>();
        units.add(unit11);
        units.add(unit12);
        units.add(unit13);
        units.add(unit21);
        units.add(unit22);
        units.add(unit23);
        units.add(unit31);
        units.add(unit32);
        units.add(unit33);
        i=1L;
        for(Unit unit : units){
            Field field = Unit.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(unit,i);
            i++;
        }
        Reservation reservation1 = new Reservation(user2,unit11,123,12,ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"));
        Reservation reservation2 = new Reservation(user2,unit12,123,12,ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"));
        Reservation reservation3 = new Reservation(user2,unit13,123,12,ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"));
        Reservation reservation4 = new Reservation(user2,unit21,123,12,ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"));
        Reservation reservation5 = new Reservation(user2,unit22,123,12,ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"));
        Reservation reservation6 = new Reservation(user2,unit23,123,12,ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"));
        Reservation reservation7 = new Reservation(user2,unit31,123,12,ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"));
        Reservation reservation8 = new Reservation(user2,unit32,123,12,ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"));
        Reservation reservation9 = new Reservation(user2,unit33,123,12,ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"));
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation1);
        reservations.add(reservation2);
        reservations.add(reservation3);
        reservations.add(reservation4);
        reservations.add(reservation5);
        reservations.add(reservation6);
        reservations.add(reservation7);
        reservations.add(reservation8);
        reservations.add(reservation9);

        DataHolder.peopleNumberMap.put(BedType.TWIN.toString(), 1);
        DataHolder.peopleNumberMap.put(BedType.DOUBLE.toString(), 2);
        DataHolder.peopleNumberMap.put(BedType.QUEEN.toString(), 2);
        DataHolder.peopleNumberMap.put(BedType.KING.toString(), 2);
        DataHolder.peopleNumberMap.put(BedType.SOFA.toString(), 1);

        Mockito.when(reservationRepository.findAll()).thenReturn(reservations);
        Mockito.when(resObjectRepository.findAll()).thenReturn(resObjects);
        Assert.assertEquals(0,reservationService.findAllResObjectsThatAreNotReservedAtThatTime(ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"),0).stream().count());
    }

    @Test
    public void allFreeResObjectsTest() throws NoSuchFieldException, IllegalAccessException {
        User user = new User();
        User user2 = new User();
        City city = new City();

        ResObject resObject1 = new ResObject("Name1","Address1","desc1", Category.APARTMENT,user,city);
        ResObject resObject2 = new ResObject("Name2","Address1","desc2", Category.APARTMENT,user,city);
        ResObject resObject3 = new ResObject("Name3","Address2","desc3", Category.APARTMENT,user,city);
        List<ResObject> resObjects = new ArrayList<>();
        resObjects.add(resObject1);
        resObjects.add(resObject2);
        resObjects.add(resObject3);
        Long i=1L;
        for(ResObject resObject :resObjects){
            Field field = ResObject.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(resObject,i);
            Field field2 = ResObject.class.getDeclaredField("units");
            field2.setAccessible(true);
            field2.set(resObject,new ArrayList<>());
            i++;
        }
        List<BedTypes> bedTypes = new ArrayList<>();
        bedTypes.add(new BedTypes(BedType.KING,2));
        Unit unit11 = new Unit(resObject1,"title",123,123,"DEsc",bedTypes);
        Unit unit12 = new Unit(resObject1,"title",123,123,"DEsc",bedTypes);
        Unit unit13 = new Unit(resObject1,"title",123,123,"DEsc",bedTypes);
        Unit unit21 = new Unit(resObject2,"title",123,123,"DEsc",bedTypes);
        Unit unit22 = new Unit(resObject2,"title",123,123,"DEsc",bedTypes);
        Unit unit23 = new Unit(resObject2,"title",123,123,"DEsc",bedTypes);
        Unit unit31 = new Unit(resObject3,"title",123,123,"DEsc",bedTypes);
        Unit unit32 = new Unit(resObject3,"title",123,123,"DEsc",bedTypes);
        Unit unit33 = new Unit(resObject3,"title",123,123,"DEsc",bedTypes);
        List<Unit> units1 = resObject1.getUnits();
        List<Unit> units2 = resObject2.getUnits();
        List<Unit> units3 = resObject3.getUnits();
        units1.add(unit11);
        units1.add(unit12);
        units1.add(unit13);
        units2.add(unit21);
        units2.add(unit22);
        units2.add(unit23);
        units3.add(unit31);
        units3.add(unit32);
        units3.add(unit33);
        List<Unit> units = new ArrayList<>();
        units.add(unit11);
        units.add(unit12);
        units.add(unit13);
        units.add(unit21);
        units.add(unit22);
        units.add(unit23);
        units.add(unit31);
        units.add(unit32);
        units.add(unit33);
        i=1L;
        for(Unit unit : units){
            Field field = Unit.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(unit,i);
            i++;
        }
        Reservation reservation1 = new Reservation(user2,unit11,123,12,ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"));
        Reservation reservation2 = new Reservation(user2,unit12,123,12,ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"));
        Reservation reservation3 = new Reservation(user2,unit13,123,12,ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"));
        Reservation reservation4 = new Reservation(user2,unit21,123,12,ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"));
        Reservation reservation5 = new Reservation(user2,unit22,123,12,ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"));
        Reservation reservation6 = new Reservation(user2,unit23,123,12,ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"));
        Reservation reservation7 = new Reservation(user2,unit31,123,12,ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"));
        Reservation reservation8 = new Reservation(user2,unit32,123,12,ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"));
        Reservation reservation9 = new Reservation(user2,unit33,123,12,ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"));
        List<Reservation> reservations = new ArrayList<>();


        DataHolder.peopleNumberMap.put(BedType.TWIN.toString(), 1);
        DataHolder.peopleNumberMap.put(BedType.DOUBLE.toString(), 2);
        DataHolder.peopleNumberMap.put(BedType.QUEEN.toString(), 2);
        DataHolder.peopleNumberMap.put(BedType.KING.toString(), 2);
        DataHolder.peopleNumberMap.put(BedType.SOFA.toString(), 1);

        Mockito.when(reservationRepository.findAll()).thenReturn(reservations);
        Mockito.when(resObjectRepository.findAll()).thenReturn(resObjects);
        Assert.assertEquals(3,reservationService.findAllResObjectsThatAreNotReservedAtThatTime(ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"),0).stream().count());
    }

    @Test
    public void oneFreeResObjectsTest() throws NoSuchFieldException, IllegalAccessException {
        User user = new User();
        User user2 = new User();
        City city = new City();

        ResObject resObject1 = new ResObject("Name1","Address1","desc1", Category.APARTMENT,user,city);
        ResObject resObject2 = new ResObject("Name2","Address1","desc2", Category.APARTMENT,user,city);
        ResObject resObject3 = new ResObject("Name3","Address2","desc3", Category.APARTMENT,user,city);
        List<ResObject> resObjects = new ArrayList<>();
        resObjects.add(resObject1);
        resObjects.add(resObject2);
        resObjects.add(resObject3);
        Long i=1L;
        for(ResObject resObject :resObjects){
            Field field = ResObject.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(resObject,i);
            Field field2 = ResObject.class.getDeclaredField("units");
            field2.setAccessible(true);
            field2.set(resObject,new ArrayList<>());
            i++;
        }
        List<BedTypes> bedTypes = new ArrayList<>();
        bedTypes.add(new BedTypes(BedType.KING,2));
        Unit unit11 = new Unit(resObject1,"title",123,123,"DEsc",bedTypes);
        Unit unit12 = new Unit(resObject1,"title",123,123,"DEsc",bedTypes);
        Unit unit13 = new Unit(resObject1,"title",123,123,"DEsc",bedTypes);
        Unit unit21 = new Unit(resObject2,"title",123,123,"DEsc",bedTypes);
        Unit unit22 = new Unit(resObject2,"title",123,123,"DEsc",bedTypes);
        Unit unit23 = new Unit(resObject2,"title",123,123,"DEsc",bedTypes);
        Unit unit31 = new Unit(resObject3,"title",123,123,"DEsc",bedTypes);
        Unit unit32 = new Unit(resObject3,"title",123,123,"DEsc",bedTypes);
        Unit unit33 = new Unit(resObject3,"title",123,123,"DEsc",bedTypes);
        List<Unit> units1 = resObject1.getUnits();
        List<Unit> units2 = resObject2.getUnits();
        List<Unit> units3 = resObject3.getUnits();
        units1.add(unit11);
        units1.add(unit12);
        units1.add(unit13);
        units2.add(unit21);
        units2.add(unit22);
        units2.add(unit23);
        units3.add(unit31);
        units3.add(unit32);
        units3.add(unit33);
        List<Unit> units = new ArrayList<>();
        units.add(unit11);
        units.add(unit12);
        units.add(unit13);
        units.add(unit21);
        units.add(unit22);
        units.add(unit23);
        units.add(unit31);
        units.add(unit32);
        units.add(unit33);
        i=1L;
        for(Unit unit : units){
            Field field = Unit.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(unit,i);
            i++;
        }
        Reservation reservation1 = new Reservation(user2,unit11,123,12,ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"));
        Reservation reservation2 = new Reservation(user2,unit12,123,12,ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"));
        Reservation reservation3 = new Reservation(user2,unit13,123,12,ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"));
        Reservation reservation4 = new Reservation(user2,unit21,123,12,ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"));
        Reservation reservation5 = new Reservation(user2,unit22,123,12,ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"));
        Reservation reservation6 = new Reservation(user2,unit23,123,12,ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"));
        Reservation reservation7 = new Reservation(user2,unit31,123,12,ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"));
        Reservation reservation8 = new Reservation(user2,unit32,123,12,ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"));
        Reservation reservation9 = new Reservation(user2,unit33,123,12,ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"));
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation1);
        reservations.add(reservation2);
        reservations.add(reservation3);
        reservations.add(reservation4);
        reservations.add(reservation5);
        reservations.add(reservation6);
        reservations.add(reservation7);
        reservations.add(reservation8);

        DataHolder.peopleNumberMap.put(BedType.TWIN.toString(), 1);
        DataHolder.peopleNumberMap.put(BedType.DOUBLE.toString(), 2);
        DataHolder.peopleNumberMap.put(BedType.QUEEN.toString(), 2);
        DataHolder.peopleNumberMap.put(BedType.KING.toString(), 2);
        DataHolder.peopleNumberMap.put(BedType.SOFA.toString(), 1);

        Mockito.when(reservationRepository.findAll()).thenReturn(reservations);
        Mockito.when(resObjectRepository.findAll()).thenReturn(resObjects);
        Assert.assertEquals(1,reservationService.findAllResObjectsThatAreNotReservedAtThatTime(ZonedDateTime.parse("2020-04-12T00:00:00+02:00"),ZonedDateTime.parse("2020-04-24T00:00:00+02:00"),0).stream().count());
    }

    @Test
    public void lastYearIncomeCreator() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        User user = new User("username","username","username","username", Role.ROLE_USER,"username","username");
        User user2 = new User();
        City city = new City();

        ResObject resObject1 = new ResObject("Name1","Address1","desc1", Category.APARTMENT,user,city);
        ResObject resObject2 = new ResObject("Name2","Address1","desc1", Category.APARTMENT,user,city);
        List<ResObject> resObjects = new ArrayList<>();
        resObjects.add(resObject1);
        resObjects.add(resObject2);
        Long i=1L;
        for(ResObject resObject :resObjects){
            Field field = ResObject.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(resObject,i);
            Field field2 = ResObject.class.getDeclaredField("units");
            field2.setAccessible(true);
            field2.set(resObject,new ArrayList<>());
            i++;
        }

        List<ResObjectYearStatistics> list1 = new ArrayList<>();
        ResObjectYearStatistics yearStatistics1 = new ResObjectYearStatistics() {
            @Override
            public long getId() {
                return 1;
            }

            @Override
            public String getName() {
                return "Accommodation1";
            }

            @Override
            public int getMonth() {
                return 1;
            }

            @Override
            public int getNum() {
                return 10;
            }

            @Override
            public int getTotal() {
                return 350;
            }
        };
        list1.add(yearStatistics1);
        ResObjectYearStatistics yearStatistics2 = new ResObjectYearStatistics() {
            @Override
            public long getId() {
                return 1;
            }

            @Override
            public String getName() {
                return "Accommodation1";
            }

            @Override
            public int getMonth() {
                return 2;
            }

            @Override
            public int getNum() {
                return 9;
            }

            @Override
            public int getTotal() {
                return 315;
            }
        };
        list1.add(yearStatistics2);
        List<ResObjectYearStatistics> list2 = new ArrayList<>();
        ResObjectYearStatistics yearStatistics3 = new ResObjectYearStatistics() {
            @Override
            public long getId() {
                return 2;
            }

            @Override
            public String getName() {
                return "Accommodation2";
            }

            @Override
            public int getMonth() {
                return 1;
            }

            @Override
            public int getNum() {
                return 5;
            }

            @Override
            public int getTotal() {
                return 5*50;
            }
        };
        list2.add(yearStatistics3);
        ResObjectYearStatistics yearStatistics4 = new ResObjectYearStatistics() {
            @Override
            public long getId() {
                return 2;
            }

            @Override
            public String getName() {
                return "Accommodation2";
            }

            @Override
            public int getMonth() {
                return 2;
            }

            @Override
            public int getNum() {
                return 7;
            }

            @Override
            public int getTotal() {
                return 7*50;
            }
        };
        list2.add(yearStatistics4);
        Mockito.when(resObjectRepository.findAllByCreator(Mockito.any(User.class))).thenReturn(resObjects);
        Mockito.when(reservationRepository.findAnnualReservationCountForProperty(Mockito.anyString(),Mockito.anyInt(),eq(1L))).thenReturn(list1);
        Mockito.when(reservationRepository.findAnnualReservationCountForProperty(Mockito.anyString(),Mockito.anyInt(),eq(2L))).thenReturn(list2);
        Method method = ReservationServiceImpl.class.getDeclaredMethod("createHashMapWithMonths");
        method.setAccessible(true);
        List<Map<String,String>> map = (List<Map<String,String>>) method.invoke(reservationService);
        for (int j=0;j<12;j++)
        {
            Map<String,String> month = map.get(j);
            month.put("Name1 1".toString(),"0");
            month.put("Name2 2".toString(),"0");
        }
        Map<String,String> month1 = map.get(0);
        month1.put("Name1 1".toString(),String.valueOf(list1.get(0).getTotal()));
        month1.put("Name2 2".toString(),String.valueOf(list2.get(0).getTotal()));
        Map<String,String> month2 = map.get(1);
        month2.put("Name1 1".toString(),String.valueOf(list1.get(1).getTotal()));
        month2.put("Name2 2".toString(),String.valueOf(list2.get(1).getTotal()));
        Assert.assertEquals(map,reservationService.lastYearIncomeForCreatorsAccommodations(user,1900));
    }

    @Test
    public void yearlyVisitorsStatisticsTest() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        User user = new User("username","username","username","username", Role.ROLE_USER,"username","username");
        User user2 = new User();
        City city = new City();

        ResObject resObject1 = new ResObject("Name1","Address1","desc1", Category.APARTMENT,user,city);
        ResObject resObject2 = new ResObject("Name2","Address1","desc1", Category.APARTMENT,user,city);
        List<ResObject> resObjects = new ArrayList<>();
        resObjects.add(resObject1);
        resObjects.add(resObject2);
        Long i=1L;
        for(ResObject resObject :resObjects){
            Field field = ResObject.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(resObject,i);
            Field field2 = ResObject.class.getDeclaredField("units");
            field2.setAccessible(true);
            field2.set(resObject,new ArrayList<>());
            i++;
        }
        List<ResObjectMonthlyVisitorCount> list1 = new ArrayList<>();
        ResObjectMonthlyVisitorCount visitorCount1 = new ResObjectMonthlyVisitorCount() {
            @Override
            public long getId() {
                return 1;
            }

            @Override
            public String getName() {
                return "Name1";
            }

            @Override
            public int getMonth() {
                return 1;
            }

            @Override
            public int getNum() {
                return 10;
            }

            @Override
            public int getTotal() {
                return 10*35;
            }
        };
        list1.add(visitorCount1);
        ResObjectMonthlyVisitorCount visitorCount2 = new ResObjectMonthlyVisitorCount() {
            @Override
            public long getId() {
                return 1;
            }

            @Override
            public String getName() {
                return "Name1";
            }

            @Override
            public int getMonth() {
                return 2;
            }

            @Override
            public int getNum() {
                return 5;
            }

            @Override
            public int getTotal() {
                return 10*35;
            }
        };
        list1.add(visitorCount2);
        List<ResObjectMonthlyVisitorCount> list2 = new ArrayList<>();
        ResObjectMonthlyVisitorCount visitorCount3 = new ResObjectMonthlyVisitorCount() {
            @Override
            public long getId() {
                return 1;
            }

            @Override
            public String getName() {
                return "Name2";
            }

            @Override
            public int getMonth() {
                return 1;
            }

            @Override
            public int getNum() {
                return 20;
            }

            @Override
            public int getTotal() {
                return 10*35;
            }
        };
        list2.add(visitorCount3);
        ResObjectMonthlyVisitorCount visitorCount4 = new ResObjectMonthlyVisitorCount() {
            @Override
            public long getId() {
                return 1;
            }

            @Override
            public String getName() {
                return "Name2";
            }

            @Override
            public int getMonth() {
                return 2;
            }

            @Override
            public int getNum() {
                return 30;
            }

            @Override
            public int getTotal() {
                return 10*35;
            }
        };
        list2.add(visitorCount4);
        Mockito.when(resObjectRepository.findAllByCreator(Mockito.any(User.class))).thenReturn(resObjects);
        Mockito.when(reservationRepository.findMonthlyVisitors(Mockito.anyString(),Mockito.anyInt(),eq(1L))).thenReturn(list1);
        Mockito.when(reservationRepository.findMonthlyVisitors(Mockito.anyString(),Mockito.anyInt(),eq(2L))).thenReturn(list2);
        Method method = ReservationServiceImpl.class.getDeclaredMethod("createHashMapWithMonths");
        method.setAccessible(true);
        List<Map<String,String>> map = (List<Map<String,String>>) method.invoke(reservationService);
        for (int j=0;j<12;j++)
        {
            Map<String,String> month = map.get(j);
            month.put("Name1 1".toString(),"0");
            month.put("Name2 2".toString(),"0");
        }
        Map<String,String> month1 = map.get(0);
        month1.put("Name1 1".toString(),String.valueOf(list1.get(0).getNum()));
        month1.put("Name2 2".toString(),String.valueOf(list2.get(0).getNum()));
        Map<String,String> month2 = map.get(1);
        month2.put("Name1 1".toString(),String.valueOf(list1.get(1).getNum()));
        month2.put("Name2 2".toString(),String.valueOf(list2.get(1).getNum()));
        Assert.assertEquals(map,reservationService.yearlyVisitorsStatistic(user,1900));
    }


}
