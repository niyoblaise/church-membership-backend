package com.willy.Church.repository;


import com.willy.Church.dto.AttendanceDto;
import com.willy.Church.model.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    boolean existsByUserIdAndEventId(Long userId, Long eventId);
    Optional<Attendance> findByUserIdAndEventId(Long userId, Long eventId);
    Page<Attendance> findByEventId(Long eventId, Pageable pageable);
    Page<Attendance> findByUserId(Long userId, Pageable pageable);
    List<Attendance> findByTimeStampBetween(LocalDateTime start, LocalDateTime end);
    long countByEventId(Long eventId);
    long countByUserId(Long userId);

    @Query("""
    SELECT NEW com.willy.Church.dto.AttendanceDto(
           a.id,
           CONCAT(a.user.firstName, ' ', a.user.lastName),
           a.event.title,
           a.timeStamp)
    FROM Attendance a
    WHERE a.event.id = :eventId
""")
    Page<AttendanceDto> findDtoByEventId(@Param("eventId") Long eventId, Pageable pageable);
}