package com.social.login.sociallogintut.member.controller;

import lombok.Data;

@Data
public class ResponseDto {

    private String tokenType; // kakao ,
    private String accessToken; // kakao ,
    private String idToken; // kakao ,
    private int expiresIn; // kakao ,
    private String refreshToken; // kakao ,
    private int refreshTokenExpiresIn; // kakao ,
    private String scope; // kakao ,
    private String loginId; // kakao ,
    private String appKey; // kakao ,


}
