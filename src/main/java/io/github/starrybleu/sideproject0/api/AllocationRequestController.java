package io.github.starrybleu.sideproject0.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import java.util.Collections;

@Slf4j
@RestController
public class AllocationRequestController {

    @GetMapping(value = "/api/allocation-requests",
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    Page<AllocationRequestPayload> getAllocationRequests(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        // todo 구현
        return new PageImpl<>(Collections.emptyList());
    }

    @PostMapping(value = "/api/allocation-requests",
            consumes = MediaType.TEXT_PLAIN_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AllocationRequestPayload> createAllocationRequest(@Valid
                                                                     @Min(value = 1)
                                                                     @Max(value = 100)
                                                                     @RequestBody String address) {
        log.info("createAllocationRequest address: {}, ", address);
        // todo 인증 객체
        // todo 구현
        long dummyCreatedId = 13L;
        return ResponseEntity.created(URI.create("/api/allocation-requests/" + dummyCreatedId))
                .body(null);
    }

    @PatchMapping(value = "/api/allocation-requests",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    AllocationRequestPayload takeRequest(@Valid @RequestBody TakingAllocationRequestReqBody reqBody) {
        log.info("takeRequest reqBody: {}, ", reqBody);
        // todo 인증 객체
        // todo 구현
        return null;
    }

}
