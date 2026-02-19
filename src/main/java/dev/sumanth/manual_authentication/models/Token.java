package dev.sumanth.manual_authentication.models;

import dev.sumanth.manual_authentication.Dto.TokenDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity(name="tokens")
public class Token extends BaseModel {
    private String token;
    private Date expiryDate;
    @ManyToOne
    private User user;


}
