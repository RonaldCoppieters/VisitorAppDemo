package be.pxl.VisitorsApplication.controller;

import be.pxl.VisitorsApplication.controller.resource.RegisterVisitorResource;
import be.pxl.VisitorsApplication.service.VisitorService;
import be.pxl.VisitorsApplication.util.exception.BadRequestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("visitors")
public class VisitorsController {
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHH:mm");
    private static final Logger LOGGER = LogManager.getLogger(VisitorsController.class);

    private final VisitorService visitorService;

    public VisitorsController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterVisitorResource resource) {

        long visitorId;

        try {
            visitorId = visitorService.register(resource);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getBadRequestResource());
        }

        return ResponseEntity.accepted().body(visitorId);
    }

    @GetMapping("{visitorId}/{entranceTimeStamp}")
    @ResponseBody
    public ResponseEntity<Object> validateEntry(@PathVariable long visitorId,
                                                @PathVariable String entranceTimeStamp) {
        LocalDateTime entryTime;

        try {
            entryTime = LocalDateTime.parse(entranceTimeStamp, TIMESTAMP_FORMAT);
        } catch (DateTimeParseException e) {
            entryTime = null;
        }

        try {
            visitorService.validateEntryAndSave(visitorId, entryTime);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getBadRequestResource());
        }

        return ResponseEntity.ok().build();
    }



}
