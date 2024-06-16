import { Component } from '@angular/core';

import { Routes } from '@angular/router';

import { AppComponent } from './app.component';
import { SignupComponent } from './pages/signup/signup.component';
import { LoginComponent } from './pages/login/login.component';
import { HomeComponent } from './pages/home/home.component';
import { ProviderComponent } from './pages/provider/provider.component';
import { AdminComponent } from './pages/admin/admin.component';
import { UserProfileComponent } from './pages/user-profile/user-profile.component';
import { MensajesComponent } from './pages/mensajes/mensajes.component';
import { NotificacionesComponent } from './pages/notificaciones/notificaciones.component';
import { ClientComponent } from './pages/client/client.component';
import { ProductosComponent } from './pages/productos/productos.component';
import { PedidosComponent } from './pages/pedidos/pedidos.component';

export const routes: Routes = [

  /*  VISTAS DE USUARIOS */

  { path: '', component: LoginComponent, pathMatch: 'full' },
  { path: 'login', component: LoginComponent, pathMatch: 'full' },
  { path: 'signup', component: SignupComponent, pathMatch: 'full' },
  { path: ':username', component: UserProfileComponent, pathMatch: 'full'},

  /*  VISTAS DE ADMINISTRADOR */

  { path: ':username/admin', component: AdminComponent, pathMatch: 'full' },
  
  /*  VISTAS DE USUARIOS */ 

  { path: ':username/home', component: HomeComponent, pathMatch: 'full' },
  { path: ':username/home/mensajes', component: MensajesComponent, pathMatch: 'full' },
  { path: ':username/home/notificaciones', component: NotificacionesComponent, pathMatch: 'full' },
  { path: ':username/home/clientes', component: ClientComponent, pathMatch: 'full' },
  { path: ':username/home/proveedores', component: ProviderComponent, pathMatch: 'full' },
  { path: ':username/home/productos', component: ProductosComponent, pathMatch: 'full' },
  { path: ':username/home/pedidos', component: PedidosComponent, pathMatch: 'full' },
  { path: ':username/config', component: PedidosComponent, pathMatch: 'full' },

  
];
