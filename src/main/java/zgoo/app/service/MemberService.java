package zgoo.app.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.app.dto.member.MemberDto.MemberRegDto;
import zgoo.app.repository.member.MemberRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    public final MemberRepository memberRepository;

    // member save
    @Transactional
    public void saveMember(MemberRegDto dto) {
        try {

        } catch (Exception e) {
            log.error("[saveMember] error: {}", e.getMessage(), e);
        }
    }
}
