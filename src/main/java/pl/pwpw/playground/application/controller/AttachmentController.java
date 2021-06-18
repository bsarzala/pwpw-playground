package pl.pwpw.playground.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.pwpw.playground.application.service.AttachmentService;


@RestController
@RequestMapping("attachments")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping(value = "/files", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> uploadFile(@RequestParam("application_number") final String applicationNumber, @RequestParam final MultipartFile file) {
        attachmentService.uploadFile(applicationNumber, file);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
