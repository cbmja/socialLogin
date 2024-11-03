package com.social.login.sociallogintut.member.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {

    private int userId;
    private String userName;
    private String loginType;
    private String loginId;
    private LocalDateTime regDate;
    private String phoneNum;
    private String email;

}
