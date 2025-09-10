package zgoo.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.app.dto.charge.CsInfoDto.CsInfoDetailDto;
import zgoo.app.repository.charge.CsRepository;
import zgoo.app.util.CodeConstants;

@Service
@RequiredArgsConstructor
@Slf4j
public class MapService {

    private final CsRepository csRepository;

    public List<CsInfoDetailDto> findNearbyStations(double lat, double lng) {
        try {
            List<CsInfoDetailDto> csList = this.csRepository.findStationsWithinRadius(lat, lng, CodeConstants.RADIUS);
            log.info("[MapService >> findNearbyStations] csList: {}", csList.toString());
            return csList;
        } catch (Exception e) {
            log.error("[MapService >> findNearbyStations] error: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public CsInfoDetailDto findStationOne(String stationId) {
        try {
            CsInfoDetailDto csInfo = this.csRepository.findStationOne(stationId);
            log.info("[MapService >> findStationOne] csInfo: {}", csInfo.toString());
            return csInfo;
        } catch (Exception e) {
            log.error("[MapService >> findStationOne] error: {}", e.getMessage(), e);
            return null;
        }
    }

    public List<CsInfoDetailDto> searchStations(String keyword, String option) {
        try {
            List<CsInfoDetailDto> csList = this.csRepository.searchStations(keyword, option);
            log.info("[MapService >> searchStations] csList: {}", csList.toString());
            return csList;
        } catch (Exception e) {
            log.error("[MapService >> searchStations] error: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public List<CsInfoDetailDto> findNearbyStationsTop3(double lat, double lng) {
        try {
            List<CsInfoDetailDto> csList = this.csRepository.findStationsWithinRadiusTop3(lat, lng, CodeConstants.RADIUS);
            log.info("[MapService >> findNearbyStationsTop3] csList: {}", csList.toString());
            return csList;
        } catch (Exception e) {
            log.error("[MapService >> findNearbyStationsTop3] error: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
}
