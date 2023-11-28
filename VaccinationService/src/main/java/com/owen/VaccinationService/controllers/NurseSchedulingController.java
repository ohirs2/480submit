package com.owen.VaccinationService.controllers;
import com.owen.VaccinationService.entities.NurseScheduling;
import com.owen.VaccinationService.services.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/nurse-scheduling")
public class NurseSchedulingController {

    @Autowired
    private DatabaseService databaseService;


    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllNurseSchedulingData() {
        return ResponseEntity.ok(databaseService.getAllNurseSchedulingData());
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Map<String, Object>>> getSchedulingByNurseId(@PathVariable String id) {
        return ResponseEntity.ok(databaseService.getNurseScheduling(Integer.parseInt(id)));
    }

    @PostMapping("/insert")
    public ResponseEntity<String> insertNurseScheduling(@RequestBody NurseScheduling nurseScheduling) {
        int nurseId = nurseScheduling.getNurseId();
        String timeSlot = nurseScheduling.getTimeSlot();

        // Calling the service method to insert the nurse scheduling
        databaseService.insertNurseScheduling(nurseId, timeSlot);

        return ResponseEntity.ok("Nurse scheduling inserted successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteNurseScheduling(@PathVariable int id) {
        int rowsAffected = databaseService.deleteNurseSchedulingById(id);

        if(rowsAffected > 0) {
            return ResponseEntity.ok("Nurse scheduling deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("No nurse scheduling found with the provided ID.");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateNurseScheduling(@PathVariable int id, @RequestBody NurseScheduling nurseScheduling) {
        int nurseId = nurseScheduling.getNurseId();
        String timeSlot = nurseScheduling.getTimeSlot();
        int rowsAffected = databaseService.updateNurseScheduling(id, nurseId, timeSlot);
        if (rowsAffected > 0) {
            return ResponseEntity.ok("Nurse scheduling updated successfully.");
        } else {
            return new ResponseEntity<>("Failed to update nurse scheduling.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
