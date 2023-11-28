package com.owen.VaccinationService.controllers;
import com.owen.VaccinationService.entities.VaccinationScheduling;
import com.owen.VaccinationService.services.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vaccinationscheduling")
public class VaccinationSchedulingController {

    @Autowired
    private DatabaseService databaseService;


    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllVaccinationSchedulingData() {
        return ResponseEntity.ok(databaseService.getAllVaccinationSchedulingData());
    }

    @PostMapping("/insert")
    public ResponseEntity<String> insertVaccinationScheduling(@RequestBody VaccinationScheduling vaccinationScheduling) {
        int vaccinationRecordId = vaccinationScheduling.getVaccinationRecordId();
        String timeSlot = vaccinationScheduling.getTimeSlot();

        // Calling the service method to insert the vaccination scheduling
        databaseService.insertVaccinationScheduling(vaccinationRecordId, timeSlot);

        return ResponseEntity.ok("Vaccination scheduling inserted successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteVaccinationScheduling(@PathVariable int id) {
        int rowsAffected = databaseService.deleteVaccinationSchedulingById(id);

        if(rowsAffected > 0) {
            return ResponseEntity.ok("Vaccination scheduling deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("No vaccination scheduling found with the provided ID.");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateVaccinationScheduling(@PathVariable int id, @RequestBody VaccinationScheduling vaccinationScheduling) {
        int vaccinationRecordId = vaccinationScheduling.getVaccinationRecordId();
        String timeSlot = vaccinationScheduling.getTimeSlot();

        int rowsAffected = databaseService.updateVaccinationScheduling(id, vaccinationRecordId, timeSlot);
        if (rowsAffected > 0) {
            return ResponseEntity.ok("Vaccination scheduling updated successfully.");
        } else {
            return new ResponseEntity<>("Failed to update the vaccination scheduling.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
