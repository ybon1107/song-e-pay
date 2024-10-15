package com.sepay.backend.reservation.controller;

import com.sepay.backend.reservation.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accommodation")
public class AccommodationController {

    private final AccommodationService accommodationService;

    @GetMapping("")
    public ResponseEntity<?> getAllAccommodation() {
        return ResponseEntity.ok(accommodationService.getAccommodations());
    }
    
    @GetMapping("/{accommodationNo}")
    public ResponseEntity<?> getAccommodation(@PathVariable Integer accommodationNo) {
        log.info("getAccommodation {}", accommodationNo);
        return ResponseEntity.ok(accommodationService.getByNo(accommodationNo));
    }
}
