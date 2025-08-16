import { Routes } from '@angular/router';
import { PacientesComponent } from './pages/pacientes/pacientes.component';

export const routes: Routes = [
    {
        path: '',
        redirectTo: 'pacientes',
        pathMatch: 'full'
    },
    {
        path: 'pacientes',
        component: PacientesComponent
    }
];
