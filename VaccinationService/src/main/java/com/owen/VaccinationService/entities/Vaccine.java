package com.owen.VaccinationService.entities;

public class Vaccine {
    private String name;
    private String company;
    private int dosesRequired;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getDosesRequired() {
        return dosesRequired;
    }

    public void setDosesRequired(int dosesRequired) {
        this.dosesRequired = dosesRequired;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
