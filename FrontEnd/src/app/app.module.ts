import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { PacientesComponent } from './pages/pacientes/pacientes.component';
import { CadastroComponent } from './pages/cadastro/cadastro.component';
import { CommonModule } from '@angular/common';
import { EditarComponent } from './pages/editar/editar.component';

@NgModule({
  declarations: [
    AppComponent,
    SidebarComponent,
    PacientesComponent,
    CadastroComponent,
    EditarComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    CommonModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
