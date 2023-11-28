package com.owen.VaccinationService.controllers;

import com.owen.VaccinationService.entities.Patient;
import com.owen.VaccinationService.services.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private DatabaseService databaseService;

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPersonData(@PathVariable String id) {
        return ResponseEntity.ok(databaseService.getPatient(Integer.parseInt(id)));
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllPatientData() {
        return ResponseEntity.ok(databaseService.getAllPatientData());
    }

    @PostMapping("/insert")
    public ResponseEntity<String> insertPatient(@RequestBody Patient patient) {
        String firstName = patient.getFirstName();
        String lastName = patient.getLastName();
        String ssn = patient.getSsn();
        int age = patient.getAge();
        String gender = patient.getGender();
        String race = patient.getRace();
        String occupationClass = patient.getOccupationClass();
        String phoneNumber = patient.getPhoneNumber();
        String address = patient.getAddress();
        String medicalHistory = patient.getMedicalHistory();

        // Calling the service method to insert the patient
        databaseService.insertPatient(firstName, lastName, ssn, age, gender, race, occupationClass, phoneNumber, address, medicalHistory);

        return ResponseEntity.ok("Patient inserted successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable String id) {
        int rowsAffected = databaseService.deletePatientById(id);

        if(rowsAffected > 0) {
            return ResponseEntity.ok("Patient deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("No patient found with the provided SSN.");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updatePatient(@PathVariable String id, @RequestBody Patient patient) {
        String firstName = patient.getFirstName();
        String lastName = patient.getLastName();
        String ssn = patient.getSsn();
        int age = patient.getAge();
        String gender = patient.getGender();
        String race = patient.getRace();
        String occupationClass = patient.getOccupationClass();
        String phoneNumber = patient.getPhoneNumber();
        String address = patient.getAddress();
        String medicalHistory = patient.getMedicalHistory();
        int rowsAffected = databaseService.updatePatient(id, ssn, firstName, lastName, age, gender, race, occupationClass, phoneNumber, address, medicalHistory);
        if (rowsAffected > 0) {
            return ResponseEntity.ok("Patient updated successfully.");
        } else {
            return new ResponseEntity<>("Failed to update the patient.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
