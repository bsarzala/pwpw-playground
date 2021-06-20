package pl.pwpw.playground.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import pl.pwpw.playground.application.exception.PlaygroundException;
import pl.pwpw.playground.application.model.*;
import pl.pwpw.playground.application.repository.ApplicationRepository;
import pl.pwpw.playground.application.repository.AttachmentRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AttachmentServiceTest {

    @Mock
    private AttachmentRepository attachmentRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    private AttachmentService attachmentService;

    @BeforeEach
    void setup() {
        attachmentService = new AttachmentService(attachmentRepository, applicationRepository);
    }

    @Test
    void shouldInvokeSave() {
        when(applicationRepository.findByApplicationNumber(any())).thenReturn(Optional.of(new Application()
                .appId(1L)
                .applicationNumber(new ApplicationNumber("PL/2020-10-05/1"))
                .firstName("First Name")
                .lastName("Last Name")
                .contactDetails(new ContactDetails(new EmailAddress("foo@bar"), new PhoneNumber("+48555555555")))
                .applicationType(ApplicationType.APPLICATION_A)));

        assertThatCode(() -> attachmentService.uploadFile("PL/2020-10-05/1", new MockMultipartFile(
                "file", "file.pdf", MediaType.APPLICATION_PDF_VALUE, "ExampleFile" .getBytes()))).doesNotThrowAnyException();
        verify(attachmentRepository, times(1)).save(any(Attachment.class));
    }

    @Test
    void shouldThrowNoSuchApplicationErrorWhenThereWasNoApplicationWithProvidedApplicationNumber() {
        when(applicationRepository.findByApplicationNumber(any())).thenReturn(Optional.empty());

        final Throwable throwable = catchThrowable(() -> attachmentService.uploadFile("PL/2020-10-05/1", new MockMultipartFile(
                "file", "file.pdf", MediaType.APPLICATION_PDF_VALUE, "ExampleFile" .getBytes())));

        assertThat(throwable)
                .isInstanceOf(PlaygroundException.class)
                .hasFieldOrPropertyWithValue("errorMessage", "There is no such application with provided application_number!")
                .hasFieldOrPropertyWithValue("errorCode", 404);
    }

    @Test
    void shouldThrowWrongFileTypeErrorWhenProvidedFileHadWrongType() {
        final Throwable throwable = catchThrowable(() -> attachmentService.uploadFile("PL/2020-10-05/1", new MockMultipartFile(
                "file", "file.xml", MediaType.TEXT_XML_VALUE, "ExampleFile" .getBytes())));

        assertThat(throwable)
                .isInstanceOf(PlaygroundException.class)
                .hasFieldOrPropertyWithValue("errorMessage", "File has wrong type! Provide .pdf or .jpg only.")
                .hasFieldOrPropertyWithValue("errorCode", 400);
    }
}