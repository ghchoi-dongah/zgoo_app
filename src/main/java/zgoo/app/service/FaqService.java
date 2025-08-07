package zgoo.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.app.dto.support.FaqDto.FaqBaseDto;
import zgoo.app.repository.support.FaqRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class FaqService {

    private final FaqRepository faqRepository;

    // faq 조회
    public List<FaqBaseDto> findFaqAll() {
        try {
            List<FaqBaseDto> faqList = this.faqRepository.findFaqAll();

            for (FaqBaseDto faq : faqList) {
                String content = faq.getContent().replace("\n", "<br>");
                faq.setContent(content);
            }

            log.info("[FaqService >> findFaqAll] faqList: {}", faqList.toString());
            return faqList;
        } catch (Exception e) {
            log.error("[FaqService >> findFaqAll] error: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    // 유형별 FAQ
    public List<FaqBaseDto> getFaqBySection(String section) {
        try {
            List<FaqBaseDto> faqList = this.faqRepository.getFaqBySection(section);
            log.info("[FaqService >> getFaqBySection] section:{},  faqList: {}", section, faqList.toString());
            return faqList;
        } catch (Exception e) {
            log.error("[FaqService >> getFaqBySection] error: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
}
