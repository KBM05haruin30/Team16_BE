package org.cookieandkakao.babting.domain.calendar.dto.kakaoDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoUserInfoDTO(
    @JsonProperty("id")
    Long id,
    @JsonProperty("kakao_account")
    KakaoAccountDTO kakaoAccountDTO
) {

}