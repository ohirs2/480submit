import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css'
})
export class AdminComponent implements OnInit {
  ngOnInit(): void {


    this.http.get<any>('http://localhost:8080/nurse').subscribe(
      (response) => {
        this.nurses = response;
        
        this.loadNurseCredentials();
      },
      (error) => {
        console.error('Error fetching data:', error);
      }
    );


    this.http.get<any>('http://localhost:8080/patient').subscribe(
      (response) => {
        this.patients = response; 
        this.loadPatientCredentials();
      },
      (error) => {
        console.error('Error fetching data:', error);
      }
    );

    this.loadVaccinesAndInventories();
    this.loadVaccinationRecords();
    this.loadAvailableTimes();
 
  }


  currentCredential: any;
  nurses: any[] = [];
  patients: any[] = [];
  nurseCredentials: any[] = [];
  patientCredentials: any[] = [];
  vaccines: any[] = [];
  vaccineInventories: any[] = [];
  vaccineRecords: any[] = [];
  
  vaccineRecordsf: any[] = [];

  vaccineRecordsp: any[] = [];

  vaccineSchedulings: any[] = [];
  searchPatientId: string = '';
  searchNurseId: string = '';
  filteredSchedules: any[] = [];

  availableTimes: any = [];

  constructor(private http: HttpClient, private cd: ChangeDetectorRef) {}

  loadVaccines() {
    this.http.get<any>('http://localhost:8080/vaccine').subscribe(
      (response) => {
        this.vaccines = response;
      },
      (error) => {
        console.error('Error fetching vaccines:', error);
      }
    );
  }

