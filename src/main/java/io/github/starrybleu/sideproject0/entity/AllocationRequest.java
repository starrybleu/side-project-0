package io.github.starrybleu.sideproject0.entity;

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
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "passenger_no")
    private Long passengerNo;

    @Column(name = "address")
    private String address;

    @Column(name = "driver_no")
    private Long driverNo;

    @Column(name = "allocated_at")
    private LocalDateTime allocatedAt;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdatedAt;

    public enum Status {
        stand_by, allocated // todo 나중에 운행 완료, 취소 등 추가 가능
    }

}
