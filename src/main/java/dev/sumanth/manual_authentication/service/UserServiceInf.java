package dev.sumanth.manual_authentication.service;

import dev.sumanth.manual_authentication.Exception.PasswordMissmatch;
import dev.sumanth.manual_authentication.models.Token;
import dev.sumanth.manual_authentication.models.User;

public interface UserServiceInf {
    User signup(String name, String email, String password);
    String login(String email, String password) throws PasswordMissmatch;
   User validateToken(String tokenvalue);
}
