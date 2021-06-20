package pl.pwpw.playground.application.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(value = AccessLevel.NONE)
    private Long id;
    private String fileType;
    @Lob
    private byte[] image;
    private LocalDateTime addDate;

    @ManyToOne
    @JoinColumn(name = "app_id", nullable = false)
    private Application application;


    public Attachment fileType(final String fileType) {
        this.fileType = fileType;
        return this;
    }

    public Attachment image(final byte[] image) {
        this.image = image;
        return this;
    }

    public Attachment addDate(final LocalDateTime addDate) {
        this.addDate = addDate;
        return this;
    }

    public Attachment application(final Application application) {
        this.application = application;
        return this;
    }
}
