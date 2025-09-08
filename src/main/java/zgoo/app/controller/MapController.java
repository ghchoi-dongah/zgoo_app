package zgoo.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.app.dto.charge.CsInfoDto.CsInfoDetailDto;
import zgoo.app.service.MapService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MapController {

    private final MapService mapService;

    @GetMapping("/map/nearby")
    public ResponseEntity<Map<String, Object>> getNearbyStations(@RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude) {
        log.info("=== find nearby charging stations ===");

        Map<String, Object> response = new HashMap<>();

        try {
            List<CsInfoDetailDto> csList = this.mapService.findNearbyStations(latitude, longitude);

            response.put("csList", csList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[MapController >> getNearbyStations] error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}
