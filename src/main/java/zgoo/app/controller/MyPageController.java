package zgoo.app.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.app.dto.member.MemberDto.MemberPasswordDto;
import zgoo.app.service.MemberService;
import zgoo.app.service.MyPageService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MyPageController {

    private final MemberService memberService;
    private final MyPageService myPageService;

    // 회원정보 수정
    // @PatchMapping("/member/update")
    // public ResponseEntity<String> updateMember() {
        
    // }

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
}
