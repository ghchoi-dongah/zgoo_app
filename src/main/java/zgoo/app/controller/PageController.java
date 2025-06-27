package zgoo.app.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.app.dto.member.MemberDto.MemberConditionDto;
import zgoo.app.service.MemberService;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PageController {

    private final MemberService memberService;

    /* 
     * 메인
     */
    @GetMapping("/main")
    public String showmain(Model model) {
        log.info("=== Main Page ===");

        try {

        } catch (Exception e) {

        }
        
        return "pages/main";
    }

    /* 
     * 로그인
     */
    @GetMapping("/login")
    public String showlogin(Model model) {
        log.info("=== Login Page ===");

        try {

        } catch (Exception e) {

        }

        return "pages/member/login";
    }

    /* 
     * 아이디 찾기
     */
    @GetMapping("/member/find-id")
    public String showfindid(Model model) {
        log.info("=== Find Id Page ===");

        try {

        } catch (Exception e) {

        }

        return "pages/member/find_id";
    }

    /* 
     * 비밀번호 찾기
     */
    @GetMapping("/member/find-pw")
    public String showfindpw(Model model) {
        log.info("=== Find Pw Page ===");
        
        try {

        } catch (Exception e) {

        }

        return "pages/member/find_pw";
    }

    /* 
     * 비밀번호 재설정
     */
    @GetMapping("/member/reset-pw")
    public String showresetpw(HttpSession session, Model model) {
        log.info("=== Reset Pw Page ===");

        try {
            String loginId = (String) session.getAttribute("resetLoginId");
            log.info("[PageController >> showresetpw] loginId: {}", loginId);
            
            if (ObjectUtils.isEmpty(loginId)) {
                return "redirect:/member/find-pw";
            }

            model.addAttribute("memLoginId", loginId);
            return "pages/member/reset_pw";
        } catch (Exception e) {
            e.getStackTrace();
            return "redirect:/member/find-pw";
        }
    }

    /*
     * 회원가입
     */
    @GetMapping("/member/join")
    public String showjoin(Model model) {
        log.info("=== Join Page ===");

        try {
            /* 약관코드 조회 */
            List<MemberConditionDto> conList = this.memberService.findAllConditionList();
            model.addAttribute("conList", conList);
        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("conList", Collections.emptyList());
        }

        return "pages/member/join";
    }
}
