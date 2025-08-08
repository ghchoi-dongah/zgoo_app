package zgoo.app.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.app.dto.support.VocDto.VocBaseDto;
import zgoo.app.service.VocService;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/voc")
public class VocController {

    private final VocService vocService;

    // voc 등록
    @PostMapping("/new")
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
}
