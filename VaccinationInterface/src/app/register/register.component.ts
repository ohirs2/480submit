import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  patient = {
    firstName: '',
    lastName: '',
    ssn: '',
    age: null,
    gender: '',
    race: '',
    occupationClass: '',
    phoneNumber: '',
    address: '',
    medicalHistory: '',
    username: '',
    password: ''
  };

  credentials: any;
  private apiUrl = 'http://localhost:8080/patient';

  constructor(private http: HttpClient, private router: Router) {}
  
  postRegisterPatients: any = [];
  
  register() {
    if (this.validateInput(this.patient)) {
      const url = `${this.apiUrl}/insert`;
      this.http.post(url, this.patient).subscribe(
        response => {
          console.log('Patient registered successfully', response);
          this.router.navigate(['/login']); 
        },
        error => {
          if(error.status == 200) {


            this.http.get('http://localhost:8080/patient').subscribe(
              response => {
                this.postRegisterPatients = response;

                this.postRegisterPatients.forEach((patient: any) => {
                  if(patient.SSN == this.patient.ssn) {

                    console.log(patient);
                    this.credentials = {
                      patientId: patient.PatientID,
                      username: this.patient.username,
                      passwordHash: this.patient.password
                    }
                    this.http.post('http://localhost:8080/credentials/patient/insert', this.credentials).subscribe(
                      response => {
                      },
                      error => {
                        if(error.status == 200) {
                          alert('successfully registered!')
                        }
                      }
                    )
                  }
                })
              },
              error => {

              }
            )

          }

        }
      );
    } else {
      alert('Please fill in all fields.');
    }
  }

  private validateInput(patient: any): boolean {
    for (const key in patient) {
      if (patient[key] === null || patient[key] === '') {
        return false;
      }
    }
    return true;
  }
}