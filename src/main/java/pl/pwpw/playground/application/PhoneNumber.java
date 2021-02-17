package pl.pwpw.playground.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

/**
 *
 */
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
class PhoneNumber {

    private String phoneNumber;
}
