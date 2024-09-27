package org.cookieandkakao.babting.domain.calendar.dto.calendarDTO;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.cookieandkakao.babting.domain.calendar.entity.Time;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TimeGetResponseDto(

    String startAt,

    String endAt,

    String timeZone,

    boolean allDay
) {
    public Time toEntity() {
        LocalDateTime startAtLocalDateTime = convertToLocalDateTime(startAt);
        LocalDateTime endAtLocalDateTime = convertToLocalDateTime(endAt);
        return new Time(startAtLocalDateTime, endAtLocalDateTime, this.timeZone, this.allDay);
    }

    private LocalDateTime convertToLocalDateTime(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX");
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(time, formatter);
        LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
        return localDateTime;
    }
}
