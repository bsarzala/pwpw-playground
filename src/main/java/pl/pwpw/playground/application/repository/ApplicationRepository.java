package pl.pwpw.playground.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pwpw.playground.application.model.Application;
import pl.pwpw.playground.application.model.ApplicationNumber;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Optional<Application> findByApplicationNumber(ApplicationNumber applicationNumber);
    Optional<List<Application>> findAllByContactDetailsEmailAddressEmailAddress(String emailAddress);
}
