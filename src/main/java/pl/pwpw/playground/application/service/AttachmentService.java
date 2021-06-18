package pl.pwpw.playground.application.service;


import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.pwpw.playground.application.exception.FaultBuilder;
import pl.pwpw.playground.application.exception.PlaygroundException;
import pl.pwpw.playground.application.model.Application;
import pl.pwpw.playground.application.model.ApplicationNumber;
import pl.pwpw.playground.application.model.Attachment;
import pl.pwpw.playground.application.repository.ApplicationRepository;
import pl.pwpw.playground.application.repository.AttachmentRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final Logger LOGGER = Logger.getLogger(getClass().getName());
    private final AttachmentRepository attachmentRepository;
    private final ApplicationRepository applicationRepository;

    public void uploadFile(final String applicationNumber, final MultipartFile file) {
        checkFileType(file);
        try {
            attachmentRepository.save(new Attachment().addDate(LocalDateTime.now())
                    .fileType(file.getContentType())
                    .image(file.getBytes())
                    .application(getApplicationFromRepository(applicationNumber)));
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "IOException occurred on getBytes() method invocation: " + ex.getMessage());
            throw new PlaygroundException(FaultBuilder.CANNOT_TRANSFORM_FILE);
        }
    }

    private void checkFileType(final MultipartFile file) {
        if (!isRequestedType(file.getContentType())) {
            throw new PlaygroundException(FaultBuilder.WRONG_FILE_TYPE);
        }
    }

    private boolean isRequestedType(final String fileType) {
        return MediaType.APPLICATION_PDF_VALUE.equals(fileType) || MediaType.IMAGE_JPEG_VALUE.equals(fileType);
    }

    private Application getApplicationFromRepository(final String applicationNumber) {
        return applicationRepository
                .findByApplicationNumber(new ApplicationNumber(applicationNumber))
                .orElseThrow(() -> new PlaygroundException(FaultBuilder.NO_SUCH_APPLICATION));
    }

}
