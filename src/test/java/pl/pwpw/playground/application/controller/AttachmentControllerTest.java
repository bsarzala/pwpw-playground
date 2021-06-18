package pl.pwpw.playground.application.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.pwpw.playground.application.exception.FaultBuilder;
import pl.pwpw.playground.application.exception.PlaygroundException;
import pl.pwpw.playground.application.exception.controller.handler.ResponseExceptionHandler;
import pl.pwpw.playground.application.service.AttachmentService;

import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class AttachmentControllerTest {

    @Mock
    private AttachmentService attachmentService;

    private AttachmentController attachmentController;

    private MockMvc mockMvc;


    @BeforeEach
    void setup() {
        attachmentController = new AttachmentController(attachmentService);

        mockMvc = MockMvcBuilders.standaloneSetup(attachmentController)
                .setControllerAdvice(new ResponseExceptionHandler())
                .build();
    }

    @Test
    void shouldReturnCreatedStatusWhenFileHasBeenAddedSuccessfully() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "file.pdf", MediaType.TEXT_PLAIN_VALUE, "ExampleFile".getBytes());

        doNothing().when(attachmentService).uploadFile("PL/2020-10-05/1", file);

        mockMvc.perform(multipart("/attachments/files")
                .file(file)
                .param("application_number", "PL/2020-10-05/1")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated());

        verify(attachmentService, times(1)).uploadFile("PL/2020-10-05/1", file);
    }

    @ParameterizedTest
    @MethodSource
    void shouldThrowError(final MockMultipartFile file, final PlaygroundException exception, final ResultMatcher status,
                          final int code, final String message) throws Exception {
        doThrow(exception)
                .when(attachmentService).uploadFile("PL/2020-10-05/1", file);

        mockMvc.perform(multipart("/attachments/files")
                .file(file)
                .param("application_number", "PL/2020-10-05/1")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(code))
                .andExpect(jsonPath("$.message").value(message));

        verify(attachmentService, times(1)).uploadFile("PL/2020-10-05/1", file);
    }
    private static Stream<Arguments> shouldThrowError() {



        return Stream.of(
                Arguments.of(new MockMultipartFile("file", "file.txt", MediaType.TEXT_PLAIN_VALUE, "ExampleFile".getBytes()),
                             new PlaygroundException(FaultBuilder.WRONG_FILE_TYPE), status().isBadRequest(), 400, "File has wrong type! Provide .pdf or .jpg only."),
                Arguments.of(new MockMultipartFile("file", "file.pdf", MediaType.APPLICATION_PDF_VALUE, "ExampleFile".getBytes()),
                             new PlaygroundException(FaultBuilder.NO_SUCH_APPLICATION), status().isNotFound(), 404, "There is no such application with provided application_number!"),
                Arguments.of(new MockMultipartFile("file", "file.pdf", MediaType.APPLICATION_PDF_VALUE, "ExampleFile".getBytes()),
                             new PlaygroundException(FaultBuilder.CANNOT_TRANSFORM_FILE), status().isInternalServerError(), 500, "Corrupted file!"));
    }
}