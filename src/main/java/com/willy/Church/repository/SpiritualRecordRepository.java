package com.willy.Church.repository;
import com.willy.Church.model.SpiritualRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpiritualRecordRepository extends JpaRepository<SpiritualRecord, Long> {

    boolean existsByUserId(Long userId);
    Optional<SpiritualRecord> findByUserId(Long userId);
    List<SpiritualRecord> findByBaptismDateAfter(LocalDate date);
    List<SpiritualRecord> findByBaptismPlaceContainingIgnoreCase(String place);
    List<SpiritualRecord> findByConfirmationDateAfter(LocalDate date);
    List<SpiritualRecord> findByConfirmationPlaceContainingIgnoreCase(String place);

    @Query("SELECT sr FROM SpiritualRecord sr WHERE sr.baptismDate IS NULL")
    List<SpiritualRecord> findNotBaptised();

    @Query("SELECT sr FROM SpiritualRecord sr WHERE sr.confirmationDate IS NULL")
    List<SpiritualRecord> findNotConfirmed();

    long countByBaptismDateIsNotNull();
    long countByConfirmationDateIsNotNull();
}