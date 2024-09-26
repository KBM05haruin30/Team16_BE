package org.cookieandkakao.babting.domain.calendar.service;


import java.net.URI;
import java.util.List;
import org.cookieandkakao.babting.domain.calendar.config.KakaoProperties;
import org.cookieandkakao.babting.domain.calendar.dto.calendarDTO.CalendarResponseBodyDTO;
import org.cookieandkakao.babting.domain.calendar.dto.calendarDTO.EventBriefDTO;
import org.cookieandkakao.babting.domain.calendar.dto.calendarDTO.EventListResponseDTO;
import org.cookieandkakao.babting.domain.calendar.dto.calendarDTO.TimeDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class TalkCalendarService {

    private final KakaoProperties kakaoProperties;
    private final RestClient restClient = RestClient.builder().build();

    public TalkCalendarService(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }
//    public EventListResponseDTO getEvents(String accessToken, String from, String to) {
//        String url = kakaoProperties.eventListUrl();
//        URI uri = buildUri(url, from, to);
//        System.out.println("API 요청 URL: " + uri.toString());
//        System.out.println("Access Token: " + accessToken);
//
//        try {
//            ResponseEntity<EventListResponseDTO> response = restClient.get()
//                .uri(uri)
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
//                .retrieve()
//                .toEntity(EventListResponseDTO.class);
//            System.out.println("API 응답 수신 성공");
//            System.out.println("API 응답 본문: " + response.getBody());
//            return response.getBody();
//        } catch (Exception e) {
//            System.out.println("예외 발생: " + e.getMessage());
//            throw new RuntimeException("API 호출 중 오류 발생: " + e.getMessage(), e);
//        }
//    }
public CalendarResponseBodyDTO getEvents(String accessToken, String from, String to) {
    String url = kakaoProperties.eventListUrl();
    URI uri = buildUri(url, from, to);
    System.out.println("API 요청 URL: " + uri.toString());
    System.out.println("Access Token: " + accessToken);

    try {
        ResponseEntity<CalendarResponseBodyDTO> response = restClient.get()
            .uri(uri)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .retrieve()
            .toEntity(CalendarResponseBodyDTO.class);
        System.out.println("API 응답 수신 성공");
        System.out.println("API 응답 본문: " + response.getBody());
        return response.getBody();
    } catch (Exception e) {
        System.out.println("예외 발생: " + e.getMessage());
        throw new RuntimeException("API 호출 중 오류 발생: " + e.getMessage(), e);
    }
}


    private URI buildUri(String baseUrl, String from, String to) {
        return URI.create(String.format("%s?from=%s&to=%s&limit=100&time_zone=Asia/Seoul", baseUrl, from, to));
    }


}
