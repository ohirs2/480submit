import { Routes } from '@angular/router';
import { AdminComponent } from './admin/admin.component';
import { NurseComponent } from './nurse/nurse.component';
import { PatientComponent } from './patient/patient.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';

export const routes: Routes = [
    { path: 'admin', component: AdminComponent },
  { path: 'nurse', component: NurseComponent },
  { path: 'patient', component: PatientComponent },
  { path: 'register', component: RegisterComponent },
  { path: '', component: LoginComponent}
];