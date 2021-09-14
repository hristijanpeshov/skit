package com.hvt.booking_lux.model;

import com.hvt.booking_lux.model.enumeration.Sentiment;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Reservation reservation;

    @Column(length = 2000)
    private String comment;

    @Enumerated(EnumType.STRING)
    private Sentiment sentiment;

    @ManyToOne
    private User user;

    public Review() {
    }

    public Review(Reservation reservation, String comment, User user) {
        this.reservation = reservation;
        this.comment = comment;
        this.user = user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public String getComment() {
        return comment;
    }

    public User getUser() {
        return user;
    }

    public Sentiment getSentiment() {
        return sentiment;
    }

    public void setSentiment(Sentiment sentiment) {
        this.sentiment = sentiment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(reservation, review.reservation) && Objects.equals(comment, review.comment) && sentiment == review.sentiment && Objects.equals(user, review.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservation, comment, sentiment, user);
    }
}
