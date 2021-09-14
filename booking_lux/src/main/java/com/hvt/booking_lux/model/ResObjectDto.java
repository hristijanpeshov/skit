package com.hvt.booking_lux.model;

public class ResObjectDto {

    private Long id;
    private String name;

    public ResObjectDto(ResObject resObject) {
        this.id = resObject.getId();
        this.name = resObject.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
