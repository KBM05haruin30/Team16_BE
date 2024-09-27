package org.cookieandkakao.babting.domain.calendar.controller;

import java.util.Collections;
import org.cookieandkakao.babting.domain.calendar.dto.calendarDTO.EventListGetResponseDto;
import org.cookieandkakao.babting.domain.calendar.dto.calendarDTO.EventListGetRequestDto;
import org.cookieandkakao.babting.domain.calendar.service.TalkCalendarService;
import org.cookieandkakao.babting.domain.member.entity.Member;
import org.cookieandkakao.babting.domain.member.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final MemberRepository memberRepository;

    public TalkCalendarController(TalkCalendarService talkCalendarService,
        MemberRepository memberRepository) {
        this.talkCalendarService = talkCalendarService;
        this.memberRepository = memberRepository;
    }

    @GetMapping("/events")
    public ResponseEntity<EventListGetResponseDto> getEventList(
        @RequestHeader(value = "Authorization") String authorizationHeader,
        @RequestBody EventListGetRequestDto eventListRequestDTO
    ) {
        String accessToken = authorizationHeader.replace("Bearer ", "");
        String from = eventListRequestDTO.from();
        String to = eventListRequestDTO.to();

        try {
            EventListGetResponseDto eventList = talkCalendarService.getEventList(accessToken, from, to);

            HttpStatus status = HttpStatus.OK;
            String message = "일정 목록을 조회했습니다.";

            if (eventList.events().isEmpty()) {
                status = HttpStatus.NO_CONTENT;
                message = "조회된 일정이 없습니다.";
            }

            EventListGetResponseDto responseBody = new EventListGetResponseDto(
                status.value(),
                message,
                eventList.events()
            );

            return ResponseEntity.status(status).body(responseBody);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new EventListGetResponseDto(
                    500,
                    "일정 조회 중 오류가 발생했습니다.",
                    Collections.emptyList()
                ));
        }
    }

    @PostMapping("/events")
    public ResponseEntity<String> saveEvents(
        @RequestHeader(value = "Authorization") String authorizationHeader,
        @RequestBody EventListGetRequestDto eventListRequestDTO
    ) {
        String accessToken = authorizationHeader.replace("Bearer ", "");
        String from = eventListRequestDTO.from();
        String to = eventListRequestDTO.to();
        Long memberId = eventListRequestDTO.memberId();

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        try {
            talkCalendarService.saveEvents(accessToken, from, to, member);
            return ResponseEntity.ok("일정이 성공적으로 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("일정 저장 중 오류가 발생했습니다.");
        }
    }
}
