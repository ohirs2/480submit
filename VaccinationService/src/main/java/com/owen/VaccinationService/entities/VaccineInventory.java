package com.owen.VaccinationService.entities;

public class VaccineInventory {
    public int getVaccineId() {
        return vaccineId;
    }

    public void setVaccineId(int vaccineId) {
        this.vaccineId = vaccineId;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public int getQuantityOnHold() {
        return quantityOnHold;
    }

    public void setQuantityOnHold(int quantityOnHold) {
        this.quantityOnHold = quantityOnHold;
    }

    private int vaccineId;
    private int quantityAvailable;
    private int quantityOnHold;
}
