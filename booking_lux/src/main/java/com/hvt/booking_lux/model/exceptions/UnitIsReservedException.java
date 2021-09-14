package com.hvt.booking_lux.model.exceptions;

public class UnitIsReservedException extends RuntimeException {

    public UnitIsReservedException(long id) {
        super("This unit is reserved for these dates");
    }
}
