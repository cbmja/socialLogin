package com.social.login.sociallogintut.member.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.social.login.sociallogintut.member.dto.User;
import com.social.login.sociallogintut.member.service.MemberInfoService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class KakaoLoginProcess {

    @Value("${kakao.nativeAppKey}")
    private String kakao_nativeAppKey;
    @Value("${kakao.clientSecret}")
    private String kakao_clientSecret;
    @Value("${kakao.redirectUri}")
    private String kakao_redirectUri;

    private final MemberInfoService memberInfoService;


    @GetMapping("/kakao/callback")
    @ResponseBody
    public String kakaoLoginProc(@RequestParam Map<String , String> params){

        String code = (String)params.get("code");
        String state = (String)params.get("state");
        String _state = "1234";
        System.out.println("code: "+code+"==========================================================================================");
        System.out.println("state: "+state+"==========================================================================================");
        // state 체크 ----------------------------------------------------------------------------------------- state 체크
        if(!state.equals(_state)){
            return null;
        }
        // code 체크 ------------------------------------------------------------------------------------------- code 체크
        if(code == null || code.isBlank()){
            return null;
        }

        // token 요청 ----------------------------------------------------------------------------------------- token 요청
        // token_type / access_token / id_token expires_in / refresh_token / refresh_token_expires_in / scope
        ResponseDto response = this.getKakaoToken(code);
        System.out.println("id token: "+response.getIdToken()+"==========================================================================================");
        // id token 디코딩 -------------------------------------------------------------------------------- id token 디코딩
        this.decodeToken(response.getIdToken() , response);
        String loginId = response.getLoginId();

        if(loginId == null || loginId.isBlank()){
            return null;
        }

        User user = new User();
        user.setLoginType("kakao");
        user.setLoginId(response.getLoginId());

        User isUser = memberInfoService.findByLoginTypeAndLoginId(user);
        // 회원 가입 -------------------------------------------------------------------------------------------- 회원 가입
        if(isUser == null){
            System.out.println("sign up==========================================================================================");
        }else{ // 로그인 ------------------------------------------------------------------------------------------- 로그인
            System.out.println("sign in==========================================================================================");
        }


        return response.toString();
    }


    private ResponseDto getKakaoToken(String code){
        // token 요청 url
        String kakaoTokenUrl = "https://kauth.kakao.com/oauth/token";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        // 요청 헤더, 바디
        headers.set("Content-type","application/x-www-form-urlencoded;charset=utf-8"); // 필수 'application/x-www-form-urlencoded;charset=utf-8' 고정
        String body = "grant_type=authorization_code" // 필수 'authorization_code' 고정
                + "&client_id=" +  kakao_nativeAppKey  // 필수
                + "&redirect_uri=" + kakao_redirectUri // 필수
                + "&code=" + code // 필수
                + "&client_secret=" + kakao_clientSecret; // 선택 : [내 애플리케이션] > [카카오 로그인] > [보안]

        ResponseEntity<String> response = null;
        ResponseDto res = new ResponseDto();

        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

        try{
            // token 요청
            response = restTemplate.exchange(
                    kakaoTokenUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            // 응답 수집
            JSONObject jsonResponse = new JSONObject(response.getBody());
            res.setTokenType(jsonResponse.getString("token_type"));
            res.setAccessToken(jsonResponse.getString("access_token"));
            res.setRefreshToken(jsonResponse.getString("refresh_token"));
            res.setExpiresIn(jsonResponse.getInt("expires_in"));
            res.setIdToken(jsonResponse.getString("id_token"));
            res.setRefreshTokenExpiresIn(jsonResponse.getInt("refresh_token_expires_in"));
            res.setScope(jsonResponse.getString("scope"));

            return res;

        }catch (Exception e){
            System.out.println("kakao getToken error");
            e.printStackTrace();
            return null;
        }

    }

    private void decodeToken(String idToken , ResponseDto res){

        // 디코딩
        DecodedJWT decodedJWT = JWT.decode(idToken);
        String payload = new String(Base64.getUrlDecoder().decode(decodedJWT.getPayload()));
        JSONObject jsonPayload = new JSONObject(payload);

        res.setLoginId(jsonPayload.getString("sub"));
        res.setAppKey(jsonPayload.getString("aud"));

    }




}
