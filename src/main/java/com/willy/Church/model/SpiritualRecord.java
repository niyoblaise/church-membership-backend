package com.willy.Church.model;


import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "spiritual_records")
public class SpiritualRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate baptismDate;
    private String baptismPlace;

    private LocalDate confirmationDate;
    private String confirmationPlace;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public SpiritualRecord() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getBaptismDate() {
        return baptismDate;
    }

    public void setBaptismDate(LocalDate baptismDate) {
        this.baptismDate = baptismDate;
    }

    public String getBaptismPlace() {
        return baptismPlace;
    }

    public void setBaptismPlace(String baptismPlace) {
        this.baptismPlace = baptismPlace;
    }

    public LocalDate getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(LocalDate confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public String getConfirmationPlace() {
        return confirmationPlace;
    }

    public void setConfirmationPlace(String confirmationPlace) {
        this.confirmationPlace = confirmationPlace;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}