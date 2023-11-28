package com.owen.VaccinationService.controllers;

import com.owen.VaccinationService.entities.Credential;
import com.owen.VaccinationService.entities.NurseCredentials;
import com.owen.VaccinationService.entities.PatientCredentials;
import com.owen.VaccinationService.services.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/credentials")
public class CredentialsController {

    @Autowired
    private DatabaseService databaseService;

    @PostMapping("/nurse/login")
    public ResponseEntity<List<Map<String, Object>>> nurseLogin(@RequestBody Credential credentials) {
        return ResponseEntity.ok(databaseService.getMatchingNurseCredentials(credentials.getUsername(), credentials.getPassword()));
    }

    @PostMapping("/patient/login")
    public ResponseEntity<List<Map<String, Object>>> patientLogin(@RequestBody Credential credentials) {
        return ResponseEntity.ok(databaseService.getMatchingPatientCredentials(credentials.getUsername(), credentials.getPassword()));
    }

    @GetMapping("/nurses/all")
    public ResponseEntity<List<Map<String, Object>>> getAllNurseCredentials() {
        return ResponseEntity.ok(databaseService.getAllNurseCredentials());
    }

    @GetMapping("/patients/all")
    public ResponseEntity<List<Map<String, Object>>> getAllPatientCredentials() {
        return ResponseEntity.ok(databaseService.getAllPatientCredentials());
    }

    // Nurse Credentials Endpoints

    @GetMapping("/nurse/{nurseId}")
    public ResponseEntity<?> getNurseCredentials(@PathVariable int nurseId) {
        var credentials = databaseService.getNurseCredentials(nurseId);
        if (credentials != null) {
            return ResponseEntity.ok(credentials);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/nurse/insert")
    public ResponseEntity<String> insertNurseCredentials(@RequestBody NurseCredentials credentials) {
        databaseService.insertNurseCredentials(credentials.getNurseId(), credentials.getUsername(), credentials.getPasswordHash());
        return ResponseEntity.ok("Nurse credentials inserted successfully");
    }

    @PutMapping("/nurse/update/{nurseId}")
    public ResponseEntity<String> updateNurseCredentials(@PathVariable int nurseId, @RequestBody NurseCredentials credentials) {
        int rowsAffected = databaseService.updateNurseCredentials(nurseId, credentials.getUsername(), credentials.getPasswordHash());
        if (rowsAffected > 0) {
            return ResponseEntity.ok("Nurse credentials updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to update nurse credentials");
        }
    }
    @DeleteMapping("/nurse/delete/{nurseId}")
    public ResponseEntity<String> deleteNurseCredentials(@PathVariable int nurseId) {
        databaseService.deleteNurseCredentials(nurseId);
        return ResponseEntity.ok("Nurse credentials deleted successfully");
    }

    // Patient Credentials Endpoints
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getPatientCredentials(@PathVariable int patientId) {
        var credentials = databaseService.getPatientCredentials(patientId);
        if (credentials != null) {
            return ResponseEntity.ok(credentials);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/patient/insert")
    public ResponseEntity<String> insertPatientCredentials(@RequestBody PatientCredentials credentials) {
        databaseService.insertPatientCredentials(credentials.getPatientId(), credentials.getUsername(), credentials.getPasswordHash());
        return ResponseEntity.ok("Patient credentials inserted successfully");
    }

    @PutMapping("/patient/update/{patientId}")
    public ResponseEntity<String> updatePatientCredentials(@PathVariable int patientId, @RequestBody PatientCredentials credentials) {
        int rowsAffected = databaseService.updatePatientCredentials(patientId, credentials.getUsername(), credentials.getPasswordHash());
        if (rowsAffected > 0) {
            return ResponseEntity.ok("Patient credentials updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to update patient credentials");
        }
    }

    @DeleteMapping("/patient/delete/{patientId}")
    public ResponseEntity<String> deletePatientCredentials(@PathVariable int patientId) {
        databaseService.deletePatientCredentials(patientId);
        return ResponseEntity.ok("Patient credentials deleted successfully");
    }
}
