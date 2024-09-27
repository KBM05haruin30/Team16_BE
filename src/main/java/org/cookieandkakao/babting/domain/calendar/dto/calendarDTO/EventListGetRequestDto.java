package org.cookieandkakao.babting.domain.calendar.dto.calendarDTO;

import org.cookieandkakao.babting.domain.member.entity.Member;

public record EventListGetRequestDto(

    String from,

    String to,

    Long memberId
) {

}
