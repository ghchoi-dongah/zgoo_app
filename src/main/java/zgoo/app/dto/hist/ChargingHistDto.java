package zgoo.app.dto.hist;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class ChargingHistDto {

    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    public static class ChgHistListDto {
        private String stationId;
        private String stationName;
        private String memberName;
        private String idTag;
        private Integer chgTime;
        private Integer soc;
        private Integer prepayCost;
        private Integer cancelCost;
        private Integer realCost;
        private BigDecimal chgAmount;
        private BigDecimal chgPrice;
        private BigDecimal unitCost;
        private LocalDateTime chgStartTime;
        private LocalDateTime chgEndTime;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChgHistSummaryDto {
        private Long chgCnt;
        private Integer realCost;
        private BigDecimal chgAmount;
    }
}
