package org.cookieandkakao.babting.domain.calendar.dto.kakaoDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoAccountDTO(
    @JsonProperty("email")
    String email
) {

}
