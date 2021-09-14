package com.hvt.booking_lux.unitTesting;

import com.hvt.booking_lux.model.City;
import com.hvt.booking_lux.model.Country;
import com.hvt.booking_lux.model.exceptions.CountryNotFoundException;
import com.hvt.booking_lux.repository.CityRepository;
import com.hvt.booking_lux.repository.CountryRepository;
import com.hvt.booking_lux.service.impl.LocationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class LocationServiceTests {


    @Mock
    private CityRepository cityRepository;

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private LocationServiceImpl locationService;


    Country macedonia = new Country("Macedonia", "MKD", "flag");
    Country serbia = new Country("Serbia", "SRB", "flag");
    Country england = new Country("England", "ENG", "flag");
    Country belgium = new Country("Belgium", "BEL", "flag");

    List<City> cities = List.of(
            new City("Skopje", macedonia),
            new City("Bitola", macedonia),
            new City("Belgrade", serbia),
            new City("London", england));


    @BeforeEach
    void setUp() {
        Mockito.when(cityRepository.findAll())
                .thenReturn(cities);

        Mockito.when(cityRepository.findAllByCountry(macedonia))
                .thenReturn(
                        List.of(
                                new City("Skopje", macedonia),
                                new City("Bitola", macedonia)));

        Mockito.when(cityRepository.findAllByCountry(serbia))
                .thenReturn(
                        List.of(
                                new City("Belgrade", serbia)));


        Mockito.when(cityRepository.findAllByCountry(england))
                .thenReturn(
                        List.of(
                                new City("London", england)));

        Mockito.when(cityRepository.findAllByCountry(belgium))
                .thenReturn(Collections.emptyList());

        Mockito.when(countryRepository.findById(1L)).thenReturn(Optional.of(macedonia));
        Mockito.when(countryRepository.findById(2L)).thenReturn(Optional.of(serbia));
        Mockito.when(countryRepository.findById(3L)).thenReturn(Optional.of(england));
        Mockito.when(countryRepository.findById(4L)).thenReturn(Optional.of(belgium));

        Mockito.when(countryRepository.findAll()).thenReturn(List.of(macedonia, serbia, england, belgium));
    }


    @Test
    void testListAllCitiesByCountry() {
        Assertions.assertEquals(
                cities.subList(0, 2),
                locationService.listAllCities(1));

        Assertions.assertEquals(
                cities.subList(2, 3),
                locationService.listAllCities(2));

        Assertions.assertEquals(
                cities.subList(3, 4),
                locationService.listAllCities(3));

        Assertions.assertEquals(
                Collections.emptyList(),
                locationService.listAllCities(4));
    }

    @Test
    void testListAllCitiesByCountryIdNotFound() {
        Assertions.assertThrows(CountryNotFoundException.class,
                () -> locationService.listAllCities(11));
    }

    @Test
    void testListAllCountries() {
        Assertions.assertEquals(List.of(macedonia, serbia, england, belgium),
                locationService.listAllCountries());
    }


    @Test
    void testListAllCities() {
        Assertions.assertEquals(cities,
                locationService.listAllCities());
    }


    @Test
    void testListAllCityNames() {
        Assertions.assertEquals(List.of("Skopje", "Bitola", "Belgrade", "London"),
                locationService.listAllCityNames());
    }


}
