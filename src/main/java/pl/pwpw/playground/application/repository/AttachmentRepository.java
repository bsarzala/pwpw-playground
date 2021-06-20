package pl.pwpw.playground.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pwpw.playground.application.model.Attachment;


public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
