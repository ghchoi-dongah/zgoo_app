package zgoo.app.dto.member;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ConditionDto {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class ConditionBaseDto {
        private String conditionCode;
        private String conditionName;
        private String agreeVersion;
        private String section;
        private LocalDateTime regDt;
    }
}
