package io.github.starrybleu.sideproject0.service;

import io.github.starrybleu.sideproject0.api.exception.UnprocessableException;
import io.github.starrybleu.sideproject0.api.request.TakingAllocationRequestReqBody;
import io.github.starrybleu.sideproject0.auth.AuthenticatedUser;
import io.github.starrybleu.sideproject0.entity.AllocationRequest;
import io.github.starrybleu.sideproject0.entity.repository.AllocationRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
public class AllocationRequestService {

    private final AllocationRequestRepository repository;

    public AllocationRequestService(AllocationRequestRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<AllocationRequest> getMyAllocationRequests(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional
    public AllocationRequest createAllocationRequest(AuthenticatedUser user, String address) {
        AllocationRequest entity = AllocationRequest.create(user.getId(), address);
        return repository.save(entity);
    }

    @Transactional
    public AllocationRequest takeRequest(Integer arId, TakingAllocationRequestReqBody reqBody) {
        AllocationRequest entity = repository.findById(arId)
                .orElseThrow(EntityNotFoundException::new);
        if (entity.getDriverNo() != null) {
            throw new UnprocessableException("The given allocation-request had already been taken");
        }
        entity.takenRequestByDriver(reqBody);
        return repository.save(entity);
    }

    public Page<AllocationRequest> getMyAllocationRequests(Integer userId, Pageable pageable) {
        Specification<AllocationRequest> spec = (root, query, cb) -> cb.equal(root.get("id"), userId);
        return repository.findAll(spec, pageable);
    }
}
