package pl.pwpw.playground.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.pwpw.playground.application.dto.ApplicationDetailsDto;
import pl.pwpw.playground.application.dto.ContactDetailsDto;
import pl.pwpw.playground.application.service.ApplicationService;

import java.util.List;

@RestController
@RequestMapping("applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @GetMapping(value = "/contact_details")
    public ContactDetailsDto contactDetails(@RequestParam("application_number") final String applicationNumber) {
        return applicationService.getContactDetails(applicationNumber);
    }

    @GetMapping(value = "/application_details")
    public List<ApplicationDetailsDto> applicationDetails(@RequestParam final String email) {
        return applicationService.getApplicationDetails(email);
    }
}
