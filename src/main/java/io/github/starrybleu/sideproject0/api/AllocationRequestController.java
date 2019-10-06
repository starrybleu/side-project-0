package io.github.starrybleu.sideproject0.api;

import io.github.starrybleu.sideproject0.api.exception.ForbiddenException;
import io.github.starrybleu.sideproject0.api.request.TakingAllocationRequestReqBody;
import io.github.starrybleu.sideproject0.api.response.AllocationRequestPayload;
import io.github.starrybleu.sideproject0.auth.AuthenticatedUser;
import io.github.starrybleu.sideproject0.entity.AllocationRequest;
import io.github.starrybleu.sideproject0.service.AllocationRequestService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", required = true, dataType = "String", paramType = "header")
    })
    @GetMapping(value = "/api/allocation-requests/mine",
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<AllocationRequestPayload> getAllocationRequestsForPassenger(@AuthenticationPrincipal AuthenticatedUser user,
                                                                            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        if (!user.isPassenger()) {
            throw new ForbiddenException("Only passenger can read list of own requests.");
        }
        return service.getMyAllocationRequests(user.getId(), pageable)
                .map(AllocationRequestPayload::from);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", required = true, dataType = "String", paramType = "header")
    })
    @GetMapping(value = "/api/allocation-requests",
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<AllocationRequestPayload> getAllocationRequestsForDriver(@AuthenticationPrincipal AuthenticatedUser user,
                                                                         @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        if (!user.isDriver()) {
            throw new ForbiddenException("Only authorized driver can read list of requests.");
        }
        return service.getMyAllocationRequests(pageable)
                .map(AllocationRequestPayload::from);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping(value = "/api/allocation-requests",
            consumes = MediaType.TEXT_PLAIN_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AllocationRequestPayload> createAllocationRequest(@AuthenticationPrincipal AuthenticatedUser user,
                                                                            @Valid
                                                                            @Min(value = 1)
                                                                            @Max(value = 100)
                                                                            @RequestBody String address) {
        log.info("createAllocationRequest address: {}, ", address);
        if (!user.isPassenger()) {
            throw new ForbiddenException("Only passenger can request allocation. user: " + user);
        }
        AllocationRequest createdEntity = service.createAllocationRequest(user, address);
        return ResponseEntity.created(URI.create("/api/allocation-requests/" + createdEntity.getId()))
                .body(AllocationRequestPayload.from(createdEntity));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", required = true, dataType = "String", paramType = "header")
    })
    @PatchMapping(value = "/api/allocation-requests/{arId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AllocationRequestPayload takeRequest(@AuthenticationPrincipal AuthenticatedUser user,
                                                @PathVariable("arId") Integer arId,
                                                @Valid @RequestBody TakingAllocationRequestReqBody reqBody) {
        log.info("takeRequest reqBody: {}, ", reqBody);
        boolean isDriverIdMatch = reqBody.getDriverNo().equals(user.getId());
        if (!user.isDriver() || !isDriverIdMatch) {
            throw new ForbiddenException("Only authorized driver can take allocation request. user: " + user + ", match: " + isDriverIdMatch);
        }
        return AllocationRequestPayload.from(service.takeRequest(arId, reqBody));
    }

}
