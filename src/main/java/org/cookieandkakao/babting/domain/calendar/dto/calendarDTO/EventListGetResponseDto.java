package org.cookieandkakao.babting.domain.calendar.dto.calendarDTO;

import java.util.List;

public record EventListGetResponseDto(

    int status,

    String message,

    List<EventGetResponseDto> events
) {

}
