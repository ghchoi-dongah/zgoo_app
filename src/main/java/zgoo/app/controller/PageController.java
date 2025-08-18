package zgoo.app.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.app.dto.code.CodeDto.CommCdBaseDto;
import zgoo.app.dto.hist.ChargingHistDto;
import zgoo.app.dto.member.MemberDto.MemberConditionDto;
import zgoo.app.dto.member.MemberDto.MemberRegDto;
import zgoo.app.dto.support.FaqDto.FaqBaseDto;
import zgoo.app.dto.support.NoticeDto.NoticeDetailDto;
import zgoo.app.dto.support.NoticeDto.NoticeListDto;
import zgoo.app.dto.support.VocDto.VocDetailDto;
import zgoo.app.dto.support.VocDto.VocListDto;
import zgoo.app.service.CommonService;
import zgoo.app.service.FaqService;
import zgoo.app.service.MemberService;
import zgoo.app.service.MyPageService;
import zgoo.app.service.NoticeService;
import zgoo.app.service.VocService;
import zgoo.app.util.CodeConstants;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PageController {

    private final MemberService memberService;
    private final MyPageService myPageService;
    private final NoticeService noticeService;
    private final CommonService commonService;
    private final FaqService faqService;
    private final VocService vocService;

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
     * QR scan
     */
    @GetMapping("/qrscan")
    public String showqrscan(Model model) {
        log.info("=== QR Scan Page ===");

        try {
            return "pages/qr";
        } catch (Exception e) {
            e.getStackTrace();
            return "redirect:/main";
        }
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

    /* 
     * 개인정보수정
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage/info")
    public String showupdatemember(MemberRegDto memberForm, Model model, Principal principal) {
        log.info("=== Update Member Info Page ===");

        try {
            MemberRegDto member = this.myPageService.findMemberInfo(principal.getName());
            if (!member.getMemLoginId().equals(principal.getName())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
            }

            model.addAttribute("memberForm", member);
        } catch (Exception e) {
            e.getStackTrace();
        }

        return "pages/member_update/member_update";
    }

    /* 
     * 비밀번호 변경
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage/info/pw")
    public String showupdatepw(Model model, Principal principal) {
        log.info("=== Update Password Page ===");

        try {

        } catch (Exception e) {

        }

        return "pages/member_update/pw_update";
    }

    /*
     * 전화번호 변경
     * */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage/info/phone")
    public String showupdatephone(Model model, Principal principal) {
        log.info("=== Update Phone Info Page ===");

        try {

        } catch (Exception e) {

        }

        return "pages/member_update/phone_update";
    }

    /* 
     * 공지사항
     */
    @GetMapping("/mypage/notice")
    public String shownotice(Model model) {
        log.info("=== Notice Page ===");

        try {
            List<NoticeListDto> noticeList = this.noticeService.findNoticeAll();
            model.addAttribute("noticeList", noticeList);

            List<CommCdBaseDto> typeList = this.commonService.findCommonCdNamesByGrpcd(CodeConstants.NOTICETYPECD);
            model.addAttribute("typeList", typeList);
        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("noticeList", Collections.emptyList());
            model.addAttribute("typeList", Collections.emptyList());
        }

        return "pages/support/notice";
    }

    /* 
     * 공지사항(상세)
     */
    @GetMapping("/mypage/notice/detail/{id}")
    public String shownoticedetail(Model model, @PathVariable("id") Long id) {
        log.info("=== Detail Notice Page ===");

        try {
            NoticeDetailDto notice = this.noticeService.findNoticeDetailOne(id);
            String content = notice.getContent().replace("\n", "<br>");
            model.addAttribute("notice", notice);
            model.addAttribute("content", content);

            return "pages/support/notice_detail";
        } catch (Exception e) {
            e.getStackTrace();
            return "pages/support/notice";
        }
    }

    /* 
     * 자주하는 질문
     */
    @GetMapping("/mypage/faq")
    public String showfaq(Model model) {
        log.info("=== Faq Page ===");

        try {
            List<FaqBaseDto> faqList = this.faqService.findFaqAll();
            model.addAttribute("faqList", faqList);

            List<CommCdBaseDto> typeList = this.commonService.findCommonCdNamesByGrpcd(CodeConstants.FAQKIND);
            model.addAttribute("typeList", typeList);
        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("typeList", Collections.emptyList());
        }

        return "pages/support/faq";
    }

    /* 
     * 1:1 문의
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage/voc")
    public String showvoc(Model model) {
        log.info("=== Voc Page ===");

        try {
            List<CommCdBaseDto> typeList = this.commonService.findCommonCdNamesByGrpcd(CodeConstants.VOCTYPE);
            model.addAttribute("typeList", typeList);
            return "pages/support/voc";
        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("typeList", Collections.emptyList());
            return "pages/full_menu";
        }
    }

    /* 
     * 내 문의내역
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage/myvoc")
    public String showmyvoc(Model model, Principal principal) {
        log.info("=== MyVoc Page ===");

        try {
            List<VocListDto> vocList = this.vocService.findVocAll(principal.getName());
            model.addAttribute("vocList", vocList);
            
            List<CommCdBaseDto> typeList = this.commonService.findCommonCdNamesByGrpcd(CodeConstants.VOCTYPE);
            model.addAttribute("typeList", typeList);
            return "pages/support/myvoc";
        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("vocList", Collections.emptyList());
            model.addAttribute("typeList", Collections.emptyList());
            return "pages/full_menu";
        }
    }

    /* 
     * 내 문의내역(상세)
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage/myvoc/detail/{id}")
    public String showmyvocdetail(Model model, @PathVariable("id") Long id, Principal principal) {
        log.info("=== MyVoc Detail Page ===");

        try {
            VocDetailDto voc = this.vocService.findVocDetailOne(id);
            model.addAttribute("voc", voc);
            return "pages/support/myvoc_detail";
        } catch (Exception e) {
            e.getStackTrace();
            return "pages/support/myvoc";
        }
    }

    /* 
     * 충전이력
     */
    @GetMapping("/mypage/history/charge")
    public String showchghist(Model model, Principal principal) {
        log.info("=== Charging Hist Page ===");

        try {
            List<ChargingHistDto> histList = this.myPageService.findChgHistAll(principal.getName(),
                LocalDate.now(), LocalDate.now());
            model.addAttribute("histList", histList);
            return "pages/hist/charge_hist";
        } catch (Exception e) {
            e.getStackTrace();
            return "pages/full_menu";
        }
    }
}
