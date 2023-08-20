package practice.board.member.dto;

import lombok.Getter;
import lombok.Setter;
import practice.board.config.auth.LoginMember;

@Getter
@Setter
public class LoginResponse {

    private Long userId;
    private String name;
    private String accessToken;

    public LoginResponse(LoginMember loginMember, String accessToken) {
        this.userId = loginMember.getMember().getId();
        this.name = loginMember.getUsername();
        this.accessToken = accessToken;
    }

}
