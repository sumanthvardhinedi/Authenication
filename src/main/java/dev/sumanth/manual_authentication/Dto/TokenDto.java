package dev.sumanth.manual_authentication.Dto;

import dev.sumanth.manual_authentication.models.Role;
import dev.sumanth.manual_authentication.models.Token;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class TokenDto {

    private String token;
   public static TokenDto from(Token token) {

      if(token == null) return null;
        TokenDto tokenDto=new TokenDto();
        tokenDto.setToken(token.getToken());
        return tokenDto;
   }
}
