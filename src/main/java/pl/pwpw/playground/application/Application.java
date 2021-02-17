package pl.pwpw.playground.application;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 *
 */
@Data
@NoArgsConstructor
@Entity
class Application {
    @Id
    @SequenceGenerator(name = "app_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_id_seq")
    private Long appId;
    @Embedded
    private ApplicationNumber applicationNumber;
    private String firstName;
    private String lastName;
    @Embedded
    private ContactDetails contactDetails;
    @Enumerated(EnumType.STRING)
    private ApplicationType applicationType;

}
