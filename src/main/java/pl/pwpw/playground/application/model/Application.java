package pl.pwpw.playground.application.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class Application {
    @Id
    @SequenceGenerator(name = "app_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_id_seq")
    private Long appId;
    @Embedded
    @Column(unique = true)
    private ApplicationNumber applicationNumber;
    private String firstName;
    private String lastName;
    @Embedded
    private ContactDetails contactDetails;
    @Enumerated(EnumType.STRING)
    private ApplicationType applicationType;


    public Application appId(final Long appId) {
        this.appId = appId;
        return this;
    }

    public Application applicationNumber(final ApplicationNumber applicationNumber) {
        this.applicationNumber = applicationNumber;
        return this;
    }

    public Application firstName(final String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Application lastName(final String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Application contactDetails(final ContactDetails contactDetails) {
        this.contactDetails = contactDetails;
        return this;
    }

    public Application applicationType(final ApplicationType applicationType) {
        this.applicationType = applicationType;
        return this;
    }
}
