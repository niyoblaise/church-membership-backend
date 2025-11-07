package com.willy.Church.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AttendanceUpdateDto {
    private Long userId;
    private Long eventId;
    private LocalDateTime timeStamp;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}