package pl.pwpw.playground.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.pwpw.playground.application.dto.ApplicationDetailsDto;
import pl.pwpw.playground.application.dto.ContactDetailsDto;
import pl.pwpw.playground.application.exception.PlaygroundException;
import pl.pwpw.playground.application.model.*;
import pl.pwpw.playground.application.repository.ApplicationRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    private ApplicationService applicationService;

    @BeforeEach
    void setup() {
        applicationService = new ApplicationService(applicationRepository);
    }

    @Test
    void shouldGetContactDetails() {
        when(applicationRepository.findByApplicationNumber(any())).thenReturn(Optional.of(new Application()
                .appId(1L)
                .applicationNumber(new ApplicationNumber("PL/2020-10-05/1"))
                .firstName("First Name")
                .lastName("Last Name")
                .contactDetails(new ContactDetails(new EmailAddress("foo@bar"), new PhoneNumber("+48555555555")))
                .applicationType(ApplicationType.APPLICATION_A)));


        final ContactDetailsDto contactDetailsDto = applicationService.getContactDetails("PL/2020-10-05/1");

        assertThatCode(() ->
                assertThat(contactDetailsDto).usingRecursiveComparison().isEqualTo(new ContactDetailsDto().email("foo@bar").phoneNumber("+48555555555"))).doesNotThrowAnyException();
    }

    @Test
    void shouldThrowNoSuchApplicationErrorWhenThereWasNoApplicationWithProvidedApplicationNumber() {
        when(applicationRepository.findByApplicationNumber(any())).thenReturn(Optional.empty());

        final Throwable throwable = catchThrowable(() -> applicationService.getContactDetails(anyString()));

        assertThat(throwable)
                .isInstanceOf(PlaygroundException.class)
                .hasFieldOrPropertyWithValue("errorMessage", "There is no such application with provided application_number!")
                .hasFieldOrPropertyWithValue("errorCode", 404);
    }

    @ParameterizedTest
    @MethodSource
    void shouldGetApplicationDetails(final Optional<List<Application>> applicationList, final List<ApplicationDetailsDto> applicationDetailsDtoList) {
        when(applicationRepository.findAllByContactDetailsEmailAddressEmailAddress(any())).thenReturn(applicationList);

        final List<ApplicationDetailsDto> applicationDetailsDto = applicationService.getApplicationDetails("foo@bar");

        assertThatCode(() ->
                assertThat(applicationDetailsDto).usingRecursiveComparison().isEqualTo(applicationDetailsDtoList)).doesNotThrowAnyException();
    }
    private static Stream<Arguments> shouldGetApplicationDetails() {
        return Stream.of(
                Arguments.of(Optional.of(
                        List.of(new Application()
                                .appId(1L)
                                .applicationNumber(new ApplicationNumber("PL/2020-10-05/1"))
                                .firstName("First Name")
                                .lastName("Last Name")
                                .contactDetails(new ContactDetails(new EmailAddress("foo@bar"), new PhoneNumber("+48555555555")))
                                .applicationType(ApplicationType.APPLICATION_A))),
                        List.of(new ApplicationDetailsDto().applicationType("APPLICATION_A").applicationNumber("PL/2020-10-05/1").lastName("Last Name"))),
                Arguments.of(Optional.of(
                        List.of(new Application()
                                        .appId(1L)
                                        .applicationNumber(new ApplicationNumber("PL/2020-10-05/1"))
                                        .firstName("First Name")
                                        .lastName("Last Name")
                                        .contactDetails(new ContactDetails(new EmailAddress("foo@bar"), new PhoneNumber("+48555555555")))
                                        .applicationType(ApplicationType.APPLICATION_A),
                                new Application()
                                        .appId(2L)
                                        .applicationNumber(new ApplicationNumber("PL/2020-10-05/2"))
                                        .firstName("First Name")
                                        .lastName("Last Name")
                                        .contactDetails(new ContactDetails(new EmailAddress("foo@bar"), new PhoneNumber("+48555555555")))
                                        .applicationType(ApplicationType.APPLICATION_B),
                                new Application()
                                        .appId(2L)
                                        .applicationNumber(new ApplicationNumber("PL/2020-12-10/256"))
                                        .firstName("First Name")
                                        .lastName("Last Name")
                                        .contactDetails(new ContactDetails(new EmailAddress("foo@bar"), new PhoneNumber("+48555555555")))
                                        .applicationType(ApplicationType.APPLICATION_C)
                        )),
                        List.of(new ApplicationDetailsDto().applicationType("APPLICATION_A").applicationNumber("PL/2020-10-05/1").lastName("Last Name"),
                                new ApplicationDetailsDto().applicationType("APPLICATION_B").applicationNumber("PL/2020-10-05/2").lastName("Last Name"),
                                new ApplicationDetailsDto().applicationType("APPLICATION_C").applicationNumber("PL/2020-12-10/256").lastName("Last Name"))));
    }


    @Test
    void shouldThrowNoSuchApplicationErrorWhenThereWasNoApplicationWithProvidedEmail() {
        when(applicationRepository.findAllByContactDetailsEmailAddressEmailAddress(any())).thenReturn(Optional.empty());

        final Throwable throwable = catchThrowable(() -> applicationService.getApplicationDetails(anyString()));

        assertThat(throwable)
                .isInstanceOf(PlaygroundException.class)
                .hasFieldOrPropertyWithValue("errorMessage", "There is no application with provided email!")
                .hasFieldOrPropertyWithValue("errorCode", 404);
    }
}