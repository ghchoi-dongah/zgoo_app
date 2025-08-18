package zgoo.app.repository.hist;

import java.time.LocalDateTime;
import java.util.List;

import zgoo.app.dto.hist.ChargingHistDto;

public interface ChargingHistRepositoryCustom {
    List<ChargingHistDto> findAllByIdTag(String idTag, LocalDateTime startDate, LocalDateTime endDate);
}
