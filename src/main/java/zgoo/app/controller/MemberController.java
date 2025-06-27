package zgoo.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.app.dto.member.BizDto.BizBaseDto;
import zgoo.app.dto.member.MemberDto.MemberRegDto;
import zgoo.app.service.MemberService;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    // 회원 등록
    @PostMapping("/new")
    public ResponseEntity<String> createMember(@Validated @RequestBody MemberRegDto dto) {
        log.info("=== create member info ===");
        log.info("[MemberController >> createMember] memberDto >> {}", dto);

        try {
            boolean saved = this.memberService.saveMember(dto);
            if (saved) {
                return ResponseEntity.ok("회원가입이 완료되었습니다.");
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("요청이 잘못되었습니다. 다시 시도해주세요.");
        } catch (Exception e) {
            log.error("[MemberController >> createMember] error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("오류가 발생했습니다. 다시 시도해주세요.");
        }
    }

    // 회원ID 중복검사
    @GetMapping("/dupcheck")
    public ResponseEntity<Boolean> checkMemLoginId(@RequestParam("memLoginId") String memLoginId) {
        log.info("=== duplicate check member login id ===");

        try {
            boolean response = this.memberService.isMemLoginIdDuplicate(memLoginId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[MemberController >> checkMemLoginId] error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 법인 조회
    @GetMapping("/search")
    public ResponseEntity<List<BizBaseDto>> searchBizInfo(@RequestParam("bizName") String bizName) {
        log.info("=== search biz info ===");

        try {
            List<BizBaseDto> bizList = this.memberService.findBizInfo(bizName);
            return ResponseEntity.ok(bizList);
        } catch (Exception e) {
            log.error("[MemberController >> searchBizInfo] error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 아이디 찾기
    @GetMapping("/find/id")
    public ResponseEntity<Optional<String>> findMemLoginId(@RequestParam("name") String name, @RequestParam("phone") String phone)  {
        log.info("=== find memLoinId ===");
        log.info("[MemberController >> findMemLoginId] name: {}, phone: {}", name, phone);

        try {
            Optional<String> memLoginId = this.memberService.findMemLoginId(name, phone);

            if (ObjectUtils.isEmpty(memLoginId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            return ResponseEntity.ok(memLoginId);
        } catch (Exception e) {
            log.error("[MemberController >> findMemLoginId] error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 비밀번호 찾기
    @PostMapping("/find/pw")
    public ResponseEntity<Map<String, Object>> findPwSubmit(@RequestParam("name") String name, @RequestParam("memLoginId") String memLoginId,
            @RequestParam("phone") String phone, HttpSession session) {
        log.info("=== find password ===");
        log.info("[MemberController >> findPwSubmit] name: {}, memLoginId: {}", name, memLoginId);

        Map<String, Object> response = new HashMap<>();

        try {
            session.setAttribute("resetLoginId", memLoginId);

            Boolean result = this.memberService.findMemberInfoInPw(name, memLoginId, phone);
            response.put("result", result);
            response.put("url", "/member/reset-pw");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("url", "/member/find-pw");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 비밀번호 재설정
    @PatchMapping("/reset/pw")
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestParam("memLoginId") String memLoginId, @RequestParam("newPw") String newPw) {
        log.info("=== find password ===");
        log.info("[MemberController >> resetPassword] memLoginId: {}, newPw: {}", memLoginId, newPw);

        Map<String, Object> response = new HashMap<>();

        try {
            Boolean result = this.memberService.updatePassword(memLoginId, newPw);
            response.put("result", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
