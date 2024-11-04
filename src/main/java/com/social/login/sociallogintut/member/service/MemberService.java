package com.social.login.sociallogintut.member.service;

import com.social.login.sociallogintut.member.dto.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MemberService {
    @Value("${jwt.secret}")
    private String jwt_secret;

    private final SqlSessionTemplate sql;

    public User findByLoginTypeAndLoginId(User user){
        return sql.selectOne("com.social.login.sociallogintut.member.mapper.UserMapper.findByLoginTypeAndLoginId", user);
    }
    public int save(User user){
        return sql.insert("com.social.login.sociallogintut.member.mapper.UserMapper.save", user);
    }
    public int login(int userId){
        return sql.insert("com.social.login.sociallogintut.member.mapper.UserAccessLogMapper.login",userId);
    }



    public void setCookie(HttpServletResponse response , User user){
        String loginToken = this.genAccessToken(user);

        Cookie loginCookie = new Cookie("accessToken" , loginToken);
        loginCookie.setPath("/");
        loginCookie.setHttpOnly(true); // 자바스크립트에서 접근 불가하게 설정(보안)
        loginCookie.setMaxAge(60 * 60 * 24 * 14); // 쿠키 유효 기간 14일
        response.addCookie(loginCookie);

    }

    public String genUserName(){
        Random random = new Random();
        String[] firstName = {"건강한","튼튼한","쾌적한","상쾌한","신선한","활기찬","청량한","청정한","생기찬","활력찬","경쾌한","강건한","강인한","기운찬","적정한","희망찬","온화한","유연한","포근한","안전한","무난한","화창한","쾌활한","깔끔한","정갈한","활달한","보송한","따뜻한","청초한","산뜻한","풍성한","정결한","온유한","단정한","안락한","온순한","눈부신","풍부한","깨끗한","자연의","푸릇한","고요한","활발한","무사한","해맑은","건전한","정상인","발랄한","빛나는","명쾌한","긍정적","행복한","낭만적","성실한","유쾌한","감동적","자유한","반짝한","순조로운","완벽한","용감한","사랑한","똑똑한","친절한","지혜한","신명한","정다운","소중한","충실한","번쩍한","흥미왕","우아한","자애킹","청아한","상냥한","선명한","뷰티풀","피곤한","배고픈","조올린","지이친","갈증난","허기진","심심한","나른한","목마른","불안한"};
        String[] lastName =  {"가방","고래","베어","꽃꽃","치킨","다리","반달","딸기","두부","드럼","꿀꿀","스타","버스","사자","사과","상자","상어","새우","소금","소리","송이","수박","식물","시계","금치","양파","여우","오리","우유","운동","이불","장미","장갑","조개","주스","책상","커피","코트","호수","호박","홀홀","기차","날개","녹차","구름","문제","지구","콩쥐","주문","편지","기름","밤빵","선물","마을","병원","벌레","버섯","담배","텃밭","수도","강철","두유","날씨","로션","사람","자두","자판","연필","우산","냉장","자연","키스","가위","화장","식탁","치즈","새벽","파도","모자","연못","코코","손톱","포도","유리","회전","경기","교육","해변","입술","음악","직원","감기","부엌","정원","시청","전구","가락","체육"};
        int num1 = random.nextInt(firstName.length);
        int num2 = random.nextInt(lastName.length);

        return firstName[num1]+lastName[num2]+num1+num2;
    }

    public String genAccessToken(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("loginId", user.getLoginId());
        claims.put("username", user.getUserName());
        claims.put("loginType", user.getLoginType());
        long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 15;
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getLoginId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, jwt_secret)
                .compact();
    }

}
