package com.provider.controller;

import com.provider.other.Consola;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("")
public class StarController {

     @GetMapping("")
     public ResponseEntity<?> star() {
         Consola.aletaVerde("PROVIDER","Ingreso a la aplicación","Pruebas de desarrollo");
         return ResponseEntity.ok(Collections.singletonMap("status", "ok"));
     }
}
