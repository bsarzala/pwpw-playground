package pl.pwpw.playground.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class ApplicationDetailsDto {
    private String applicationType;
    private String applicationNumber;
    private String lastName;

    public ApplicationDetailsDto applicationType(final String applicationType) {
        this.applicationType = applicationType;
        return this;
    }

    public ApplicationDetailsDto applicationNumber(final String applicationNumber) {
        this.applicationNumber = applicationNumber;
        return this;
    }

    public ApplicationDetailsDto lastName(final String lastName) {
        this.lastName = lastName;
        return this;
    }
}
