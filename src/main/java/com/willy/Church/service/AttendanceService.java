package com.willy.Church.service;


import com.willy.Church.dto.AttendanceDto;
import com.willy.Church.dto.AttendanceUpdateDto;
import com.willy.Church.model.Attendance;
import com.willy.Church.model.Event;
import com.willy.Church.repository.AttendanceRepository;
import com.willy.Church.repository.EventRepository;
import com.willy.Church.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;


    public AttendanceService(AttendanceRepository attendanceRepository, EventRepository eventRepository, UserRepository userRepository) {
        this.attendanceRepository = attendanceRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Attendance save(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    @Transactional(readOnly = true)
    public Optional<Attendance> findById(Long id) {
        return attendanceRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<Attendance> findAll(Pageable pageable) {
        return attendanceRepository.findAll(pageable);
    }

    @Transactional
    public void deleteById(Long id) {
        attendanceRepository.deleteById(id);
    }

    @Transactional
    public Attendance checkIn(Long userId, Long eventId) {
        if (attendanceRepository.existsByUserIdAndEventId(userId, eventId)) {
            throw new IllegalStateException("User already checked-in to this event");
        }
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        if (event.getStartTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Cannot check-in to a past event");
        }
        Attendance attendance = new Attendance();
        attendance.setUser(userRepository.getReferenceById(userId));
        attendance.setEvent(event);
        attendance.setTimeStamp(LocalDateTime.now());
        return attendanceRepository.save(attendance);
    }

    @Transactional
    public void cancelCheckIn(Long userId, Long eventId) {
        Attendance attendance = attendanceRepository
                .findByUserIdAndEventId(userId, eventId)
                .orElseThrow(() -> new IllegalArgumentException("Attendance not found"));
        attendanceRepository.delete(attendance);
    }

    @Transactional(readOnly = true)
    public Optional<Attendance> findByUserAndEvent(Long userId, Long eventId) {
        return attendanceRepository.findByUserIdAndEventId(userId, eventId);
    }

    @Transactional(readOnly = true)
    public Page<Attendance> findByEvent(Long eventId, Pageable pageable) {
        return attendanceRepository.findByEventId(eventId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Attendance> findByUser(Long userId, Pageable pageable) {
        return attendanceRepository.findByUserId(userId, pageable);
    }

    @Transactional
    public Attendance updateAttendance(Long id, AttendanceUpdateDto dto) {
        Attendance existing = attendanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Attendance not found"));


        if (dto.getUserId() != null) {
            if (!userRepository.existsById(dto.getUserId())) {
                throw new IllegalArgumentException("User id=" + dto.getUserId() + " not found");
            }
            existing.setUser(userRepository.getReferenceById(dto.getUserId()));
        }

        if (dto.getEventId() != null) {
            if (!eventRepository.existsById(dto.getEventId())) {
                throw new IllegalArgumentException("Event id=" + dto.getEventId() + " not found");
            }
            existing.setEvent(eventRepository.getReferenceById(dto.getEventId()));
        }

        if (dto.getTimeStamp() != null) {
            existing.setTimeStamp(dto.getTimeStamp());
        }

        return attendanceRepository.save(existing);
    }

    @Transactional(readOnly = true)
    public Page<AttendanceDto> findDtoByEvent(Long eventId, Pageable pageable) {
        if (!eventRepository.existsById(eventId))
            throw new IllegalArgumentException("Event id=" + eventId + " not found");
        return attendanceRepository.findDtoByEventId(eventId, pageable);
    }

    @Transactional(readOnly = true)
    public List<Attendance> findBetween(LocalDateTime start, LocalDateTime end) {
        return attendanceRepository.findByTimeStampBetween(start, end);
    }

    @Transactional(readOnly = true)
    public long countByEvent(Long eventId) {
        return attendanceRepository.countByEventId(eventId);
    }

    @Transactional(readOnly = true)
    public long countByUser(Long userId) {
        return attendanceRepository.countByUserId(userId);
    }
}