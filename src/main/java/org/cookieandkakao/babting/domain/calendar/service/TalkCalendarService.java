package org.cookieandkakao.babting.domain.calendar.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.Map;
import org.cookieandkakao.babting.domain.calendar.dto.request.EventCreateRequestDto;
import org.cookieandkakao.babting.domain.calendar.dto.response.EventCreateResponseDto;
import org.cookieandkakao.babting.domain.calendar.dto.response.EventGetResponseDto;
import org.cookieandkakao.babting.domain.calendar.dto.response.EventListGetResponseDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class TalkCalendarService {

    private final RestClient restClient = RestClient.builder().build();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final EventService eventService;

    public TalkCalendarService(EventService eventService) {
        this.eventService = eventService;
    }

    public EventListGetResponseDto getEventList(String accessToken, String from, String to) {
        String url = "https://kapi.kakao.com/v2/api/calendar/events";
        URI uri = buildGetListUri(url, from, to);
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

    public EventGetResponseDto getEvent(String accessToken, String eventId) {
        String url = "https://kapi.kakao.com/v2/api/calendar/event";
        URI uri = buildGetEventUri(url, eventId);
        try {
            ResponseEntity<Map<String, EventGetResponseDto>> response = restClient.get()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, EventGetResponseDto>>() {});

            // 응답에서 "event" 키로 값을 꺼냄
            Map<String, EventGetResponseDto> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("event")) {
                return responseBody.get("event");
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("API 호출 중 오류 발생: " + e.getMessage(), e);
        }
    }

    private URI buildGetListUri(String baseUrl, String from, String to) {
        return URI.create(String.format("%s?from=%s&to=%s&limit=100&time_zone=Asia/Seoul", baseUrl, from, to));
    }

    private URI buildGetEventUri(String baseUrl, String eventId) {
        return URI.create(String.format("%s?event_id=%s", baseUrl, eventId));
    }

    public EventCreateResponseDto createEvent(String accessToken, EventCreateRequestDto eventCreateRequestDto, Long memberId) {
        String url = "https://kapi.kakao.com/v2/api/calendar/create/event";
        URI uri = URI.create(url);

        try {
            System.out.println("변환중");
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

            // event라는 key에 JSON 형태의 데이터를 추가해야 함
            String eventJson = convertToJSONString(eventCreateRequestDto); // EventCreateRequestDto를 JSON으로 변환
            System.out.println(eventJson);
            formData.add("event", eventJson); // event라는 key로 JSON 데이터를 추가

            System.out.println("변환 성공");
            System.out.println("POST 요청중");

            // POST 요청 실행
            ResponseEntity<Map> response = restClient.post()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .body(formData)
                .retrieve()
                .toEntity(Map.class);
            System.out.println("POST 요청 성공");
            // 응답에서 event_id 추출
            System.out.println("event_id 추출 중");
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("event_id")) {
                String eventId = responseBody.get("event_id").toString();
                // EventService를 호출하여 이벤트 저장
                eventService.saveCreatedEvent(eventCreateRequestDto, eventId, memberId);
                return new EventCreateResponseDto(eventId);  // EventCreateResponseDto로 응답 반환
            }
            System.out.println("event_id 추출 성공");

            throw new RuntimeException("Event 생성 중 오류 발생: 응답에서 event_id가 없습니다.");

        } catch (Exception e) {
            throw new RuntimeException("API 호출 중 오류 발생: " + e.getMessage(), e);
        }
    }

    // EventCreateRequestDto를 JSON 문자열로 변환하는 메서드
    private String convertToJSONString(EventCreateRequestDto eventCreateRequestDto) {
        try {
            return objectMapper.writeValueAsString(eventCreateRequestDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
