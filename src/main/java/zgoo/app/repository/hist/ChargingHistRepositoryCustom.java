package zgoo.app.repository.hist;

import java.time.LocalDateTime;
import java.util.List;

import zgoo.app.dto.hist.ChargingHistDto.ChgHistListDto;
import zgoo.app.dto.hist.ChargingHistDto.ChgHistSummaryDto;

public interface ChargingHistRepositoryCustom {
    List<ChgHistListDto> findAllByIdTag(String idTag, LocalDateTime startDate, LocalDateTime endDate);
    ChgHistSummaryDto getCurrentMonthChgSummary(String idTag);
}
