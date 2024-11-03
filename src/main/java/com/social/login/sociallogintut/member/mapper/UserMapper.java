package com.social.login.sociallogintut.member.mapper;

import com.social.login.sociallogintut.member.dto.User;


public interface UserMapper {

    User findByLoginTypeAndLoginId(User user);

}
