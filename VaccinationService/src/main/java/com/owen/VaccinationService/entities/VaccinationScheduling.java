package com.owen.VaccinationService.entities;

public class VaccinationScheduling {
    private int vaccinationRecordId;
    private String timeSlot;

    public int getVaccinationRecordId() {
        return vaccinationRecordId;
    }

    public void setVaccinationRecordId(int vaccinationRecordId) {
        this.vaccinationRecordId = vaccinationRecordId;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }
}
