package com.hvt.booking_lux.model;

import com.hvt.booking_lux.bootstrap.DataHolder;
import com.hvt.booking_lux.model.enumeration.BedType;
import com.hvt.booking_lux.model.enumeration.Status;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
//@Where(clause = "status='ACTIVE'")
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private double size;

//    private boolean status;

    private double price;


    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany
    private List<BedTypes> bedTypes;

    @Column(length = 2000)
    private String description;

    @ManyToOne
    private ResObject resObject;

    @ElementCollection
    private List<String> unitImages;

    @OneToMany(mappedBy = "unit")
    private List<Reservation> reservations;

    public Unit() {
    }

    public Unit(ResObject resObject,String title, double size, double price, String description) {
        this.resObject = resObject;
        this.size = size;
        this.price = price;
        this.description = description;
        this.status = Status.ACTIVE;
        this.title = title;
        this.bedTypes = new ArrayList<>();
        unitImages = new ArrayList<>();
    }

    public Unit(ResObject resObject,String title, double size, double price, String description, List<BedTypes> bedTypes) {
        this.resObject = resObject;
        this.size = size;
        this.price = price;
        this.description = description;
        this.status = Status.ACTIVE;
        this.bedTypes = bedTypes;
        this.title = title;
        unitImages = new ArrayList<>();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public List<BedTypes> getBedTypes() {
        return bedTypes;
    }

    public void setBedTypes(List<BedTypes> bedTypes) {
        this.bedTypes = bedTypes;
    }

    public Long getId() {
        return id;
    }

    public double getSize() {
        return size;
    }

//    public boolean isStatus() {
//        return status;
//    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public ResObject getResObject() {
        return resObject;
    }

    public int getNumberOfPeople() {
        return bedTypes.stream().mapToInt(s-> DataHolder.peopleNumberMap.get(s.getBedType().toString()) * s.getCount()).sum();
    }

    public String getBedsAsString(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0 ; i<bedTypes.size(); i++){
            if(i == bedTypes.size()-1){
                sb.append(bedTypes.get(i).getBedType());
            }
            else{
                sb.append(bedTypes.get(i).getBedType()).append(", ");

            }
        }
        return sb.toString();
    }

    public boolean hasReviews(){
        return !this.getReservations().stream().allMatch(r -> r.getReview() == null);
    }
    public List<String> getUnitImages() {
        return unitImages;
    }

    public void setSize(double size) {
        this.size = size;
    }

//    public void setStatus(boolean status) {
//        this.status = status;
//    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setResObject(ResObject resObject) {
        this.resObject = resObject;
    }

    public void setUnitImages(List<String> unitImages) {
        this.unitImages = unitImages;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unit unit = (Unit) o;
        return Double.compare(unit.size, size) == 0 && Double.compare(unit.price, price) == 0 && Objects.equals(title, unit.title) && status == unit.status && Objects.equals(bedTypes, unit.bedTypes) && Objects.equals(description, unit.description) && Objects.equals(resObject, unit.resObject);
    }
}
