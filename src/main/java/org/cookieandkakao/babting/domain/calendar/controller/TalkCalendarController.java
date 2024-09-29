package org.cookieandkakao.babting.domain.calendar.controller;

import java.util.Collections;
import org.cookieandkakao.babting.domain.calendar.dto.response.EventGetResponseDto;
import org.cookieandkakao.babting.domain.calendar.dto.response.EventListGetResponseDto;
import org.cookieandkakao.babting.domain.calendar.dto.request.EventListGetRequestDto;
import org.cookieandkakao.babting.domain.calendar.service.EventService;
import org.cookieandkakao.babting.domain.calendar.service.TalkCalendarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calendar")
public class TalkCalendarController {

    private final TalkCalendarService talkCalendarService;
    private final EventService eventService;

    public TalkCalendarController(TalkCalendarService talkCalendarService, EventService eventService) {
        this.talkCalendarService = talkCalendarService;
        this.eventService = eventService;
    }

    @GetMapping("/events")
    public ResponseEntity<EventListGetResponseDto> getEventList(
        @RequestHeader(value = "Authorization") String authorizationHeader,
        @RequestBody EventListGetRequestDto eventListRequestDTO,
        @RequestParam Long memberId
    ) {
        String accessToken = authorizationHeader.replace("Bearer ", "");
        try {
            String from = eventListRequestDTO.from();
            String to = eventListRequestDTO.to();

            EventListGetResponseDto eventList = talkCalendarService.getEventList(accessToken, from, to);

            for (EventGetResponseDto event : eventList.events()) {
                eventService.saveEvent(event, memberId);  // memberId를 사용해 저장
            }

            HttpStatus status = HttpStatus.OK;
            String message = "일정 목록을 조회했습니다.";

            if (eventList.events().isEmpty()) {
                status = HttpStatus.NO_CONTENT;
                message = "조회된 일정이 없습니다.";
            }

            EventListGetResponseDto responseBody = new EventListGetResponseDto(
                status.value(),
                message,
                eventList.events(),
                eventList.hasNext()
            );

            return ResponseEntity.status(status).body(responseBody);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new EventListGetResponseDto(
                    500,
                    "일정 조회 중 오류가 발생했습니다.",
                    Collections.emptyList(),
                    false
                ));
        }
    }
}
