package zgoo.app.dto.charge;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class CsInfoDto {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class CsInfoBaseDto {
        private String stationId;
        private String stationName;
        private Double latitude;
        private Double longitude;
        private Integer distance;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder(toBuilder = true)
    @ToString(callSuper = true)
    public static class CsInfoDetailDto extends CsInfoBaseDto {
        private String opStatus;
        private String opStatusName;
        private String parkingFeeYn;
        private String address;
        private String addressDetail;
        private LocalTime openStartTime;
        private LocalTime openEndTime;
    }
}
