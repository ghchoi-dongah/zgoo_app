package zgoo.app.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.app.domain.member.Member;
import zgoo.app.dto.member.MemberDto.MemberCarDto;
import zgoo.app.dto.member.MemberDto.MemberPasswordDto;
import zgoo.app.dto.member.MemberDto.MemberRegDto;
import zgoo.app.repository.member.MemberCarRepository;
import zgoo.app.repository.member.MemberRepository;
import zgoo.app.util.EncryptionUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyPageService {

    public final MemberRepository memberRepository;
    public final MemberCarRepository memberCarRepository;

    // 회원정보 조회
    public MemberRegDto findMemberInfo(String memLoginId) {
        try {
            MemberRegDto member = this.memberRepository.findMemberInfo(memLoginId);

            // birth format
            String birth = member.getBirth();
            LocalDate date = LocalDate.parse(birth);
            String formattedBirth = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            member.setBirth(formattedBirth);

            // car info
            List<MemberCarDto> carList = this.memberCarRepository.findAllByMemberId(member.getMemberId());
            log.info("[MyPageService >> findMemberInfo] carList: {}", carList.toString());
            member.setCar(carList);

            log.info("[MyPageService >> findMemberInfo] member: {}", member.toString());
            return member;
        } catch (Exception e) {
            log.error("[MyPageService >> findMemberInfo] error: {}", e.getMessage(), e);
            return null;
        }
    }

    // 비밀번호 변경
    @Transactional
    public Integer updatePasswordInfo(MemberPasswordDto dto, String memLoginId) {
        try {
            Member member = this.memberRepository.findByMemLoginId(memLoginId)
                    .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
            
            dto.setExistPassword(EncryptionUtils.encryptSHA256(dto.getExistPassword()));

            // 1. 현재 비밀번호 일치여부
            if (!member.getPassword().equals(dto.getExistPassword())) {
                log.info("[MyPageService >> updatePasswordInfo] doesn't match the current password");
                return 0;
            }

            // 2. 새 비밀번호 =/= 새 비밀번호
            if (!dto.getNewPassword().equals(dto.getNewPasswordCheck())) {
                log.info("[MyPageService >> updatePasswordInfo] two password values don't match");
                return 2;
            }

            // update new password
            String newPassword = EncryptionUtils.encryptSHA256(dto.getNewPassword());
            member.updatePasswordInfo(newPassword);
            log.info("[MyPageService >> updatePasswordInfo] password change complete");
            return 1;
        } catch (Exception e) {
            log.error("[MyPageService >> updatePasswordInfo] error: {}", e.getMessage(), e);
            return null;
        }
    }
}
