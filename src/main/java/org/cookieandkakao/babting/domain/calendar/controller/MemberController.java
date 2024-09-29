package org.cookieandkakao.babting.domain.calendar.controller;

import org.cookieandkakao.babting.domain.calendar.service.MemberService;
import org.cookieandkakao.babting.domain.member.entity.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Member> createMember(@RequestBody Member member) {
        System.out.println("Received Member: " + member.getKakaoMemberId() + ", " + member.getNickname());

        if (member.getKakaoMemberId() == null || member.getNickname() == null) {
            System.out.println("kakaoMemberId와 nickname은 필수 값입니다.");
            throw new IllegalArgumentException("kakaoMemberId와 nickname은 필수 값입니다.");
        }
        Member savedMember = memberService.saveMember(member);
        return new ResponseEntity<>(savedMember, HttpStatus.CREATED);
    }

}
