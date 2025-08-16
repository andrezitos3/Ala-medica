import { Routes } from '@angular/router';
import { PacientesComponent } from './pages/pacientes/pacientes.component';
import { CadastroComponent } from './pages/cadastro/cadastro.component';

export const routes: Routes = [
    {
        path: '',
        redirectTo: 'pacientes',
        pathMatch: 'full'
    },
    {
        path: 'pacientes',
        component: PacientesComponent
    },
    {
        path: 'cadastro',
        component: CadastroComponent
    }
];
