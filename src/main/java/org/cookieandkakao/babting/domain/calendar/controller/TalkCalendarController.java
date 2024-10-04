package org.cookieandkakao.babting.domain.calendar.controller;

import java.util.ArrayList;
import java.util.List;
import org.cookieandkakao.babting.common.apiresponse.ApiResponseBody.SuccessBody;
import org.cookieandkakao.babting.common.apiresponse.ApiResponseGenerator;
import org.cookieandkakao.babting.domain.calendar.dto.request.EventCreateRequestDto;
import org.cookieandkakao.babting.domain.calendar.dto.response.EventCreateResponseDto;
import org.cookieandkakao.babting.domain.calendar.dto.response.EventDetailGetResponseDto;
import org.cookieandkakao.babting.domain.calendar.dto.response.EventGetResponseDto;
import org.cookieandkakao.babting.domain.calendar.dto.response.EventListGetResponseDto;
import org.cookieandkakao.babting.domain.calendar.service.EventService;
import org.cookieandkakao.babting.domain.calendar.service.TalkCalendarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<SuccessBody<EventListGetResponseDto>> getEventList(
        @RequestHeader(value = "Authorization") String authorizationHeader,
        @RequestParam String from,
        @RequestParam String to,
        @RequestParam Long memberId
    ) {
        String accessToken = authorizationHeader.replace("Bearer ", "");

        /*EventListGetResponseDto eventList = talkCalendarService.getEventList(accessToken, from, to);

        List<EventGetResponseDto> updatedEvents = new ArrayList<>();

        for (EventGetResponseDto event : eventList.events()) {
            if (event.id() != null) {
                event = talkCalendarService.getEvent(accessToken, event.id()).event();
                updatedEvents.add(event);
            } else {
                updatedEvents.add(event);
            }
        }

        eventList = new EventListGetResponseDto(updatedEvents);*/

        // updatedEvents를 캐시에서 가져오기
        List<EventGetResponseDto> updatedEvents = talkCalendarService.getUpdatedEventList(accessToken, from, to, memberId);
        EventListGetResponseDto eventList = new EventListGetResponseDto(updatedEvents);

        if (updatedEvents.isEmpty()) {
            return ApiResponseGenerator.success(HttpStatus.NO_CONTENT, "조회된 일정이 없습니다.", eventList);
        }

        return ApiResponseGenerator.success(HttpStatus.OK, "일정 목록을 조회했습니다.", eventList);
    }

    @GetMapping("/events/{event_id}")
    public ResponseEntity<SuccessBody<EventDetailGetResponseDto>> getEvent(
        @RequestHeader(value = "Authorization") String authorizationHeader,
        @PathVariable("event_id") String eventId,
        @RequestParam Long memberId
    ) {
        String accessToken = authorizationHeader.replace("Bearer ", "");

        EventDetailGetResponseDto eventDetailGetResponseDto = talkCalendarService.getEvent(accessToken, eventId);

        if (eventDetailGetResponseDto == null) {
            return ApiResponseGenerator.success(HttpStatus.NO_CONTENT, "조회된 일정이 없습니다.", eventDetailGetResponseDto);
        }

        return ApiResponseGenerator.success(HttpStatus.OK, "일정 목록을 조회했습니다.", eventDetailGetResponseDto);
    }

    @PostMapping("/create/event")
    public ResponseEntity<SuccessBody<EventCreateResponseDto>> createEvent(
        @RequestHeader(value = "Authorization") String authorizationHeader,
        @RequestBody EventCreateRequestDto eventRequestDto,
        @RequestParam Long memberId
    ) {
        String accessToken = authorizationHeader.replace("Bearer ", "");
        // 카카오 api로 일정 생성
        EventCreateResponseDto eventCreateResponseDto = talkCalendarService.createEvent(accessToken, eventRequestDto, memberId);

        // 성공 응답
        return ApiResponseGenerator.success(HttpStatus.OK, "일정이 성공적으로 생성되었습니다.", eventCreateResponseDto);
    }
}
