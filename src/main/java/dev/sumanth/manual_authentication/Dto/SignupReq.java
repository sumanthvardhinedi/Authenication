package dev.sumanth.manual_authentication.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupReq {
    private String name;
    private String email;
    private String password;
}
