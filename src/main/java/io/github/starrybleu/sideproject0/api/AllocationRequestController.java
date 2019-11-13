package io.github.starrybleu.sideproject0.api;

import io.github.starrybleu.sideproject0.api.exception.ForbiddenException;
import io.github.starrybleu.sideproject0.api.request.CreateAllocationRequestReqBody;
import io.github.starrybleu.sideproject0.api.response.AllocationRequestPayload;
import io.github.starrybleu.sideproject0.auth.AuthenticatedUser;
import io.github.starrybleu.sideproject0.entity.AllocationRequest;
import io.github.starrybleu.sideproject0.service.AllocationRequestService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
import java.net.URI;

@Slf4j
@RestController
public class AllocationRequestController {

    private final AllocationRequestService service;

    public AllocationRequestController(AllocationRequestService service) {
        this.service = service;
    }

    @ApiOperation(value = "승객 - 본인 요청 목록 조회", notes = "승객이 본인의 pagination 된 배차 요청 목록 조회")
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

    @ApiOperation(value = "기사 - 모든 요청 목록 조회", notes = "기사가 모든 pagination 된 배차 요청 목록 조회")
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
        return service.getAllocationRequests(pageable)
                .map(AllocationRequestPayload::from);
    }

    @ApiOperation(value = "승객 - 배차 요청", notes = "승객이 원하는 주소로 배차 요청")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping(value = "/api/allocation-requests",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AllocationRequestPayload> createAllocationRequest(@AuthenticationPrincipal AuthenticatedUser user,
                                                                            @Valid @RequestBody CreateAllocationRequestReqBody reqBody) {
        log.info("createAllocationRequest address: {}, ", reqBody);
        if (!user.isPassenger()) {
            throw new ForbiddenException("Only passenger can request allocation. user: " + user);
        }
        AllocationRequest createdEntity = service.createAllocationRequest(user, reqBody.getAddress());
        return ResponseEntity.created(URI.create("/api/allocation-requests/" + createdEntity.getId()))
                .body(AllocationRequestPayload.from(createdEntity));
    }

    @ApiOperation(value = "기사 - 배차", notes = "기사가 지정한 '배차 요청'에 대한 점유")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", required = true, dataType = "String", paramType = "header")
    })
    @PatchMapping(value = "/api/allocation-requests/{arId}/take",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AllocationRequestPayload takeRequest(@AuthenticationPrincipal AuthenticatedUser user,
                                                @PathVariable("arId") Integer arId) {
        log.info("takeRequest user: {}, ", user);
        if (!user.isDriver()) {
            throw new ForbiddenException("Only authorized driver can take allocation request. user: " + user);
        }
        return AllocationRequestPayload.from(service.takeRequest(arId, user.getId()));
    }

}
