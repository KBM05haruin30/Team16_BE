package org.cookieandkakao.babting.domain.calendar.service;

import java.net.URI;
import org.cookieandkakao.babting.domain.calendar.dto.calendarDTO.EventListGetResponseDto;
import org.cookieandkakao.babting.domain.calendar.entity.Event;
import org.cookieandkakao.babting.domain.calendar.entity.PersonalCalendar;
import org.cookieandkakao.babting.domain.calendar.repository.EventRepository;
import org.cookieandkakao.babting.domain.calendar.repository.PersonalCalendarRepository;
import org.cookieandkakao.babting.domain.member.entity.Member;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class TalkCalendarService {

    private final RestClient restClient = RestClient.builder().build();
    private final PersonalCalendarRepository personalCalendarRepository;
    private final EventRepository eventRepository;

    public TalkCalendarService(PersonalCalendarRepository personalCalendarRepository, EventRepository eventRepository) {
        this.personalCalendarRepository = personalCalendarRepository;
        this.eventRepository = eventRepository;
    }

    public void saveEvents(String accessToken, String from, String to, Member member) {
        PersonalCalendar personalCalendar = personalCalendarRepository.findByMember(member)
            .orElseGet(() -> {
                PersonalCalendar newCalendar = new PersonalCalendar(member);
                return personalCalendarRepository.save(newCalendar);
            });
        System.out.println("개인 캘린더 생성 완료");
        EventListGetResponseDto eventList = getEventList(accessToken, from, to);
        System.out.println("일정 조회 완료");
        eventList.events().forEach(eventDto -> {
            try {
                Event event = new Event(
                    personalCalendar,
                    eventDto.time().toEntity(),  // 여기서 오류 가능성 있음
                    eventDto.title(),
                    eventDto.type(),
                    eventDto.isRecurEvent(),
                    eventDto.rrule(),
                    eventDto.dtStart(),
                    eventDto.description(),
                    eventDto.color(),
                    eventDto.memo()
                );
                System.out.println("일정 변환 완료");

                eventRepository.save(event);
                System.out.println("일정 저장 완료");

            } catch (Exception e) {
                System.out.println("Event 생성 중 오류 발생: " + e.getMessage());
            }
        });

    }


    public EventListGetResponseDto getEventList(String accessToken, String from, String to) {
        String url = "https://kapi.kakao.com/v2/api/calendar/events";
        URI uri = buildUri(url, from, to);
        try {
            ResponseEntity<EventListGetResponseDto> response = restClient.get()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .toEntity(EventListGetResponseDto.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("API 호출 중 오류 발생: " + e.getMessage(), e);
        }
    }

    private URI buildUri(String baseUrl, String from, String to) {
        return URI.create(String.format("%s?from=%s&to=%s&limit=100&time_zone=Asia/Seoul", baseUrl, from, to));
    }
}

