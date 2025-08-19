package zgoo.app.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.app.domain.member.Member;
import zgoo.app.dto.hist.ChargingHistDto.ChgHistListDto;
import zgoo.app.dto.hist.ChargingHistDto.ChgHistSummaryDto;
import zgoo.app.dto.member.MemberDto.MemberCarDto;
import zgoo.app.dto.member.MemberDto.MemberConditionDto;
import zgoo.app.dto.member.MemberDto.MemberPasswordDto;
import zgoo.app.dto.member.MemberDto.MemberRegDto;
import zgoo.app.repository.condition.ConditionRepository;
import zgoo.app.repository.hist.ChargingHistRepository;
import zgoo.app.repository.member.MemberCarRepository;
import zgoo.app.repository.member.MemberRepository;
import zgoo.app.util.EncryptionUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyPageService {

    public final MemberRepository memberRepository;
    public final MemberCarRepository memberCarRepository;
    public final ConditionRepository conditionRepository;
    public final ChargingHistRepository chargingHistRepository;

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

    // 회원정보 수정
    @Transactional
    public Integer updateMemberInfo(MemberRegDto dto, String memLoginId) {
        try {
            Member member = this.memberRepository.findByMemLoginId(memLoginId)
                    .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

            // birth format
            String birth = dto.getBirth();
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate date = LocalDate.parse(birth, inputFormatter);
            String formattedBirth = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dto.setBirth(formattedBirth);

            member.updateMemberInfo(dto);
            log.info("[MyPageService >> updateMemberInfo] member info update complete");
            return 1;
        } catch (Exception e) {
            log.error("[MyPageService >> updateMemberInfo] error: {}", e.getMessage(), e);
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

    // 충전이력 조회
    public List<ChgHistListDto> findChgHistAll(String memLoginId, LocalDate startTime, LocalDate endTime) {
        try {
            Member member = this.memberRepository.findByMemLoginId(memLoginId)
                    .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
            String idTag = member.getIdTag();
            log.info("[MyPageService >> findChgHistAll] idTag: {}", idTag);

            LocalDateTime startOfMonth = startTime.withDayOfMonth(1).atStartOfDay();
            LocalDateTime endOfMonth = endTime.withDayOfMonth(endTime.lengthOfMonth()).atTime(23, 59, 59);
            log.info("[MyPageService >> findChgHistAll] startOfMonth: {}, endOfMonth: {}", startOfMonth, endOfMonth);

            List<ChgHistListDto> histList = this.chargingHistRepository.findAllByIdTag(idTag, startOfMonth, endOfMonth);
            log.info("[MyPageService >> findChgHistAll] histList: {}", histList.toString());
            return histList;
        } catch (Exception e) {
            log.error("[MyPageService >> findChgHistAll] error: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    
    // 약관 조회 by code
    public MemberConditionDto findConditionByConditionCode(String code) {
        try {
            MemberConditionDto conDto = this.conditionRepository.findConditionByConditionCode(code);
            log.info("[MyPageService >> findConditionByConditionCode] conDto: {}", conDto.toString());
            return conDto;
        } catch (Exception e) {
            log.error("[MyPageService >> findConditionByConditionCode] error: {}", e.getMessage(), e);
            return null;
        }
    }

    // 당월충전내역
    public ChgHistSummaryDto getCurrentMonthChgSummary(String memLoginId) {
        try {
            Member member = this.memberRepository.findByMemLoginId(memLoginId)
                    .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
            ChgHistSummaryDto chgHist = this.chargingHistRepository.getCurrentMonthChgSummary(member.getIdTag());
            log.info("[MyPageService >> getCurrentMonthChgSummary] chgHist: {}", chgHist.toString());
            return chgHist;
        } catch (Exception e) {
            log.error("[MyPageService >> getCurrentMonthChgSummary] error: {}", e.getMessage(), e);
            return null;
        }
    }
}
