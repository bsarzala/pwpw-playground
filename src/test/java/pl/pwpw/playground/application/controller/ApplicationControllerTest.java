package pl.pwpw.playground.application.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.pwpw.playground.application.dto.ApplicationDetailsDto;
import pl.pwpw.playground.application.dto.ContactDetailsDto;
import pl.pwpw.playground.application.exception.FaultBuilder;
import pl.pwpw.playground.application.exception.PlaygroundException;
import pl.pwpw.playground.application.exception.controller.handler.ResponseExceptionHandler;
import pl.pwpw.playground.application.service.ApplicationService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ApplicationControllerTest {

    @Mock
    private ApplicationService applicationService;

    private ApplicationController applicationController;

    private MockMvc mockMvc;


    @BeforeEach
    void setup() {
        applicationController = new ApplicationController(applicationService);

        mockMvc = MockMvcBuilders.standaloneSetup(applicationController)
                .setControllerAdvice(new ResponseExceptionHandler())
                .build();
    }


    @Test
    void shouldReturnOKStatusOnGetContactDetailsInvocation() throws Exception {
        when(applicationService.getContactDetails("PL/2020-10-05/1")).thenReturn(new ContactDetailsDto()
                .email("foo@bar")
                .phoneNumber("123321123345333"));

        mockMvc.perform(get("/applications/contact_details?application_number=PL/2020-10-05/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("foo@bar"))
                .andExpect(jsonPath("$.phoneNumber").value("123321123345333"));
    }


    @Test
    void shouldReturnNotFoundWhenThereIsNoSuchApplicationInDatabase() throws Exception {
        when(applicationService.getContactDetails("PL/2020-10-05/1"))
                .thenThrow(new PlaygroundException(FaultBuilder.NO_SUCH_APPLICATION));

        mockMvc.perform(get("/applications/contact_details?application_number=PL/2020-10-05/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("There is no such application with provided application_number!"));
    }

    @Test
    void shouldReturnOkResponseOnGetApplicationDetailsInvocation() throws Exception {
        when(applicationService.getApplicationDetails("foo@bar")).thenReturn(List.of(new ApplicationDetailsDto()
                .applicationType("APPLICATION_B")
                .applicationNumber("PL/2020-10-05/1")
                .lastName("KOWALSKI")));

        mockMvc.perform(get("/applications/application_details?email=foo@bar")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].applicationType").value("APPLICATION_B"))
                .andExpect(jsonPath("$[0].applicationNumber").value("PL/2020-10-05/1"))
                .andExpect(jsonPath("$[0].lastName").value("KOWALSKI"));
    }

    @Test
    void shouldReturnOkResponseWithMultipleResultsOnGetApplicationDetailsInvocation() throws Exception {
        when(applicationService.getApplicationDetails("foo@bar")).thenReturn(List.of(
                new ApplicationDetailsDto()
                        .applicationType("APPLICATION_B")
                        .applicationNumber("PL/2020-10-05/1")
                        .lastName("KOWALSKI"),
                new ApplicationDetailsDto()
                        .applicationType("APPLICATION_A")
                        .applicationNumber("PL/2020-10-05/2")
                        .lastName("KOWALSKI"),
                new ApplicationDetailsDto()
                        .applicationType("APPLICATION_C")
                        .applicationNumber("PL/2020-11-06/223")
                        .lastName("KOWALSKI")
        ));

        mockMvc.perform(get("/applications/application_details?email=foo@bar")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].applicationType").value("APPLICATION_B"))
                .andExpect(jsonPath("$[0].applicationNumber").value("PL/2020-10-05/1"))
                .andExpect(jsonPath("$[0].lastName").value("KOWALSKI"))
                .andExpect(jsonPath("$[1].applicationType").value("APPLICATION_A"))
                .andExpect(jsonPath("$[1].applicationNumber").value("PL/2020-10-05/2"))
                .andExpect(jsonPath("$[1].lastName").value("KOWALSKI"))
                .andExpect(jsonPath("$[2].applicationType").value("APPLICATION_C"))
                .andExpect(jsonPath("$[2].applicationNumber").value("PL/2020-11-06/223"))
                .andExpect(jsonPath("$[2].lastName").value("KOWALSKI"));
    }

    @Test
    void shouldReturnNotFoundWhenThereIsNoSuchEmailInDatabase() throws Exception {
        when(applicationService.getApplicationDetails("foo@bar"))
                .thenThrow(new PlaygroundException(FaultBuilder.NO_SUCH_EMAIL));

        mockMvc.perform(get("/applications/application_details?email=foo@bar")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("There is no application with provided email!"));
    }
}