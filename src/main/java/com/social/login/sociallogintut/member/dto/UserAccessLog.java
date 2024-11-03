package com.social.login.sociallogintut.member.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserAccessLog {

    private int accessLogId;
    private int userId;
    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;
}
