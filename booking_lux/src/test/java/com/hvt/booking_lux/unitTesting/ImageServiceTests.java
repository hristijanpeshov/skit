package com.hvt.booking_lux.unitTesting;

import com.hvt.booking_lux.model.*;
import com.hvt.booking_lux.model.enumeration.Category;
import com.hvt.booking_lux.model.enumeration.Role;
import com.hvt.booking_lux.repository.ObjectImageRepository;
import com.hvt.booking_lux.repository.UnitImagesRepository;
import com.hvt.booking_lux.service.ImageService;
import com.hvt.booking_lux.service.ReservationObjectService;
import com.hvt.booking_lux.service.UnitService;
import com.hvt.booking_lux.service.impl.ImageServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;


@RunWith(MockitoJUnitRunner.class)
public class ImageServiceTests {

    @Mock
    private ObjectImageRepository objectImageRepository;
    @Mock
    private UnitImagesRepository unitImagesRepository;
    @Mock
    private ReservationObjectService reservationObjectService;
    @Mock
    private UnitService unitService;

    private ImageService imageService;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.imageService = Mockito.spy(new ImageServiceImpl(objectImageRepository,unitImagesRepository,reservationObjectService,unitService));
        User user = new User("username","password","FirstName","LastName",Role.ROLE_ADMIN,"address","phone");
        City city = new City();
        ResObject resObject = new ResObject("Name","Address","Desc", Category.HOTEL,user,city);
        resObject.setObjectImages(new ArrayList<>());
        Unit unit = new Unit(resObject,"title",12,123,"Desc");
        ObjectImage objectImage = new ObjectImage(resObject,"URL");
        Mockito.when(reservationObjectService.findResObjectById(0)).thenReturn(resObject);
        Mockito.when(objectImageRepository.save(Mockito.any(ObjectImage.class))).thenAnswer((Answer) invocation -> invocation.getArguments()[0]);
        Mockito.when(unitService.findById(0)).thenReturn(unit);
        Mockito.when(unitImagesRepository.save(Mockito.any(UnitImages.class))).thenAnswer((Answer) invocation -> invocation.getArguments()[0]);
    }
    @BeforeEach
    public void initEach(){

    }

    @Test
    public void TestAddImageToAccommodation(){
        ObjectImage objectImage = imageService.addImageToResObject(0,"URL");
        Assert.assertEquals("URL",objectImage.getUrl());
    }

    @Test
    public void TestAddImageToUnit(){
        UnitImages unitImages = imageService.addImageToUnit(0,"URL");
        Assert.assertEquals("URL",unitImages.getUrl());
    }
}

