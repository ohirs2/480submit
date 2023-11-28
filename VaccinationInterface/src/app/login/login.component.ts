import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AppComponent } from '../app.component';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  role: string = 'nurse';
  credential: any;

  constructor(private http: HttpClient, private router: Router, private appComponent: AppComponent) {}

  login() {
    const url = `http://localhost:8080/credentials/${this.role}/login`;

    this.credential = {
      username: this.username,
      password: this.password
    }

    console.log(this.credential)

    this.http.post<any>(url, this.credential).subscribe(
      response => {
        if(response.length != 0) {
          console.log(response[0].NurseID)
          this.appComponent.patientId = response[0].PatientID;
          this.appComponent.nurseId = response[0].NurseID;
          this.router.navigate([`/${this.role}`]);
        } else if (this.role == 'patient') {
          if (confirm('No account found for this patient. Would you like to register?')) {
            this.router.navigate(['/register']);
          }
        }
      },
      error => {
        console.error('Login failed', error);
      }
    );
  }
}