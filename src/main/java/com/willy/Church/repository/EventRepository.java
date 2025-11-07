package com.willy.Church.repository;

import com.willy.Church.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    boolean existsByTitleAndStartTime(String title, LocalDateTime startTime);
    Optional<Event> findByTitle(String title);
    List<Event> findByLocationId(Long locationId);
    Page<Event> findByStartTimeBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
    Page<Event> findByStartTimeAfter(LocalDateTime now, Pageable pageable);
    Page<Event> findByEndTimeBefore(LocalDateTime now, Pageable pageable);
    Page<Event> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
    long countByLocationId(Long locationId);

    boolean existsByLocationId(Long locationId);
}
