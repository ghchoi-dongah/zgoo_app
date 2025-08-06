package zgoo.app.dto.support;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class NoticeDto {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class NoticeBaseDto {
        private Long noticeId;
        private Long views;
        private String title;
        private String type;
        private String delYn;
        private LocalDateTime regDt;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class NoticeListDto extends NoticeBaseDto {
        private String typeName;
        private boolean isNew;
        private LocalDate startDate;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class NoticeDetailDto extends NoticeBaseDto {
        private String typeName;
        private String content;
    }
}
