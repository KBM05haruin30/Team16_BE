package org.cookieandkakao.babting.domain.calendar.controller;

import org.cookieandkakao.babting.domain.calendar.config.KakaoProperties;
import org.cookieandkakao.babting.domain.calendar.service.KakaoLoginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping
public class KakaoLoginController {

    private final KakaoProperties kakaoProperties;
    private final KakaoLoginService kakaoLoginService;

    public KakaoLoginController(KakaoProperties kakaoProperties, KakaoLoginService kakaoLoginService) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoLoginService = kakaoLoginService;
    }

    @GetMapping("/kakao/auth")
    public String kakaoLogin(Model model) {
        String loginUrl = kakaoLoginService.generateKakaoLoginUrl();
        return "redirect:" + loginUrl;
    }

    @GetMapping
    public String kakaoAccessToken(@RequestParam(value = "code") String authorizationCode,
        RedirectAttributes redirectAttributes) {
        if (authorizationCode != null) {
            String accessToken = kakaoLoginService.getAccessToken(authorizationCode);
            String email = kakaoLoginService.getUserEmail(accessToken);
            Long userId = kakaoLoginService.getUserId(accessToken);
            Long tokenId = kakaoLoginService.getTokenId(accessToken);
            System.out.println("accessToken: " + accessToken);
            System.out.println("email: " + email);
            System.out.println("userInfoId: " + userId);
            System.out.println("tokenInfoId: " + tokenId);
            return "/kakao/success";
        }
        String loginUrl = kakaoLoginService.generateKakaoLoginUrl();
        return "redirect:" + loginUrl;
    }
}