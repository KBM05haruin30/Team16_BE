package org.cookieandkakao.babting.domain.calendar.dto.kakaoDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoTokenInfoDTO(
    @JsonProperty("id")
    Long id,
    @JsonProperty("expires_in")
    Integer expiresIn,
    @JsonProperty("app_id")
    Integer appId
) {

}
