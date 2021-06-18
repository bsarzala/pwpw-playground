package pl.pwpw.playground.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContactDetailsDto {
    private String email;
    private String phoneNumber;


    public ContactDetailsDto email(final String email) {
        this.email = email;
        return this;
    }

    public ContactDetailsDto phoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }
}
