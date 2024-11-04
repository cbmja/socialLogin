package com.social.login.sociallogintut.member.service;

import com.social.login.sociallogintut.member.dto.User;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

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




}
