package zgoo.app.dto.code;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class CodeDto {
    @Data
    @SuperBuilder
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class CommCdBaseDto {
        private String grpCode;
        private String commonCode;
        private String commonCodeName;
    }
}
