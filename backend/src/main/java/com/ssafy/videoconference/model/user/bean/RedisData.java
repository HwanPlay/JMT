package com.ssafy.videoconference.model.user.bean;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RedisData  implements Serializable {
    private static final long serialVersionUID = -7353484588260422449L;
    private String userId;
    private String accessToken;
    private String refreshToken;
}
