package com.willy.Church.service;


import com.willy.Church.model.SpiritualRecord;
import com.willy.Church.model.User;
import com.willy.Church.repository.SpiritualRecordRepository;
import com.willy.Church.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SpiritualRecordService {

    private final SpiritualRecordRepository spiritualRecordRepository;
    private final UserRepository userRepository;


    public SpiritualRecordService(SpiritualRecordRepository spiritualRecordRepository, UserRepository userRepository) {
        this.spiritualRecordRepository = spiritualRecordRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public SpiritualRecord save(SpiritualRecord record) {
        return spiritualRecordRepository.save(record);
    }

    @Transactional(readOnly = true)
    public Optional<SpiritualRecord> findById(Long id) {
        return spiritualRecordRepository.findById(id);
    }

    @Transactional
    public void deleteById(Long id) {
        spiritualRecordRepository.deleteById(id);
    }

    @Transactional
    public SpiritualRecord createForUser(Long userId,
                                         LocalDate baptismDate, String baptismPlace,
                                         LocalDate confirmationDate, String confirmationPlace) {
        if (spiritualRecordRepository.existsByUserId(userId)) {
            throw new IllegalStateException("User already has a spiritual record");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        SpiritualRecord record = new SpiritualRecord();
        record.setUser(user);
        record.setBaptismDate(baptismDate);
        record.setBaptismPlace(baptismPlace);
        record.setConfirmationDate(confirmationDate);
        record.setConfirmationPlace(confirmationPlace);
        return spiritualRecordRepository.save(record);
    }

    @Transactional
    public SpiritualRecord updateRecord(Long userId,
                                        LocalDate baptismDate,
                                        String baptismPlace,
                                        LocalDate confirmationDate,
                                        String confirmationPlace) {

        SpiritualRecord r = spiritualRecordRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Spiritual record not found"));

        if (baptismDate != null) r.setBaptismDate(baptismDate);
        if (baptismPlace != null) r.setBaptismPlace(baptismPlace);
        if (confirmationDate != null) r.setConfirmationDate(confirmationDate);
        if (confirmationPlace != null) r.setConfirmationPlace(confirmationPlace);

        return r;
    }

    @Transactional(readOnly = true)
    public Optional<SpiritualRecord> findByUser(Long userId) {
        return spiritualRecordRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<SpiritualRecord> findBaptisedAfter(LocalDate date) {
        return spiritualRecordRepository.findByBaptismDateAfter(date);
    }

    @Transactional(readOnly = true)
    public List<SpiritualRecord> findByBaptismPlace(String place) {
        return spiritualRecordRepository.findByBaptismPlaceContainingIgnoreCase(place);
    }

    @Transactional(readOnly = true)
    public List<SpiritualRecord> findConfirmedAfter(LocalDate date) {
        return spiritualRecordRepository.findByConfirmationDateAfter(date);
    }

    @Transactional(readOnly = true)
    public List<SpiritualRecord> findByConfirmationPlace(String place) {
        return spiritualRecordRepository.findByConfirmationPlaceContainingIgnoreCase(place);
    }

    @Transactional(readOnly = true)
    public List<SpiritualRecord> findNotBaptised() {
        return spiritualRecordRepository.findNotBaptised();
    }

    @Transactional(readOnly = true)
    public List<SpiritualRecord> findNotConfirmed() {
        return spiritualRecordRepository.findNotConfirmed();
    }

    @Transactional(readOnly = true)
    public long countBaptised() {
        return spiritualRecordRepository.countByBaptismDateIsNotNull();
    }

    @Transactional(readOnly = true)
    public long countConfirmed() {
        return spiritualRecordRepository.countByConfirmationDateIsNotNull();
    }
}
