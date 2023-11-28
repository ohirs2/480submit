import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { AppComponent } from '../app.component';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-patient',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './patient.component.html',
  styleUrl: './patient.component.css'
})
export class PatientComponent implements OnInit {

  patientId: number;
  patientData: any = {
    PatientID: '',
    FirstName: '',
    LastName: '',
    SSN: '',
    Age: '',
    Gender: '',
    Race: '',
    OccupationClass: '',
    PhoneNumber: '',
    Address: '',
    MedicalHistory: ''
  }; 
  availableTimeSlots: any[] = []; // Holds available time slots for vaccination
  scheduledTimes: any[] = []; // Holds the patient's scheduled times
  vaccinationHistory: any[] = []; // Holds the patient's vaccination history

  username: string = '';
  password: string = '';
  updatedAddress: string = '';
  updatedPhoneNumber: string = '';
  ssn: string = '';

  selectedTime: string = ''; 

  vaccineRecords: any = [];
  vaccineRecordsp: any = [];
  vaccineRecordsf: any = [];



  constructor(private http: HttpClient, private appComponent: AppComponent) {
    this.patientId = this.appComponent.patientId;
  }

  ngOnInit(): void {
    this.getPatientData();
    this.getAvailableTimes();
    this.loadVaccinationRecords();
  }

  getPatientData() {
    this.http.get<any>('http://localhost:8080/patient/' + this.patientId).subscribe(
      response => {
        this.patientData = response;
        this.ssn = response?.SSN;
        console.log(response)
      },
      error => {
        console.log(error)
      }
    )
  }

  scheduleVaccination() {
    const vaccinationData = {
      patientId: this.patientId,
      nurseId: 2,
      doseNumber: 2,
      scheduledTime: this.selectedTime,
      status: 'scheduled'
    };

    this.http.post('http://localhost:8080/vaccination-record/insert', vaccinationData)
      .subscribe(
        response => {
          console.log('Vaccination scheduled', response);

        },
        error => console.error('Error scheduling vaccination', error)
      );
  }


  updatePatientInfo() {
    const updatedInfo = {
      firstName: this.patientData.FirstName,
      lastName: this.patientData.LastName,
      age: this.patientData.Age,
      gender: this.patientData.Gender,
      race: this.patientData.Race,
      ssn: this.ssn,
      occupationClass: this.patientData.OccupationClass,
      phoneNumber: this.patientData.PhoneNumber,
      address: this.patientData.Address,
      medicalHistory: this.patientData.MedicalHistory,
    };
    console.log(updatedInfo);
    this.http.put('http://localhost:8080/patient/update/' + this.patientId, updatedInfo)
      .subscribe(
        response => {
          console.log('Patient info updated', response);
        },
        error => console.error('Error updating patient info', error)
      );
  }

  getAvailableTimes() {
    const url = `http://localhost:8080/nurse-scheduling`;
    this.http.get<any>(url).subscribe(
      response => {
        this.availableTimeSlots = response;
    },
    error => {
      console.log(error)
    }
    )
  }

  loadVaccinationRecords() {
    this.http.get<any>('http://localhost:8080/vaccination-record/' + this.patientId).subscribe(
      (response) => {
        this.vaccineRecords = response;
        console.log('test');
        console.log(response);
        this.vaccineRecords.forEach((record: any) => {

          if(this.isDateInPast(record.ScheduledTime)) {
            this.vaccineRecordsp.push(record)
          } else {
            this.vaccineRecordsf.push(record);
          }
        })
      },
      (error) => {
        console.error('Error fetching vaccination records:', error);
      }
    );
  }

  cancelVaccination(vaccinationId: number) {
    this.http.delete(`http://localhost:8080/vaccination-record/delete/${vaccinationId}`)
      .subscribe(
        response => {
          console.log('Vaccination cancelled', response);
          this.vaccineRecordsf = this.vaccineRecordsf.filter((record: any) => record.id !== vaccinationId);
        },
        error => console.error('Error cancelling vaccination', error)
      );
  }

  isDateInPast(dateString: string): boolean {
    const now = new Date();
    const inputDate = new Date(dateString);

    return inputDate < now;
}
  

  

}
