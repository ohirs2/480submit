package com.owen.VaccinationService.controllers;

import com.owen.VaccinationService.entities.Vaccine;
import com.owen.VaccinationService.services.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vaccine")
public class VaccineController {

    @Autowired
    private DatabaseService databaseService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllVaccineData() {
        return ResponseEntity.ok(databaseService.getAllVaccineData());
    }

    @PostMapping("/insert")
    public ResponseEntity<String> insertVaccine(@RequestBody Vaccine vaccine) {
        String name = vaccine.getName();
        String company = vaccine.getCompany();
        int dosesRequired = vaccine.getDosesRequired();
        String description = vaccine.getDescription();

        // Calling the service method to insert the vaccine
        databaseService.insertVaccine(name, company, dosesRequired, description);

        return ResponseEntity.ok("Vaccine inserted successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteVaccine(@PathVariable int id) {
        int rowsAffected = databaseService.deleteVaccineById(id);

        if(rowsAffected > 0) {
            return ResponseEntity.ok("Vaccine deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("No vaccine found with the provided ID.");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateVaccine(@PathVariable int id, @RequestBody Vaccine vaccine) {
        String name = vaccine.getName();
        String company = vaccine.getCompany();
        int dosesRequired = vaccine.getDosesRequired();
        String description = vaccine.getDescription();

        int rowsAffected = databaseService.updateVaccine(id, name, company, dosesRequired, description);
        if (rowsAffected > 0) {
            return ResponseEntity.ok("Vaccine updated successfully.");
        } else {
            return new ResponseEntity<>("Failed to update the vaccine.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
