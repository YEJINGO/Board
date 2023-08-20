package practice.board.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import practice.board.entity.MemberEnum;

@Getter
@Setter
@NoArgsConstructor
public class MemberSignupRequest {
    private String name;
    private String email;
    private String password;
    private MemberEnum role;


}
