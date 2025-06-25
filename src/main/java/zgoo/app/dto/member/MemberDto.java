package zgoo.app.dto.member;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class MemberDto {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class MemberBaseDto {
        private Long memberId;
        private Long bizId;
        private String bizType;
        private String memLoginId;
        private String password;
        private String name;
        private String phoneNo;
        private String idTag;
        private String email;
        private String birth;
        private String zipCode;
        private String address;
        private String addressDetail;
        private String userState;
        private String creditcardStat;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class MemberRegDto extends MemberBaseDto {

        // 결제카드 정보
        private MemberCreditCardDto card;

        // 차량 정보
        private List<MemberCarDto> car;
    
        // 약관 정보
        private List<MemberConditionDto> condition;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MemberCreditCardDto {
        private String tid;
        private String cardNum;
        private String fnCode;
        private String fnCodeName;
        private String representativeCard;
        private LocalDateTime cardRegDt;
        private String tidCheck;
        private String representativeCardCheck;

        private String carNum1;
        private String carNum2;
        private String carNum3;
        private String carNum4;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MemberCarDto {
        private String carType;
        private String carTypeName;
        private String carNum;
        private String model;
        private LocalDateTime carRegDt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MemberConditionDto {
        private String conditionCode;
        private String conditionName;
        private String agreeVersion;
        private String sectionName;
        private String section;

        private String agreeYn;
        private String agreeYnCheck;
        private String stringAgreeDt;
        private LocalDateTime agreeDt;
    }
}
