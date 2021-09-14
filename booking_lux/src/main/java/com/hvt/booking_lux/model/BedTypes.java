package com.hvt.booking_lux.model;

import com.hvt.booking_lux.model.enumeration.BedType;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class BedTypes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    private BedType bedType;

    int count;

    public BedTypes() {
    }

    public BedTypes(BedType bedType, int count) {
        this.bedType = bedType;
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public BedType getBedType() {
        return bedType;
    }

    public int getCount() {
        return count;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BedTypes bedTypes = (BedTypes) o;
        return count == bedTypes.count && bedType == bedTypes.bedType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bedType, count);
    }
}
