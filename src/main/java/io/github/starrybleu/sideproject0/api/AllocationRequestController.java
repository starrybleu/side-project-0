package io.github.starrybleu.sideproject0.api;

import io.github.starrybleu.sideproject0.entity.AllocationRequest;
import io.github.starrybleu.sideproject0.service.AllocationRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.net.URI;

@Slf4j
@RestController
public class AllocationRequestController {

    private final AllocationRequestService service;

    public AllocationRequestController(AllocationRequestService service) {
        this.service = service;
    }

    @GetMapping(value = "/api/allocation-requests",
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<AllocationRequestPayload> getAllocationRequests(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return service.getAllocationRequests(pageable)
                .map(AllocationRequestPayload::from);
    }

    @PostMapping(value = "/api/allocation-requests",
            consumes = MediaType.TEXT_PLAIN_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AllocationRequestPayload> createAllocationRequest(@Valid
                                                                     @Min(value = 1)
                                                                     @Max(value = 100)
                                                                     @RequestBody String address) {
        log.info("createAllocationRequest address: {}, ", address);
        // todo 인증 객체
        int dummyPassengerNo = 1377;
        AllocationRequest createdEntity = service.createAllocationRequest(dummyPassengerNo, address);
        return ResponseEntity.created(URI.create("/api/allocation-requests/" + createdEntity.getId()))
                .body(AllocationRequestPayload.from(createdEntity));
    }

    @PatchMapping(value = "/api/allocation-requests/{arId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AllocationRequestPayload takeRequest(@PathVariable("arId") Integer arId,
                                         @Valid @RequestBody TakingAllocationRequestReqBody reqBody) {
        log.info("takeRequest reqBody: {}, ", reqBody);
        // todo 인증 객체
        // todo 인증 객체의 userId 와 reqBody 의 driverNo 일치 여부 확인. Otherwise, 403 forbidden
        return AllocationRequestPayload.from(service.takeRequest(arId, reqBody));
    }

}
