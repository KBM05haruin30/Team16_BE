package org.cookieandkakao.babting.domain.calendar.dto.calendarDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record EventListResponseDTO(
    List<EventBriefDTO> events,
    @JsonProperty("has_next")
    boolean hasNext
) {

}
