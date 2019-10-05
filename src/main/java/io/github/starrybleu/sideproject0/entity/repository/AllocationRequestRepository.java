package io.github.starrybleu.sideproject0.entity.repository;

import io.github.starrybleu.sideproject0.entity.AllocationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AllocationRequestRepository extends JpaRepository<AllocationRequest, Integer>,
        JpaSpecificationExecutor<AllocationRequest> {
}
