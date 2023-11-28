package com.owen.VaccinationService.controllers;

import com.owen.VaccinationService.entities.Nurse;
import com.owen.VaccinationService.entities.Patient;
import com.owen.VaccinationService.entities.Vaccine;
import com.owen.VaccinationService.entities.VaccineInventory;
import com.owen.VaccinationService.services.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/db")
public class DatabaseController {


    @Autowired
    private DatabaseService databaseService;


    @GetMapping("/info")
    public String info() {
        return "Final project for CS 480 - Database Systems";
    }

    @GetMapping("/tables")
    public List<String> getAllTables() {
        return databaseService.getAllTableNames();
    }

    @GetMapping("/schema")
    public Map<String, List<Map<String, Object>>> getDatabaseSchema() {
        return databaseService.getDatabaseSchema();
    }

    @GetMapping("/data")
    public Map<String, List<Map<String, Object>>> getAllData() {
        return databaseService.getAllDataFromAllTables();
    }

    @GetMapping("/clear")
    public String clearData() {
        databaseService.clearAllDataFromTables();
        return "cleared tables";
    }




}
