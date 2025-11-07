package com.willy.Church.controller;

import com.willy.Church.dto.AttendanceDto;
import com.willy.Church.dto.AttendanceUpdateDto;
import com.willy.Church.model.Attendance;
import com.willy.Church.service.AttendanceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")

public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Attendance> one(@PathVariable Long id) {
        return attendanceService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        attendanceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/check-in")
    public ResponseEntity<Attendance> checkIn(@RequestParam Long userId,
                                              @RequestParam Long eventId) {
        Attendance saved = attendanceService.checkIn(userId, eventId);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/cancel-check-in")
    public ResponseEntity<Void> cancelCheckIn(@RequestParam Long userId,
                                              @RequestParam Long eventId) {
        attendanceService.cancelCheckIn(userId, eventId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(params = "eventId")
    public ResponseEntity<Page<AttendanceDto>> byEvent(
            @RequestParam Long eventId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(attendanceService.findDtoByEvent(eventId, pageable));
    }

    @GetMapping(params = "userId")
    public ResponseEntity<Page<Attendance>> byUser(
            @RequestParam Long userId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(attendanceService.findByUser(userId, pageable));
    }

    @GetMapping(params = {"userId", "eventId"})
    public ResponseEntity<Attendance> byUserAndEvent(@RequestParam Long userId,
                                                     @RequestParam Long eventId) {
        return attendanceService.findByUserAndEvent(userId, eventId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/between")
    public ResponseEntity<List<Attendance>> between(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(attendanceService.findBetween(from, to));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Attendance> update(@PathVariable Long id,
                                             @RequestBody AttendanceUpdateDto dto) {
        Attendance updated = attendanceService.updateAttendance(id, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/count-by-event")
    public ResponseEntity<Long> countByEvent(@RequestParam Long eventId) {
        return ResponseEntity.ok(attendanceService.countByEvent(eventId));
    }

    @GetMapping("/count-by-user")
    public ResponseEntity<Long> countByUser(@RequestParam Long userId) {
        return ResponseEntity.ok(attendanceService.countByUser(userId));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<Attendance>> all(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(attendanceService.findAll(pageable));
    }
}