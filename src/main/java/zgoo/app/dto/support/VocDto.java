package zgoo.app.dto.support;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class VocDto {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class VocBaseDto {
        private Long vocId;
        private String type;
        private String title;
        private String content;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class VocListDto extends VocBaseDto {
        private String typeName;
        private String replyStat;
        private String replyStatName;
        private LocalDateTime regDt;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class VocDetailDto extends VocBaseDto {
        private String typeName;
        private String replyStatName;
        private String replyContent;
        private LocalDateTime regDt;
        private LocalDateTime replyDt;
    }
}
