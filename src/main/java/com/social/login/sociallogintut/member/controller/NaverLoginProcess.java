package com.social.login.sociallogintut.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class NaverLoginProcess {

    @Value("${naver.clientId}")
    private String naver_clientId;
    @Value("${naver.clientSecret}")
    private String naver_clientSecret;
    @Value("${naver.redirectUri}")
    private String naver_redirectUri;
}
