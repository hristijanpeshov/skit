package com.hvt.booking_lux.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class City {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Country country;

    private String name;

    public City() {
    }

    public City(String name, Country country){
        this.name = name;
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return Objects.equals(country, city.country) && Objects.equals(name, city.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, name);
    }

    public Long getId() {
        return id;
    }

    public Country getCountry() {
        return country;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

