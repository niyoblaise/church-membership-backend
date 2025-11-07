package com.willy.Church.dto;

import java.time.LocalDateTime;

public record AttendanceDto(
        Long id,
        String userName,
        String eventTitle,
        LocalDateTime timeStamp) {

    public AttendanceDto {}
}