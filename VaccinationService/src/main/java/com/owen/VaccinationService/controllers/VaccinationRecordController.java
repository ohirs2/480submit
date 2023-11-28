package com.owen.VaccinationService.controllers;
import com.owen.VaccinationService.entities.VaccinationRecord;
import com.owen.VaccinationService.services.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vaccination-record")
public class VaccinationRecordController {

    @Autowired
    private DatabaseService databaseService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllVaccinationRecordData() {
        return ResponseEntity.ok(databaseService.getAllVaccinationRecordData());
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Map<String, Object>>> getVaccinationRecordsByPatientID(@PathVariable String id) {
        return ResponseEntity.ok(databaseService.getVaccinationRecords(id));
    }

    @PostMapping("/insert")
    public ResponseEntity<String> insertVaccinationRecord(@RequestBody VaccinationRecord vaccinationRecord) {
        int patientId = vaccinationRecord.getPatientId();
        int vaccineId = vaccinationRecord.getVaccineId();
        int doseNumber = vaccinationRecord.getDoseNumber();
        String scheduledTime = vaccinationRecord.getScheduledTime();
        int nurseId = vaccinationRecord.getNurseId();
        String status = vaccinationRecord.getStatus();

        // Calling the service method to insert the vaccination record
        databaseService.insertVaccinationRecord(patientId, vaccineId, doseNumber, scheduledTime, nurseId, status);

        return ResponseEntity.ok("Vaccination record inserted successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteVaccinationRecord(@PathVariable int id) {
        int rowsAffected = databaseService.deleteVaccinationRecordById(id);

        if(rowsAffected > 0) {
            return ResponseEntity.ok("Vaccination record deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("No vaccination record found with the provided ID.");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateVaccinationRecord(@PathVariable int id, @RequestBody VaccinationRecord vaccinationRecord) {
        int patientId = vaccinationRecord.getPatientId();
        int vaccineId = vaccinationRecord.getVaccineId();
        int doseNumber = vaccinationRecord.getDoseNumber();
        String scheduledTime = vaccinationRecord.getScheduledTime();
        int nurseId = vaccinationRecord.getNurseId();
        String status = vaccinationRecord.getStatus();

        int rowsAffected = databaseService.updateVaccinationRecord(id, patientId, vaccineId, doseNumber, scheduledTime, nurseId, status);
        if (rowsAffected > 0) {
            return ResponseEntity.ok("Vaccination record updated successfully.");
        } else {
            return new ResponseEntity<>("Failed to update the vaccination record.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
