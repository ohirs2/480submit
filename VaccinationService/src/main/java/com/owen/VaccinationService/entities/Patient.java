package com.owen.VaccinationService.entities;

public class Patient {
    private String firstName;
    private String lastName;
    private String ssn;
    private int age;
    private String gender;
    private String race;
    private String occupationClass;
    private String phoneNumber;
    private String address;
    private String medicalHistory;

    // Getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSsn() {
        return ssn;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getRace() {
        return race;
    }

    public String getOccupationClass() {
        return occupationClass;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    // Setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public void setOccupationClass(String occupationClass) {
        this.occupationClass = occupationClass;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }
}
