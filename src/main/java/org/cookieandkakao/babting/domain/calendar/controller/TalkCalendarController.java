package org.cookieandkakao.babting.domain.calendar.controller;

import java.util.Collections;
import java.util.List;
import org.cookieandkakao.babting.domain.calendar.dto.calendarDTO.CalendarResponseBodyDTO;
import org.cookieandkakao.babting.domain.calendar.dto.calendarDTO.EventBriefDTO;
import org.cookieandkakao.babting.domain.calendar.dto.calendarDTO.EventListResponseDTO;
import org.cookieandkakao.babting.domain.calendar.service.TalkCalendarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calendar")
public class TalkCalendarController {

    private final TalkCalendarService calendarService;

    public TalkCalendarController(TalkCalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("/events")
    public ResponseEntity<CalendarResponseBodyDTO> getEvents(
        @RequestHeader(value = "Authorization") String authorizationHeader,
        @RequestParam(value = "from") String from,
        @RequestParam(value = "to") String to
    ) {
        String accessToken = authorizationHeader.replace("Bearer ", "");
        System.out.println("Access Token: " + accessToken);
        try {
           CalendarResponseBodyDTO events = calendarService.getEvents(accessToken, from, to);
            if (events.events().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new CalendarResponseBodyDTO(
                        204,
                        "조회된 일정이 없습니다.",
                        events.events(),
                        false
                    ));
            }
            CalendarResponseBodyDTO responseBody = new CalendarResponseBodyDTO(
                200,
                "조회 성공",
                events.events(),
                events.hasNext()
            );
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CalendarResponseBodyDTO(
                    500,
                    "일정 조회 중 오류가 발생했습니다.",
                    Collections.emptyList(),
                    false
                ));
        }
    }

}
