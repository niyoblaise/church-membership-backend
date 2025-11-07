package com.willy.Church.controller;


import com.willy.Church.model.SpiritualRecord;
import com.willy.Church.service.SpiritualRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/spiritual-records")

public class SpiritualRecordController {

    private final SpiritualRecordService spiritualRecordService;

    public SpiritualRecordController(SpiritualRecordService spiritualRecordService) {
        this.spiritualRecordService = spiritualRecordService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpiritualRecord> one(@PathVariable Long id) {
        return spiritualRecordService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        spiritualRecordService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/users/{userId}")
    public ResponseEntity<SpiritualRecord> create(@PathVariable Long userId,
                                                  @RequestParam(required = false) LocalDate baptismDate,
                                                  @RequestParam(required = false) String baptismPlace,
                                                  @RequestParam(required = false) LocalDate confirmationDate,
                                                  @RequestParam(required = false) String confirmationPlace) {
        SpiritualRecord saved = spiritualRecordService.createForUser(
                userId, baptismDate, baptismPlace, confirmationDate, confirmationPlace);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<SpiritualRecord> update(@PathVariable Long userId,
                                                  @RequestParam(required = false) LocalDate baptismDate,
                                                  @RequestParam(required = false) String baptismPlace,
                                                  @RequestParam(required = false) LocalDate confirmationDate,
                                                  @RequestParam(required = false) String confirmationPlace) {
        SpiritualRecord updated = spiritualRecordService.updateRecord(
                userId, baptismDate, baptismPlace, confirmationDate, confirmationPlace);
        return ResponseEntity.ok(updated);
    }


    @GetMapping("/users/{userId}")
    public ResponseEntity<SpiritualRecord> byUser(@PathVariable Long userId) {
        return spiritualRecordService.findByUser(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/not-baptised")
    public ResponseEntity<List<SpiritualRecord>> notBaptised() {
        return ResponseEntity.ok(spiritualRecordService.findNotBaptised());
    }

    @GetMapping("/not-confirmed")
    public ResponseEntity<List<SpiritualRecord>> notConfirmed() {
        return ResponseEntity.ok(spiritualRecordService.findNotConfirmed());
    }

    @GetMapping("/baptised-after")
    public ResponseEntity<List<SpiritualRecord>> baptisedAfter(@RequestParam LocalDate date) {
        return ResponseEntity.ok(spiritualRecordService.findBaptisedAfter(date));
    }

    @GetMapping("/confirmed-after")
    public ResponseEntity<List<SpiritualRecord>> confirmedAfter(@RequestParam LocalDate date) {
        return ResponseEntity.ok(spiritualRecordService.findConfirmedAfter(date));
    }

    @GetMapping("/baptism-place")
    public ResponseEntity<List<SpiritualRecord>> byBaptismPlace(@RequestParam String place) {
        return ResponseEntity.ok(spiritualRecordService.findByBaptismPlace(place));
    }

    @GetMapping("/confirmation-place")
    public ResponseEntity<List<SpiritualRecord>> byConfirmationPlace(@RequestParam String place) {
        return ResponseEntity.ok(spiritualRecordService.findByConfirmationPlace(place));
    }

    @GetMapping("/stats/baptised-count")
    public ResponseEntity<Long> baptisedCount() {
        return ResponseEntity.ok(spiritualRecordService.countBaptised());
    }

    @GetMapping("/stats/confirmed-count")
    public ResponseEntity<Long> confirmedCount() {
        return ResponseEntity.ok(spiritualRecordService.countConfirmed());
    }
}