package org.cookieandkakao.babting.domain.calendar.dto.kakaoDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoAccessTokenDTO(
    @JsonProperty("access_token")
    String accessToken,
    @JsonProperty("token_type")
    String tokenType,
    @JsonProperty("refresh_token")
    String refreshToken,
    @JsonProperty("expires_in")
    Long expiresIn,
    @JsonProperty("scope")
    String scope,
    @JsonProperty("refresh_token_expires_in")
    Long refreshTokenExpiresIn
) {

}