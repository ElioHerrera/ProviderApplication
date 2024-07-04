package com.provider.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;


@RestController
@RequestMapping("")
public class StarController {

     @GetMapping("")
    public ResponseEntity<?> star() {
         return ResponseEntity.ok(Collections.singletonMap("status", "ok"));
    }
}
