package io.github.starrybleu.sideproject0.entity;

import io.github.starrybleu.sideproject0.api.TakingAllocationRequestReqBody;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "allocation_request")
@EntityListeners(AuditingEntityListener.class)
public class AllocationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id; // DDL 에서 INTEGER PRIMARY 가 되어야 mysql 의 auto_increment 처럼 잘 작동함

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "passenger_no")
    private Integer passengerNo;

    @Column(name = "address")
    private String address;

    @Column(name = "driver_no")
    private Integer driverNo;

    @Column(name = "allocated_at")
    private LocalDateTime allocatedAt;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdatedAt;

    AllocationRequest() {
    }

    public static AllocationRequest create(Integer passengerNo, String requestedAddress) {
        AllocationRequest entity = new AllocationRequest();
        entity.status = Status.stand_by;
        entity.passengerNo = passengerNo;
        entity.address = requestedAddress;
        return entity;
    }

    public void takenRequestByDriver(TakingAllocationRequestReqBody reqBody) {
        this.status = Status.allocated;
        this.driverNo = reqBody.getDriverNo();
        this.allocatedAt = LocalDateTime.now();
    }

    public enum Status {
        stand_by, allocated // note 나중에 운행 완료, 취소 등 추가 가능
    }

}
