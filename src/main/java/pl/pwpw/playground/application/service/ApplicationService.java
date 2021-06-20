package pl.pwpw.playground.application.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pwpw.playground.application.dto.ApplicationDetailsDto;
import pl.pwpw.playground.application.dto.ContactDetailsDto;
import pl.pwpw.playground.application.exception.FaultBuilder;
import pl.pwpw.playground.application.exception.PlaygroundException;
import pl.pwpw.playground.application.model.Application;
import pl.pwpw.playground.application.model.ApplicationNumber;
import pl.pwpw.playground.application.repository.ApplicationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ContactDetailsDto getContactDetails(final String applicationNumber) {
        final Application application = applicationRepository
                .findByApplicationNumber(new ApplicationNumber(applicationNumber))
                .orElseThrow(() -> new PlaygroundException(FaultBuilder.NO_SUCH_APPLICATION));

        return new ContactDetailsDto()
                .email(application.getContactDetails().getEmailAddress().getEmailAddress())
                .phoneNumber(application.getContactDetails().getPhoneNumber().getPhoneNumber());
    }

    public List<ApplicationDetailsDto> getApplicationDetails(final String email) {
        final List<Application> applicationList = applicationRepository
                .findAllByContactDetailsEmailAddressEmailAddress(email)
                .orElseThrow(() -> new PlaygroundException(FaultBuilder.NO_SUCH_EMAIL));

        return applicationList.stream().map(application -> new ApplicationDetailsDto()
                .applicationNumber(application.getApplicationNumber().getApplicationNumber())
                .applicationType(application.getApplicationType().name())
                .lastName(application.getLastName()))
                .collect(Collectors.toList());
    }
}
