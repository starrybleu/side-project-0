package io.github.starrybleu.sideproject0.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.starrybleu.sideproject0.entity.AllocationRequest;
import lombok.Data;

import java.time.LocalDateTime;

@Data
class AllocationRequestPayload {
    Integer id;
    AllocationRequest.Status status;
    Integer passengerNo;
    String address;
    Integer driverNo;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime allocatedAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime createdAt;

    static AllocationRequestPayload from(AllocationRequest entity) {
        AllocationRequestPayload payload = new AllocationRequestPayload();
        payload.id = entity.getId();
        payload.status = entity.getStatus();
        payload.passengerNo = entity.getPassengerNo();
        payload.address = entity.getAddress();
        payload.driverNo = entity.getDriverNo();
        payload.allocatedAt = entity.getAllocatedAt();
        payload.createdAt = entity.getCreatedAt();
        return payload;
    }
}
