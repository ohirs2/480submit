package com.owen.VaccinationService.controllers;

import com.owen.VaccinationService.entities.Nurse;
import com.owen.VaccinationService.services.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/nurse")
public class NurseController {

    @Autowired
    private DatabaseService databaseService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllNurseData() {
        return ResponseEntity.ok(databaseService.getAllNurseData());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getNurseData(@PathVariable String id) {
        return ResponseEntity.ok(databaseService.getNurse(Integer.parseInt(id)));
    }

    @PostMapping("/insert")
    public ResponseEntity<String> insertNurse(@RequestBody Nurse nurse) {
        String firstName = nurse.getFirstName();
        String middleInitial = nurse.getMiddleInitial();
        String lastName = nurse.getLastName();
        String employeeID = nurse.getEmployeeID();
        String gender = nurse.getGender();
        String phoneNumber = nurse.getPhoneNumber();
        String address = nurse.getAddress();

        // Calling the service method to insert the nurse
        databaseService.insertNurse(firstName, middleInitial, lastName, employeeID, gender, phoneNumber, address);

        return ResponseEntity.ok("Nurse inserted successfully");
    }

    @DeleteMapping("/delete/{id}")
    public String deleteNurse(@PathVariable String id) {
        // Removed the redundant declaration of nurseId since it's already being passed as a method parameter
        int rowsAffected = databaseService.deleteNurseById(id);

        if(rowsAffected > 0) {
            System.out.println("Nurse deleted successfully.");
            return "Nurse deleted successfully."; // Return a response
        } else {
            System.out.println("No nurse found with the provided ID.");
            return "No nurse found with the provided ID."; // Return a response
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateNurse(@PathVariable String id, @RequestBody Nurse nurse) {
        // Extract the nurse information from the request body
        String firstName = nurse.getFirstName();
        String middleInitial = nurse.getMiddleInitial();
        String lastName = nurse.getLastName();
        String employeeID = nurse.getEmployeeID();
        String gender = nurse.getGender();
        String phoneNumber = nurse.getPhoneNumber();
        String address = nurse.getAddress();


        // Check if the nurse exists before updating
        List<Map<String, Object>> nurseData = databaseService.getAllNurseData();
        boolean nurseExists = nurseData.stream().anyMatch(n -> n.get("NurseID").toString().equals(id.toString()));

        if (!nurseExists) {
            return new ResponseEntity<>("Nurse with ID " + id + " not found.", HttpStatus.NOT_FOUND);
        }

        // Calling the service method to update the nurse
        int rowsAffected = databaseService.updateNurse(id, employeeID, firstName, middleInitial, lastName, gender, phoneNumber, address);
        if (rowsAffected > 0) {
            return ResponseEntity.ok("Nurse updated successfully.");
        } else {
            return new ResponseEntity<>("Failed to update the nurse.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
