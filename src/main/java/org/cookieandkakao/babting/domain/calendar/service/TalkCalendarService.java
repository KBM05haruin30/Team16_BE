package org.cookieandkakao.babting.domain.calendar.service;

import java.net.URI;
import org.cookieandkakao.babting.domain.calendar.config.KakaoProperties;
import org.cookieandkakao.babting.domain.calendar.dto.kakaoDTO.KakaoAccessTokenDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class TalkCalendarService {

    private final KakaoProperties kakaoProperties;
    private final RestClient restClient = RestClient.builder().build();

    public TalkCalendarService(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }

    public String generateKakaoLoginUrl() {
        return kakaoProperties.generateLoginUrl();
    }

    public String getAccessToken(String authorizationCode) {
        String url = kakaoProperties.tokenUrl();
        final LinkedMultiValueMap<String, String> body = createBody(authorizationCode);
        ResponseEntity<KakaoAccessTokenDTO> response = restClient.post()
            .uri(URI.create(url))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(KakaoAccessTokenDTO.class);
        KakaoAccessTokenDTO kakaoAccessTokenDTO = response.getBody();
        return kakaoAccessTokenDTO.accessToken();
    }

    private LinkedMultiValueMap<String, String> createBody(String authorizationCode) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_url", kakaoProperties.redirectUrl());
        body.add("code", authorizationCode);
        return body;
    }

}
