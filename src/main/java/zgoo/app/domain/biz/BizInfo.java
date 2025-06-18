package zgoo.app.domain.biz;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "BIZ_INFO")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class BizInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "biz_id")
    private Long id;

    @Column(name = "biz_no", unique = true)
    private String bizNo;

    @Column(name = "biz_name")
    private String bizName;

    @Column(name = "bid")
    private String bid;

    @Column(name = "card_num")
    private String cardNum;

    @Column(name = "card_name")
    private String cardName;

    @Column(name = "card_code")
    private String cardCode;

    @Column(name = "card_expire_month")
    private String cardExpireMonth;

    @Column(name = "card_expire_year")
    private String cardExpireYear;

    @Column(name = "terms_etf")
    private boolean termsEtf;

    @Column(name = "terms_rb")
    private boolean termsRb;

    @Column(name = "terms_privacy")
    private boolean termsPrivacy;

    @Column(name = "terms_privacy3rd")
    private boolean termsPrivacy3rd;

    @Column(name = "auth_date")
    private String authDate;

    @Column(name = "reg_dt")
    private LocalDateTime regDt;
}
