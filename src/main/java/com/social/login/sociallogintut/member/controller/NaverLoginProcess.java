package com.social.login.sociallogintut.member.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.social.login.sociallogintut.member.dto.User;
import com.social.login.sociallogintut.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

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
    private final MemberService memberService;

    @GetMapping("/naver/callback")
    public String naverLoginProc(@RequestParam Map<String , String> params , HttpServletRequest request , HttpServletResponse response){

        String code = (String)params.get("code") != null ? (String)params.get("code") : "";
        String state = (String)params.get("state") != null ? (String)params.get("state") : "";
        String error = (String)params.get("error") != null ? (String)params.get("error") : "";
        String errorDescription = (String)params.get("error_description") != null ? (String)params.get("error_description") : "";

        String _state = "1234";
        System.out.println("===== code: "+code+"============================================================================================================================================");
        System.out.println("===== state: "+state+"============================================================================================================================================");
        // error 체크 ----------------------------------------------------------------------------------------- error 체크
        if(!error.isBlank()){
            return null; // 에러
        }
        // state 체크 ----------------------------------------------------------------------------------------- state 체크 ok
        if(!state.equals(_state)){
            return null; // state 검증 실패
        }
        // code 체크 ------------------------------------------------------------------------------------------- code 체크 ok
        if(code == null || code.isBlank()){
            return null; // code 발급 실패
        }

        // token 요청 ----------------------------------------------------------------------------------------- token 요청 ok
        LoginDto loginDto = this.getNaverToken(code , state);
        System.out.println("===== access_token: "+loginDto.getAccessToken()+"============================================================================================================================================");
        // 사용자 정보 요청 -------------------------------------------------------------------------------- 사용자 정보 요청 ok
        this.getNaverUserInfo(loginDto);
        String loginId = loginDto.getLoginId();
        if(loginId == null || loginId.isBlank()){
            return null; // 사용자 정보 요청 실패
        }

        User user = new User();
        user.setLoginType("naver");
        user.setLoginId(loginDto.getLoginId());
        System.out.println("===== loginType: "+user.getLoginType()+"============================================================================================================================================");
        System.out.println("===== loginId: "+user.getLoginId()+"============================================================================================================================================");
        User isUser = memberService.findByLoginTypeAndLoginId(user);
        // 회원 가입 -------------------------------------------------------------------------------------------- 회원 가입 ok
        if(isUser == null){
            System.out.println("sign up====================================================================================================================================================================================");
            // 동의 항목 수집 필요

            user.setUserName(memberService.genUserName());

            int joinResult = memberService.save(user);
            if(joinResult <= 0){
                return null; // 회원가입 실패
            }


        }

        // 로그인 ------------------------------------------------------------------------------------------- 로그인
        System.out.println("sign in====================================================================================================================================================================================");
        int loginResult = memberService.login(user.getUserId());
        if(loginResult <= 0){
            return null; //로그인 실패
        }else{
            memberService.setCookie(response,user);
        }


        return "/view/index";
    }


    // naver 서버에 토큰 요청 / access , refresh , id token ... etc
    private LoginDto getNaverToken(String code , String state){
        // token 요청 url
        String naverTokenUrl = "https://nid.naver.com/oauth2.0/token";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        // 요청 바디
        String body = "grant_type=authorization_code" // 필수 'authorization_code' 고정
                + "&client_id=" +  naver_clientId  // 필수
                + "&client_secret=" + naver_clientSecret // 필수
                + "&code=" + code // 필수
                + "&state=" + state; // 필수

        ResponseEntity<String> response = null;
        LoginDto dto = new LoginDto();

        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

        try{
            // token 요청
            response = restTemplate.exchange(
                    naverTokenUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            // 응답 수집
            JSONObject jsonResponse = new JSONObject(response.getBody());
            dto.setAccessToken(jsonResponse.getString("access_token"));
            dto.setRefreshToken(jsonResponse.getString("refresh_token"));
            dto.setTokenType(jsonResponse.getString("token_type"));
            dto.setExpiresIn(jsonResponse.getInt("expires_in"));
            dto.setError(jsonResponse.getString("error"));
            dto.setErrorDescription(jsonResponse.getString("error_description"));

            return dto;

        }catch (Exception e){
            System.out.println("naver getToken error");
            e.printStackTrace();
            return null;
        }

    }

    // 회원 정보 요청
    private void getNaverUserInfo(LoginDto loginDto) {
        // 회원 정보 요청 url
        String naverUserUrl = "https://openapi.naver.com/v1/nid/me";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        // 요청 헤더 , 바디
        headers.set("Authorization",loginDto.getTokenType()+" "+loginDto.getAccessToken()); // 필수

        ResponseEntity<String> response = null;
        LoginDto dto = new LoginDto();

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try{
            // token 요청
            response = restTemplate.exchange(
                    naverUserUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            // 응답 수집
            JSONObject jsonResponse = new JSONObject(response.getBody());
            JSONObject responseBody = jsonResponse.getJSONObject("response");
            loginDto.setLoginId(responseBody.getString("id"));


        }catch (Exception e){
            System.out.println("naver getUserInfo error");
            e.printStackTrace();
        }

    }
    // kakao publickey 발급
    public static PublicKey getPublicKey(String kid) {
        try {
            URL url = new URL("https://kauth.kakao.com/.well-known/jwks.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jwks = mapper.readTree(conn.getInputStream()).get("keys");

            for (JsonNode key : jwks) {
                if (key.get("kid").asText().equals(kid)) {
                    // n과 e를 Base64 URL 디코딩 후 BigInteger로 변환
                    BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(key.get("n").asText()));
                    BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode(key.get("e").asText()));

                    // RSAPublicKeySpec을 사용해 공개 키 생성
                    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                    return keyFactory.generatePublic(new RSAPublicKeySpec(modulus, exponent));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}
