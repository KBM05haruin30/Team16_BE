package org.cookieandkakao.babting.domain.calendar.service;

import java.net.URI;
import org.cookieandkakao.babting.domain.calendar.config.KakaoProperties;
import org.cookieandkakao.babting.domain.calendar.dto.kakaoDTO.KakaoAccessTokenDTO;
import org.cookieandkakao.babting.domain.calendar.dto.kakaoDTO.KakaoTokenInfoDTO;
import org.cookieandkakao.babting.domain.calendar.dto.kakaoDTO.KakaoUserInfoDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoLoginService {

    private final KakaoProperties kakaoProperties;
    private final RestClient restClient = RestClient.builder().build();

    public KakaoLoginService(KakaoProperties kakaoProperties) {
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

    public String getUserEmail(String accessToken) {
        String url = kakaoProperties.userInfoUrl();
        ResponseEntity<KakaoUserInfoDTO> response = restClient.get()
            .uri(URI.create(url))
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .retrieve()
            .toEntity(KakaoUserInfoDTO.class);
        KakaoUserInfoDTO kakaoUserInfoDTO = response.getBody();
        return kakaoUserInfoDTO.kakaoAccountDTO().email();
    }

    public Long getUserId(String accessToken) {
        String url = kakaoProperties.userInfoUrl();
        ResponseEntity<KakaoUserInfoDTO> response = restClient.get()
            .uri(URI.create(url))
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .retrieve()
            .toEntity(KakaoUserInfoDTO.class);
        KakaoUserInfoDTO kakaoUserInfoDTO = response.getBody();
        return kakaoUserInfoDTO.id();
    }

    public Long getTokenId(String accessToken) {
        String url = kakaoProperties.tokenInfoUrl();
        ResponseEntity<KakaoTokenInfoDTO> response = restClient.get()
            .uri(URI.create(url))
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .retrieve()
            .toEntity(KakaoTokenInfoDTO.class);
        KakaoTokenInfoDTO kakaoTokenInfoDTO = response.getBody();
        return kakaoTokenInfoDTO.id();
    }


}
