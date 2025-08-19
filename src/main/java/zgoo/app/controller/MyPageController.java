package zgoo.app.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.app.dto.hist.ChargingHistDto.ChgHistListDto;
import zgoo.app.dto.member.MemberDto.MemberPasswordDto;
import zgoo.app.dto.member.MemberDto.MemberRegDto;
import zgoo.app.dto.support.FaqDto.FaqBaseDto;
import zgoo.app.dto.support.NoticeDto.NoticeListDto;
import zgoo.app.service.FaqService;
import zgoo.app.service.MyPageService;
import zgoo.app.service.NoticeService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MyPageController {

    private final MyPageService myPageService;
    private final NoticeService noticeService;
    private final FaqService faqService;

    // 회원정보 수정
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/update/member")
    public ResponseEntity<String> updateMember(@RequestBody MemberRegDto dto, Principal principal) {
        log.info("=== update member info ===");
        log.info("[MyPageController >> updateMember] dto: {}", dto.toString());

        try {
            Integer result = this.myPageService.updateMemberInfo(dto, principal.getName());

            return switch (result) {
                case 1 -> ResponseEntity.status(HttpStatus.OK).body("수정이 완료되었습니다.");
                default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("수정에 실패했습니다.");
            };

        } catch (Exception e) {
            log.error("[MyPageController >> updateMember] error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("수정에 실패했습니다.");
        }
    }

    // 비밀번호 변경
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/update/pw")
    public ResponseEntity<Map<String, Object>> updatePassword(@RequestBody MemberPasswordDto dto, Principal principal) {
        log.info("=== update passsword info ===");

        Map<String, Object> response = new HashMap<>();

        try {
            Integer result = this.myPageService.updatePasswordInfo(dto, principal.getName());

            switch (result) {
                case 0 -> response.put("message", "현재 비밀번호가 올바르지 않습니다.");
                case 1 -> response.put("message", "비밀번호가 변경되었습니다.");
                case 2 -> response.put("message", "새 비밀번호 값이 일치하지 않습니다.");
                default -> response.put("message", "비밀번호 변경에 실패했습니다.");
            }

            response.put("state", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[MyPageController >> updatePassword] error: {}", e.getMessage(), e);
            response.put("message", "비밀번호 변경 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 유형별 공지조회
    @GetMapping("/notice/get")
    public ResponseEntity<List<NoticeListDto>> getNoticesByType(@RequestParam("code") String code) {
        log.info("=== get notice list ====");

        try {
            if ("N1".equals(code)) {
                List<NoticeListDto> noticeList = this.noticeService.findNoticeAll();
                return ResponseEntity.ok(noticeList);
            } 
            
            List<NoticeListDto> noticeList = this.noticeService.getNoticesByType(code);
            return ResponseEntity.ok(noticeList);
        } catch (Exception e) {
            log.error("[MyPageController >> getNoticesByType] error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 유형별 FAQ
    @GetMapping("/faq/get")
    public ResponseEntity<List<FaqBaseDto>> getFaqBySection(@RequestParam("section") String section) {
        log.info("=== get faq list ===");

        try {
            if ("ALL".equals(section)) {
                List<FaqBaseDto> faqList = this.faqService.findFaqAll();
                return ResponseEntity.ok(faqList);
            }
            
            List<FaqBaseDto> faqList = this.faqService.getFaqBySection(section);
            return ResponseEntity.ok(faqList);
        } catch (Exception e) {
            log.error("[MyPageController >> getFaqBySection] error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 충전이력 조회
    @GetMapping("/chghist/get")
    public ResponseEntity<List<ChgHistListDto>> getChgHist(@RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate, Principal principal) {
        log.info("=== get charge hist list ===");

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            startDate = fixYearMonth(startDate);
            endDate = fixYearMonth(endDate);

            LocalDate startOfMonth = YearMonth.parse(startDate, formatter).atDay(1);
            LocalDate endOfMonth = YearMonth.parse(endDate, formatter).atEndOfMonth();

            List<ChgHistListDto> histList = this.myPageService.findChgHistAll(principal.getName(), 
                startOfMonth, endOfMonth);
            return ResponseEntity.ok(histList);
        } catch (Exception e) {
            log.error("[MyPageController >> getChgHist] error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 한 자리 월을 두 자리로 보정
    private String fixYearMonth(String ym) {
        String[] parts = ym.split("-");
        String year = parts[0];
        String month = parts[1].length() == 1 ? "0" + parts[1] : parts[1];
        return year + "-" + month;
    }
}
