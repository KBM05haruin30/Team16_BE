package org.cookieandkakao.babting.domain.calendar.dto.calendarDTO;

import java.util.List;

public record CalendarResponseBodyDTO(
    int status,
    String message,
    List<EventBriefDTO> events,
    boolean hasNext
) {

}
