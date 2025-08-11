package zgoo.app.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.app.dto.support.VocDto.VocBaseDto;
import zgoo.app.dto.support.VocDto.VocListDto;
import zgoo.app.service.VocService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class VocController {

    private final VocService vocService;

    // voc save
    @PostMapping("/voc/new")
    public ResponseEntity<String> createVoc(@Validated @RequestBody VocBaseDto dto, Principal principal) {
        log.info("=== create voc info ===");
        log.info("[VocController >> createVoc] voc: {}", dto.toString());

        try {
            boolean saved = this.vocService.saveVoc(dto, principal.getName());

            if (saved) {
                return ResponseEntity.ok("문의 등록이 완료되었습니다.");
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("요청이 잘못되었습니다. 다시 시도해주세요.");
        } catch (Exception e) {
            log.error("[VocController >> createVoc] error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("오류가 발생했습니다. 다시 시도해주세요.");
        }
    }

    // voc get by type
    @GetMapping("/voc/get")
    public ResponseEntity<List<VocListDto>> getVocByType(@RequestParam("type") String type, Principal principal) {
        log.info("=== get voc list ===");

        try {
            if ("ALL".equals(type)) {
                List<VocListDto> vocList = this.vocService.findVocAll(principal.getName());
                return ResponseEntity.ok(vocList);
            }

            List<VocListDto> vocList = this.vocService.getVocByType(type, principal.getName());
            return ResponseEntity.ok(vocList);
        } catch (Exception e) {
            log.error("[VocController >> getVocByType] error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // voc delete
    @PatchMapping("/voc/delete/{id}")
    public ResponseEntity<String> deleteVoc(@PathVariable("id") Long id) {
        log.info("=== delete voc info ===");
        log.info("delete vocId: {}", id);

        try {
            this.vocService.deleteVoc(id);
            return ResponseEntity.ok("삭제가 완료되었습니다.");
        } catch (Exception e) {
            log.error("[VocController >> deleteVoc] error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("삭제가 실패했습니다.");
        }
    }
}
