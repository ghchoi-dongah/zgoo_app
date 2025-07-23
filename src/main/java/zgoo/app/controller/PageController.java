package zgoo.app.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.groovy.runtime.ObjectUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.app.dto.member.MemberDto.MemberConditionDto;
import zgoo.app.dto.support.NoticeDto.NoticeListDto;
import zgoo.app.service.MemberService;
import zgoo.app.service.NoticeService;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PageController {

    private final MemberService memberService;
    private final NoticeService noticeService;

    /* 
     * 메인
     */
    @GetMapping("/main")
    public String showmain(Model model) {
        log.info("=== Main Page ===");

        try {
            List<NoticeListDto> noticeList = this.noticeService.getRecentNotices();
            model.addAttribute("noticeList", noticeList);

        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("noticeList", Collections.emptyList());
        }
        
        return "pages/main";
    }

    /* 
     * 충전소 찾기
     */
    @GetMapping("/find-charger")
    public String showfindcharger(Model model) {
        log.info("=== Find Charger Page ===");

        try {

        } catch (Exception e) {
            e.getStackTrace();
        }

        return "pages/find_charger";
    }

    /* 
     * 전체 메뉴
     */
    @GetMapping("/full-menu")
    public String showfullmenu(Model model, Principal principal) {
        log.info("=== Full Menu Page ===");

        try {

            if (principal != null) {
                String memLoginId = principal.getName();
                log.info("memLoginId: {}", memLoginId);

                Optional<String> name = this.memberService.getMemberName(memLoginId);
                model.addAttribute("name", name.orElse(""));
            }

        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("name", "");
        }

        return "pages/full_menu";
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
