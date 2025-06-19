package zgoo.app.dto.member;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class BizDto {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class BizBaseDto {
        private Long bizId;
        private String bizName;
        private String confirmCard; // 카드 등록 여부
    }
}
