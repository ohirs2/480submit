import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DataService } from '../data.service';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AppComponent } from '../app.component';

@Component({
  selector: 'app-nurse',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './nurse.component.html',
  styleUrl: './nurse.component.css'
})
export class NurseComponent implements OnInit {

  id: number = -1;
  nurse: any;

  vaccineId: string = '';
  patientId: string = '';
  doseNumber: string = '';
  inputTimeSlot: any = {};
  status: string = '';
  

  newTimeSlot: string = '';
  selectedTimeSlot: any = {};
  scheduledTimes: any[] = [];
  private apiUrl = 'http://localhost:8080'; 

  constructor(private http: HttpClient, private appComponent: AppComponent) {}
  ngOnInit(): void {
    this.id = this.appComponent.nurseId;
    this.getNurseInfo();
    this.getScheduledTimes()
  }

  getNurseInfo() {
    const url = `${this.apiUrl}/nurse/${this.id}`;

    this.http.get<any>(url).subscribe(
      response => {
        this.nurse = {
          address: response.Address,
          middleInitial: response.MiddleInitial,
          gender: response.Gender,
          employeeID: response.EmployeeID,
          firstName: response.FirstName,
          lastName: response.LastName,
          phoneNumber: response.PhoneNumber,
          nurseId: this.id
        }

        console.log(this.nurse)
      },
      error => {
      }
    );
  }


  getScheduledTimes() {
    const url = `${this.apiUrl}/nurse-scheduling/${this.id}`;
    this.http.get<any>(url).subscribe(
      response => {


        console.log(response)
        response.forEach( (time: any) => {
          this.scheduledTimes.push({
            timeSlot: time.TimeSlot,
            nurseSchedulingId: time.NurseSchedulingID 
          })
        })
      
    },
    error => {
      console.log(error)
    }
    )

  }

  updateNurseInfo() {
    const url = `${this.apiUrl}/nurse/update/${this.id}`;
    this.http.put(url, this.nurse).subscribe(
      response => {
        console.log('Information updated', response);
      },
      error => {
        console.error('Update failed', error);
      }
    );
  }

  scheduleTime() {
    console.log('scheduling')

    if(this.newTimeSlot != '') {

    const url = `${this.apiUrl}/nurse-scheduling/insert`;
    this.http.post(url, { nurseId: this.id, timeSlot: this.newTimeSlot }).subscribe(
      response => {
        this.scheduledTimes.push({timeSlot: this.newTimeSlot}); 
      },
      error => {
        console.log(error)
        this.scheduledTimes.push({timeSlot: this.newTimeSlot}); 
      }
    );

    }
  }

  cancelTime() {
    console.log(this.selectedTimeSlot)
    const url = `${this.apiUrl}/nurse-scheduling/delete/${this.selectedTimeSlot.nurseSchedulingId}`;
    this.http.delete(url).subscribe(
      response => {
        console.log('Time cancelled', response);
      },
      error => {
        
        this.scheduledTimes = this.scheduledTimes.filter(t => t !== this.selectedTimeSlot);
        this.selectedTimeSlot = ''; 
      }
    );
  }

  recordVaccination() {
    const vaccinationRecord = {
      patientId: this.patientId,
      vaccineId: this.vaccineId,
      doseNumber: this.doseNumber,
      scheduledTime: this.inputTimeSlot.timeSlot,
      nurseId: this.id,
      status: this.status
    };
    console.log(vaccinationRecord)
    const url = `${this.apiUrl}/vaccination-record/insert`;
    this.http.post(url, vaccinationRecord).subscribe(
      response => {
        console.log('Vaccination recorded', response);
      },
      error => {
        console.error('Recording failed', error);
        if(error.status == 200) {
          alert("added vaccination record");
          this.patientId = '';
          this.vaccineId = '';
          this.doseNumber = '';
          this.inputTimeSlot = {};
          this.status = '';
        }
      }
    );
  }

}