package org.cookieandkakao.babting.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.cookieandkakao.babting.domain.member.dto.KakaoMemberProfileDto;

@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(nullable = false, unique = true, name = "kakao_member_id")
    private Long kakaoMemberId;

    @Column(nullable = false, name = "nickname")
    private String nickname;

    @Column(name = "thumbnail_image_url")
    private String thumbnailImageUrl;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    /*@OneToOne
    @JoinColumn(name = "kako_token_id")
    private KakaoToken kakaoToken;*/

    protected Member() {}

    public Member(Long kakaoMemberId) {
        this.kakaoMemberId = kakaoMemberId;
    }

    public void updateProfile(KakaoMemberProfileDto profileDto) {
        this.nickname = profileDto.getNickname();
        this.thumbnailImageUrl = profileDto.getThumbnailImage();
        this.profileImageUrl = profileDto.getProfileImage();
    }

    /*public void updateKakaoToken(KakaoToken kakaoToken) {
        this.kakaoToken = kakaoToken;
    }*/

    public Long getMemberId() {
        return memberId;
    }
}
