package zgoo.app.dto.support;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class FaqDto {

    @Data
    @SuperBuilder
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class FaqBaseDto {
        private Long faqId;
        private String title;
        private String content;
        private String delYn;
        private String section;
        private String sectionName;
        private LocalDateTime regDt;
    }
}
