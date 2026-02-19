package dev.sumanth.manual_authentication.controllers;
import dev.sumanth.manual_authentication.Dto.SignupReq;
import dev.sumanth.manual_authentication.Dto.TokenDto;
import dev.sumanth.manual_authentication.Dto.UserDto;
import dev.sumanth.manual_authentication.Dto.loginReq;
import dev.sumanth.manual_authentication.Exception.PasswordMissmatch;
import dev.sumanth.manual_authentication.models.Token;
import dev.sumanth.manual_authentication.models.User;
import dev.sumanth.manual_authentication.service.UserServiceInf;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserServiceInf userService;

    public UserController(UserServiceInf userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public UserDto signup(@RequestBody SignupReq signupReq) {
        User user = userService.signup(
                signupReq.getName(),
                signupReq.getEmail(),
                signupReq.getPassword()
        );
        return UserDto.from(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody loginReq loginReq) throws PasswordMissmatch {
        String token = String.valueOf(userService.login(
                loginReq.getEmail(),
                loginReq.getPassword()
        ));

        return token;
    }

    @GetMapping("/validate/{token}")
    public UserDto validateToken(@PathVariable String token) {
      User user= userService.validateToken(token);
      return UserDto.from(user);
    }
}
