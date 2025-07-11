package com.itwillbs.factron.entity;

import com.itwillbs.factron.entity.Approval;
import com.itwillbs.factron.entity.Client;
import com.itwillbs.factron.entity.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "purchase")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchase_seq")
    @SequenceGenerator(name = "purchase_seq", sequenceName = "purchase_seq", allocationSize = 1)
    private Long id; // 발주 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee; // 발주 등록자 (사원 ID)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
    private Client client; // 거래처 ID

    @Column(name = "status_code", length = 6, nullable = false)
    private String statusCode; // 상태 코드 (예: 대기, 완료, 반려, 취소)

    @Column(name = "created_at", updatable = false)
    private LocalDate createdAt; // 발주 등록일

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approval_id", referencedColumnName = "id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Approval approval; // 결재 정보 (발주 결재 정보)

    public void updateStatus(String statusCode) {
        this.statusCode = statusCode;
    }

    // 결재 정보 변경용 (예: 결재 취소 시 null로 설정)
    public void setApproval(Approval approval) {
        this.approval = approval;
    }
}
