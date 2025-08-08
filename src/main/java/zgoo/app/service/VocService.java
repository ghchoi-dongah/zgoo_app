package zgoo.app.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.app.domain.member.Member;
import zgoo.app.domain.support.Voc;
import zgoo.app.dto.support.VocDto.VocBaseDto;
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
}
