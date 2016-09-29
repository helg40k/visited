package mobi.visited.resource;

import javassist.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

@ControllerAdvice
public class ResourceExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Void> notFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    @RequestMapping(produces = "text/html")
    public ResponseEntity<String> toResponse(Exception e) {
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        StringBuilder message = new StringBuilder(e.getMessage());
        message.append("\n\n");
        Arrays.asList(stackTraceElements)
            .forEach(element -> message.append(element.toString()));

        LOG.error(e.getMessage(), e);

        return ResponseEntity
            .status(500)
            .body(message.toString());
    }
}
