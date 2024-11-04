package com.social.login.sociallogintut.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class LoginController {

    @Value("${kakao.nativeAppKey}")
    private String kakao_nativeAppKey;
    @Value("${kakao.restApiKey}")
    private String kakao_restApiKey;
    @Value("${kakao.javaScriptKey}")
    private String kakao_javaScriptKey;
    @Value("${kakao.adminKey}")
    private String kakao_adminKey;
    @Value("${kakao.clientSecret}")
    private String kakao_clientSecret;
    @Value("${kakao.redirectUri}")
    private String kakao_redirectUri;

    @Value("${naver.clientId}")
    private String naver_clientId;
    @Value("${naver.clientSecret}")
    private String naver_clientSecret;
    @Value("${naver.redirectUri}")
    private String naver_redirectUri;


    @GetMapping("/login")
    public String signup(Model model){

        model.addAttribute("title" , "login");
        return "/view/member/login";
    }

    @GetMapping("/kakao/auth")
    public RedirectView kakaoAuth(){
        //인가코드 요청 url
        String kakaAuthUrl = "https://kauth.kakao.com/oauth/authorize"
                            +"?client_id=" + kakao_restApiKey // 필수
                            +"&redirect_uri=" + kakao_redirectUri // 필수
                            +"&response_type=code" // 필수 'code' 고정
                            +"&state=1234" // 선택 csrf 방어
                            +"&scope=openid" // 선택 id_token 발급받기 위해
                            +"&nonce=1234"; // 선택 id_token 위조방지

        return new RedirectView(kakaAuthUrl);
    }

    @GetMapping("/naver/auth")
    public RedirectView naverAuth(){
        //인가코드 요청 url
        String naverAuthUrl = "https://nid.naver.com/oauth2.0/authorize"
                +"?response_type=code" // 필수 'code' 고정
                +"&client_id=" + naver_clientId // 필수
                +"&redirect_uri=" + naver_redirectUri // 필수
                +"&state=1234"; // 선택 csrf 방어

        return new RedirectView(naverAuthUrl);
    }

}
