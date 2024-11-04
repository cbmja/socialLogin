package com.social.login.sociallogintut.member.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.social.login.sociallogintut.member.dto.User;
import com.social.login.sociallogintut.member.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
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
@RequestMapping("/api/v1/member")
public class KakaoLoginProcess {


    @Value("${kakao.nativeAppKey}")
    private String kakao_nativeAppKey;
    @Value("${kakao.clientSecret}")
    private String kakao_clientSecret;
    @Value("${kakao.redirectUri}")
    private String kakao_redirectUri;

    private final MemberService memberService;


    @GetMapping("/kakao/callback")
    public String kakaoLoginProc(@RequestParam Map<String , String> params , HttpServletRequest request , HttpServletResponse response){

        String code = (String)params.get("code");
        String state = (String)params.get("state");
        String _state = "1234";
        System.out.println("===== code: "+code+"============================================================================================================================================");
        System.out.println("===== state: "+state+"============================================================================================================================================");
        // state 체크 ----------------------------------------------------------------------------------------- state 체크 ok
        if(!state.equals(_state)){
            return null; // state 검증 실패
        }
        // code 체크 ------------------------------------------------------------------------------------------- code 체크 ok
        if(code == null || code.isBlank()){
            return null; // code 발급 실패
        }

        // token 요청 ----------------------------------------------------------------------------------------- token 요청 ok
        LoginDto loginDto = this.getKakaoToken(code);
        System.out.println("===== id_token: "+loginDto.getIdToken()+"============================================================================================================================================");
        // id token 디코딩 -------------------------------------------------------------------------------- id token 디코딩 ok
        if("nonce".equals(this.verifyToken(loginDto.getIdToken() , loginDto))){
            return null; // nonce 검증 실패
        }
        String loginId = loginDto.getLoginId();
        if(loginId == null || loginId.isBlank()){
            return null; // token decode 실패
        }

        User user = new User();
        user.setLoginType("kakao");
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


    // kakao 서버에 토큰 요청 / access , refresh , id token ... etc
    private LoginDto getKakaoToken(String code){
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
        LoginDto dto = new LoginDto();

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
            dto.setTokenType(jsonResponse.getString("token_type"));
            dto.setAccessToken(jsonResponse.getString("access_token"));
            dto.setRefreshToken(jsonResponse.getString("refresh_token"));
            dto.setExpiresIn(jsonResponse.getInt("expires_in"));
            dto.setIdToken(jsonResponse.getString("id_token"));
            dto.setRefreshTokenExpiresIn(jsonResponse.getInt("refresh_token_expires_in"));
            dto.setScope(jsonResponse.getString("scope"));

            return dto;

        }catch (Exception e){
            System.out.println("kakao getToken error");
            e.printStackTrace();
            return null;
        }

    }

    // id 토큰 검증
    private String verifyToken(String idToken, LoginDto dto) {
        String[] parts = idToken.split("\\.");
        String headerJson = new String(Base64.getUrlDecoder().decode(parts[0]));
        String kid = "";

        try {
            kid = new ObjectMapper().readTree(headerJson).get("kid").asText();
        } catch (Exception e) {
            e.printStackTrace();
        }

        PublicKey publicKey = getPublicKey(kid);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(idToken)
                .getBody();
        String nonce = claims.get("nonce", String.class);
        String _nonce = "1234";
        if(!nonce.equals(_nonce)){
            return "nonce";
        }

        dto.setLoginId(claims.get("sub", String.class));
        dto.setAppKey(claims.get("aud", String.class));

        return "success";
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
