package org.project.models;

import javax.persistence.*;

@Entity
@Table(name = "note_app")
public class Note {

    public Note(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;
    private String carBrand;
    private String carModel;
    private String maintenanceWork;
    private Double maintenanceCost;

    public Note(String date, String carBrand, String carModel, String maintenanceWork, Double maintenanceCost, User user) {
        this.user = user;
        this.date = date;
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.maintenanceWork = maintenanceWork;
        this.maintenanceCost = maintenanceCost;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Note() {
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getMaintenanceWork() {
        return maintenanceWork;
    }

    public void setMaintenanceWork(String maintenanceWork) {
        this.maintenanceWork = maintenanceWork;
    }

    public Double getMaintenanceCost() {
        return maintenanceCost;
    }

    public void setMaintenanceCost(Double maintenanceCost) {
        this.maintenanceCost = maintenanceCost;
    }
}