  loadVaccinationRecords() {
    this.http.get<any>('http://localhost:8080/vaccination-record').subscribe(
      (response) => {
        console.log(response )
        this.vaccineRecords = response;

        this.vaccineRecords.forEach((record) => {
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

  loadAvailableTimes() {
    this.http.get('http://localhost:8080/nurse-scheduling').subscribe(
      response => {
        console.log(response);
        this.availableTimes = response
      },
      error => {
        console.log(error);
      }
    )
  }

  isDateInPast(dateString: string): boolean {
    const now = new Date();
    const inputDate = new Date(dateString);

    return inputDate < now;
}

  filterSchedules() {
    this.filteredSchedules = this.vaccineSchedulings.filter(schedule => {
      return (this.searchPatientId ? schedule.patientId.toString().includes(this.searchPatientId) : true) &&
             (this.searchNurseId ? schedule.nurseId.toString().includes(this.searchNurseId) : true);
    });
  }

  loadVaccineInventories(): void {
    this.http.get<any>('http://localhost:8080/vaccineinventory').subscribe(
      (response) => {
        this.vaccineInventories = response;
      },
      (error) => {
        console.error('Error fetching vaccine inventories:', error);
      }
    );
  }

  loadVaccinesAndInventories() {
    this.http.get<any>('http://localhost:8080/vaccine').subscribe(
      (vaccineResponse) => {
        this.vaccines = vaccineResponse;
        this.http.get<any>('http://localhost:8080/vaccineinventory').subscribe(
          (inventoryResponse) => {
            this.vaccineInventories = inventoryResponse;
            this.mergeVaccinesWithInventories();
          },
          (inventoryError) => {
            console.error('Error fetching vaccine inventories:', inventoryError);
          }
        );
      },
      (vaccineError) => {
        console.error('Error fetching vaccines:', vaccineError);
      }
    );
  }

  mergeVaccinesWithInventories() {
    this.vaccines = this.vaccines.map(vaccine => {
      const inventoryRecord = this.vaccineInventories.find(inv => inv.VaccineID === vaccine.VaccineID);
      if (inventoryRecord) {
        console.log(inventoryRecord)
        return { ...vaccine, QuantityAvailable: inventoryRecord.QuantityAvailable,
                             QuantityOnHold: inventoryRecord.QuantityOnHold,
                             VaccineInventoryID: inventoryRecord.VaccineInventoryID
        };
      }
      return vaccine;
    });
  }

  addVaccine(): void {
    const newVaccine = {
      Name: '',
      Company: '',
      DosesRequired: null,
      Description: '',
      VaccineID: null, 
      QuantityAvailable: null,
      isEditMode: true  
    };
    this.vaccines.push(newVaccine);
    this.cd.detectChanges();
  }


  setEdit(vaccine: any) {
    vaccine.isEditMode = !vaccine.isEditMode;
  }
  

  loadNurseCredentials() {
    this.http.get<any>('http://localhost:8080/credentials/nurses/all').subscribe(
      (response) => {
        this.nurseCredentials = response;
        this.nurses.forEach(nurse => {
          const credential = this.nurseCredentials.find(c => c.NurseID === nurse.NurseID);
          nurse.username = credential ? credential.Username : '';
          nurse.password = credential ? credential.PasswordHash : ''; 
          
        });

      },
      (error) => console.error('Error fetching nurse credentials:', error)
    );

  }

  loadPatientCredentials() {
    this.http.get<any>('http://localhost:8080/credentials/patients/all').subscribe(
      (response) => {
        console.log(response)
        this.patientCredentials = response;
        this.patients.forEach(patient => {
          const credential = this.patientCredentials.find(c => c.PatientID === patient.PatientID);
          patient.username = credential ? credential.Username : '';
          patient.password = credential ? credential.PasswordHash : ''; 
        });

      },
      (error) => console.error('Error fetching nurse credentials:', error)
    );

  }

  toggleEditNurse(nurse: any): void {
    nurse.isEditMode = !nurse.isEditMode;
    if (!nurse.isEditMode) {
      this.saveNurse(nurse);
      this.saveNurseCredentials(nurse);
    }
  }

  toggleEditVaccine(vaccine: any): void {

    vaccine.isEditMode = !vaccine.isEditMode;

    
    if(!vaccine.isEditMode) {
      this.http.post("http://localhost:8080/vaccine/insert", vaccine).subscribe(
        response => {
          console.log(response)
        },
        error => {
          console.log(error);
        }
      )
    }


    let inventory = {
      vaccineId: vaccine.VaccineID,
      quantityAvailable: vaccine.QuantityAvailable,
      quantityOnHold: vaccine.QuantityOnHold
    }



    if (!vaccine.isEditMode) {
      this.http.put('http://localhost:8080/vaccineinventory/update/' + vaccine.VaccineInventoryID, inventory).subscribe({
        next: (response) => {
          console.log('Credentials created successfully');
        },
        error: (error) => this.http.put('http://localhost:8080/vaccineinventory/update/' + vaccine.VaccineInventoryID, inventory).subscribe()
      });
    }
  }

  saveNurseCredentials(nurse: any): void {
    const credentialObj = {
      nurseId: nurse.NurseID,
      username: nurse.username,
      passwordHash: nurse.password
    };

    

    if (this.isNewCredential(nurse)) {
      this.createNurseCredentials(credentialObj);
    } else {
      this.updateNurseCredentials(credentialObj);
    }
  }

  isNewCredential(nurse: any): boolean {
    return !this.nurseCredentials.some(c => c.NurseID === nurse.NurseID);
  }

  createNurseCredentials(credential: any): void {
    console.log(credential)
    this.http.post('http://localhost:8080/credentials/nurse/insert', credential).subscribe({
      next: (response) => {
        console.log('Credentials created successfully');
      },
      error: (error) => console.error('Error creating credentials:', error)
    });
  }

  updateNurseCredentials(credential: any): void {
    this.http.put(`http://localhost:8080/credentials/nurse/update/${credential.nurseId}`, credential).subscribe({
      next: (response) => {
        console.log('Credentials updated successfully');
      },
      error: (error) => console.error('Error updating credentials:', error)
    });
  }



  credentialByID(nurseID: Number) {
    if(this.nurseCredentials.find((nurseCredential) => {
      nurseCredential.NurseID == nurseID
    }) != null) {
      return this.nurseCredentials.find((nurseCredential) => {
        nurseCredential.NurseID == nurseID
      })
    } else {
      return {
        Username: 'undefined',
        Password: 'undefined'
      }
    }
  }

  saveNurse(nurse: any): void {

    let obj = {
      firstName: nurse.FirstName,
      middleInitial: nurse.MiddleInitial,
      lastName: nurse.LastName,
      employeeID: nurse.EmployeeID,
      gender: nurse.Gender,
      phoneNumber: nurse.PhoneNumber,
      address: nurse.Address
    }



    if(nurse.NurseID) {
      this.http.put('http://localhost:8080/nurse/update/' + nurse.NurseID, obj).subscribe({
        next: (response) => {
          const credentialObj = {
            nurseId: nurse.NurseID,
            username: nurse.username,
            passwordHash: nurse.password
          };
          this.http.post('http://localhost:8080/credentials/nurse/insert', credentialObj).subscribe({
            next: (response) => {
              console.log('Credentials created successfully');
            }
          });
        },
        error: (error) => {
        }
      });
    } else {
      this.http.post('http://localhost:8080/nurse/insert', obj).subscribe({
        next: (response) => {
        },
        error: (error) => {
        }
      });
    }
  }

  addNurse(): void {
    const newNurse = {
      FirstName: '',
      MiddleInitial: '',
      LastName: '',
      EmployeeID: '',
      Gender: '',
      PhoneNumber: '',
      Address: '',
      isEditMode: true 
    };
    this.nurses.push(newNurse);
  }

  deleteNurse(index: number): void {
    const nurseToDelete = this.nurses[index];
    if (nurseToDelete.NurseID) {
      this.http.delete('http://localhost:8080/nurse/delete/' + nurseToDelete.NurseID).subscribe({
        next: (response) => {
        },
        error: (error) => {
          this.nurses = [
            ...this.nurses.slice(0, index),
            ...this.nurses.slice(index + 1)
          ];
          this.http.delete('http://localhost:8080/credentials/nurse/delete/' + nurseToDelete.NurseID).subscribe({
            next: (response) => {
              console.log('Credentials deleted successfully');
            },
            error: (error) => console.error('Error deleting credentials:', error)
          });
          this.cd.detectChanges();
        }
      });
    } else {
      this.nurses.splice(index, 1);
    }
  }


}
