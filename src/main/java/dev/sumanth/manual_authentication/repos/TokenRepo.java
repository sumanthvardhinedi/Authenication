package dev.sumanth.manual_authentication.repos;

import dev.sumanth.manual_authentication.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface TokenRepo extends JpaRepository<Token, Long> {
    @Override
    Token save(Token token);

    Optional<Token> findByToken(String tokenvalue);
    Optional<Token> findByTokenAndExpiryDateAfter(String token, Date now);

    @Override
    Optional<Token> findById(Long aLong);
}
