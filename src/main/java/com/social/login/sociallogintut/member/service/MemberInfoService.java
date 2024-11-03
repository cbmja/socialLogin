package com.social.login.sociallogintut.member.service;

import com.social.login.sociallogintut.member.dto.User;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberInfoService {

    private final SqlSessionTemplate sql;

    public User findByLoginTypeAndLoginId(User user){
        return sql.selectOne("com.social.login.sociallogintut.member.mapper.UserMapper.findByLoginTypeAndLoginId", user);
    }



}
