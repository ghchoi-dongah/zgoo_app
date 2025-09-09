package zgoo.app.repository.charge;

import java.util.List;

import zgoo.app.dto.charge.CsInfoDto.CsInfoDetailDto;

public interface CsRepositoryCustom {
    List<CsInfoDetailDto> findStationsWithinRadius(double lat, double lng, double radius);
    List<CsInfoDetailDto> searchStations(String keyword, String option);
    CsInfoDetailDto findStationOne(String stationId);
}
