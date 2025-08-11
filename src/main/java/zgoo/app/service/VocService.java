package zgoo.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.app.domain.member.Member;
import zgoo.app.domain.support.Voc;
import zgoo.app.dto.support.VocDto.VocBaseDto;
import zgoo.app.dto.support.VocDto.VocDetailDto;
import zgoo.app.dto.support.VocDto.VocListDto;
import zgoo.app.mapper.VocMapper;
import zgoo.app.repository.member.MemberRepository;
import zgoo.app.repository.support.VocRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class VocService {

    public final VocRepository vocRepository;
    public final MemberRepository memberRepository;

    // voc save
    @Transactional
    public boolean saveVoc(VocBaseDto dto, String memLoginId) {
        try {
            Member member = this.memberRepository.findMemberByMemLoginId(memLoginId);

            if (ObjectUtils.isEmpty(member)) {
                log.warn("[VocService >> saveVoc] member is empty");
                return false;
            }

            Voc voc = VocMapper.toEntity(dto, member);
            this.vocRepository.save(voc);
            log.info("[saveVoc >> saveVoc] save complete");
            return true;
        } catch (Exception e) {
            log.error("[VocService >> saveVoc] error: {}", e.getMessage(), e);
            return false;
        }
    }

    // voc findAll
    public List<VocListDto> findVocAll(String memLoginId) {
        try {
            Member member = this.memberRepository.findMemberByMemLoginId(memLoginId);

            if (ObjectUtils.isEmpty(member)) {
                log.warn("[VocService >> findVocAll] member is empty");
                return new ArrayList<>();
            }

            List<VocListDto> vocList = this.vocRepository.findVocAll(member.getId());
            log.info("[VocService >> findVocAll] vocList: {}", vocList.toString());
            return vocList;
        } catch (Exception e) {
            log.error("[VocService >> findVocAll] error: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    // voc get
    public List<VocListDto> getVocByType(String type, String memLoginId) {
        try {
            Member member = this.memberRepository.findMemberByMemLoginId(memLoginId);

            if (ObjectUtils.isEmpty(member)) {
                log.warn("[VocService >> getVocByType] member is empty");
                return new ArrayList<>();
            }

            List<VocListDto> vocList = this.vocRepository.getVocByType(type, member.getId());
            log.info("[VocService >> getVocByType] vocList: {}", vocList.toString());
            return vocList;
        } catch (Exception e) {
            log.error("[VocService >> getVocByType] error: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    // voc detail
    public VocDetailDto findVocDetailOne(Long id) {
        try {
            VocDetailDto voc = this.vocRepository.findVocDetailOne(id);
            String content = voc.getContent() != null
                    ? voc.getContent().replace("\n", "<br>")
                    : "";

            String replyContent = voc.getReplyContent() != null
                    ? voc.getReplyContent().replace("\n", "<br>")
                    : "";
            voc.setContent(content);
            voc.setReplyContent(replyContent);
            log.info("[VocService >> findVocDetailOne] voc: {}", voc.toString());
            return voc;
        } catch (Exception e) {
            log.error("[VocService >> findVocDetailOne] error: {}", e.getMessage(), e);
            return null;
        }
    }

    // voc delete
    @Transactional
    public void deleteVoc(Long id) {
        try {
            Voc voc = this.vocRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("voc info not found with id: " + id));

            Long count = this.vocRepository.updateDelYn(id);
            log.info("[VocService << deleteVoc] delete voc id: {}", id);
        } catch (Exception e) {
            log.error("[VocService >> deleteVoc] error: {}", e.getMessage(), e);
        }
    }
}
