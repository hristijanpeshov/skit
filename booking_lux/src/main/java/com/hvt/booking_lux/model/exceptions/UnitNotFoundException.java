package com.hvt.booking_lux.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such Unit")
public class UnitNotFoundException extends RuntimeException{

    public UnitNotFoundException(long id) {
        super("Unit with " + id + " was not found!");
    }
}
