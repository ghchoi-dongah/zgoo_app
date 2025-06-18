package zgoo.app.domain.member;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "MEMBER_AUTH")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class MemberAuth {

    @Id
    @Column(name = "id_tag")
    private String idTag;

    @Column(name = "expire_date")
    private LocalDate expireDate;

    @Column(name = "use_yn", length = 1)
    private String useYn;

    @Column(name = "parent_id_tag")
    private String parentIdTag;

    @Column(name = "total_charging_power", precision = 12, scale = 3)
    private BigDecimal totalChargingPower;

    @Column(name = "status")
    private String status;

    @Column(name = "total_charging_price")
    private Long totalChargingPrice;

    @Column(name = "reg_dt")
    private LocalDateTime regDt;

    @Column(name = "mod_dt")
    private LocalDateTime modDt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
