package zgoo.app.domain.code;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "COMMON_CODE", indexes = {
        @Index(name = "idx_common_code_grp", columnList = "grp_code")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class CommonCode {
    @Id
    @Column(name = "common_code")
    private String commonCode;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "reserved", length = 100)
    private String reserved;

    @Column(name = "ref_code1", length = 10)
    private String refCode1;

    @Column(name = "ref_code2", length = 10)
    private String refCode2;

    @Column(name = "ref_code3", length = 10)
    private String refCode3;

    @Column(name = "reg_user_id", length = 20)
    private String regUserId;

    private LocalDateTime regDt;

    @Column(name = "mod_user_id", length = 20)
    private String modUserId;

    private LocalDateTime modDt;

    @JoinColumn(name = "grp_code")
    @ManyToOne(fetch = FetchType.LAZY)
    private GrpCode group;
}
