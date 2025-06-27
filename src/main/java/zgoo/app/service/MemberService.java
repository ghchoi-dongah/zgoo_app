package zgoo.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.app.domain.biz.BizInfo;
import zgoo.app.domain.condition.ConditionCode;
import zgoo.app.domain.member.Member;
import zgoo.app.domain.member.MemberAuth;
import zgoo.app.domain.member.MemberCar;
import zgoo.app.domain.member.MemberCondition;
import zgoo.app.domain.member.MemberCreditCard;
import zgoo.app.dto.member.BizDto.BizBaseDto;
import zgoo.app.dto.member.MemberDto.MemberCarDto;
import zgoo.app.dto.member.MemberDto.MemberConditionDto;
import zgoo.app.dto.member.MemberDto.MemberCreditCardDto;
import zgoo.app.dto.member.MemberDto.MemberRegDto;
import zgoo.app.mapper.MemberMapper;
import zgoo.app.repository.biz.BizRepository;
import zgoo.app.repository.condition.ConditionRepository;
import zgoo.app.repository.member.MemberAuthRepository;
import zgoo.app.repository.member.MemberCarRepository;
import zgoo.app.repository.member.MemberConditionRepository;
import zgoo.app.repository.member.MemberCreditCardRepository;
import zgoo.app.repository.member.MemberRepository;
import zgoo.app.util.ComcodeConstants;
import zgoo.app.util.EncryptionUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    public final BizRepository bizRepository;
    public final MemberRepository memberRepository;
    public final MemberCarRepository memberCarRepository;
    public final MemberAuthRepository memberAuthRepository;
    public final MemberConditionRepository memberConditionRepository;
    public final MemberCreditCardRepository memberCreditCardRepository;
    public final ConditionRepository conditionRepository;


    // 약관 조회
    public List<MemberConditionDto> findAllConditionList() {
        try {
            List<MemberConditionDto> conList = this.conditionRepository.findAllConditionList();
            log.info("[MemberService >> findAllConditionList] conList: {}", conList.toString());
            return conList;
        } catch (Exception e) {
            log.error("[MemberService >> findAllConditionList] error: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    // 회원 저장
    @Transactional
    public boolean saveMember(MemberRegDto dto) {
        try {
            if (dto == null) {
                throw new IllegalArgumentException("회원 정보가 없습니다.");
            }

            log.info("[MemberService >> saveMember] member info: {}", dto.toString());

            Member member;
            dto.setIdTag(generateIdTag());  // 회원번호 자동부여
            dto.setPassword(EncryptionUtils.encryptSHA256(dto.getPassword()));  // 암호화

            // 회원구분(개인/법인)
            switch (dto.getBizType()) {
                case "PB" -> {
                    // 약관 & 결제카드 정보 확인
                    if (ObjectUtils.isEmpty(dto.getCondition()) ||  ObjectUtils.isEmpty(dto.getCard())) {
                        throw new IllegalArgumentException("개인 회원의 약관 또는 카드 정보가 없습니다.");
                    }

                    member = MemberMapper.toEntity(dto);
                }
                case "CB" -> {
                    if (ObjectUtils.isEmpty(dto.getCondition())) {
                        throw new IllegalArgumentException("법인 회원의 약관 정보가 없습니다.");
                    }

                    BizInfo biz = this.bizRepository.findById(dto.getBizId())
                            .orElseThrow(() -> new IllegalArgumentException("bizInfo not found with id: " + dto.getBizId()));
                    member = MemberMapper.toEntityBiz(dto, biz);

                    // 법인회원 결제카드 상태
                    if (ObjectUtils.isEmpty(biz.getCardNum()) || ObjectUtils.isEmpty(biz.getBid())) {
                        throw new IllegalStateException("법인 회원 결제카드 정보가 없습니다.");
                    }

                    member.updateCreditStatInfo("MCNORMAL");
                }
                default -> {
                    throw new IllegalArgumentException("[MemberService >> saveMember] PB/CB 이외의 값이 입력됐습니다. 입력값: " + dto.getBizType());
                }
            }

            Member saved = this.memberRepository.save(member); // save member

            // 개인회원일 경우에만 결제카드 정보 저장
            if ("PB".equals(dto.getBizType())) {
                log.info("[MemberService >> saveMember] save credit card info:{}", dto.getCard().toString());
                saveCreditCard(saved, dto.getCard());
            }

            saveCar(saved, dto.getCar()); // save car
            saveCondition(saved, dto.getCondition()); // save condition

            // 회원카드관리(기본값 설정)
            MemberAuth auth = MemberMapper.toEntityAuth(saved);
            this.memberAuthRepository.save(auth);
            log.info("[MemberService >> saveMember] save complete");
            return true;
        } catch (Exception e) {
            log.error("[MemberService >> saveMember] error: {}", e.getMessage(), e);
            throw e;
        }
    }

    // 개인회원 결제카드 정보 저장
    public void saveCreditCard(Member member, MemberCreditCardDto dto) {
        try {
            /* 
             * 1. 개인회원은 결제카드 정보가 1건 이상 필수 등록
             * 2. 회원 등록 시 대표카드 1건 등록
             */
            if (ObjectUtils.isEmpty(dto) || dto.getCardNum().isEmpty() || dto.getTid().isEmpty()) {
                throw new IllegalArgumentException("카드 정보가 없습니다.");
            }

            /* 
             * TODO: 카드정보 로직(ex. 암호화)
             */

            log.info("[MemberService >> saveCreditCard] credit card info: {}", dto.toString());
            member.updateCreditStatInfo(ComcodeConstants.MCNORMAL);
            dto.setRepresentativeCard("Y");
            MemberCreditCard card = MemberMapper.toEntityCard(dto, member);
            this.memberCreditCardRepository.save(card);
            log.info("[MemberService >> saveCreditCard] save complete");
        } catch (IllegalArgumentException e) {
            log.warn("[MemberService >> saveCreditCard] invalid argument: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("[MemberService >> saveCreditCard] error: {}", e.getMessage(), e);
            throw new RuntimeException("결제카드 정보 저장 중 오류가 발생했습니다.");
        }
    }

    // 차량정보 저장
    public void saveCar(Member member, List<MemberCarDto> dtos) {
        try {
            if (!ObjectUtils.isEmpty(dtos)) {
                log.info("[MemberService >> saveCar] car info: {}", dtos.toString());
                for (MemberCarDto dto : dtos) {
                    // 공백 제거
                    String cleanCarNum = dto.getCarNum() != null ? dto.getCarNum().replaceAll("\\s+", "") : null;
                    String cleanModel = dto.getModel() != null ? dto.getModel().replaceAll("\\s+", "") : null;
                    dto.setCarNum(cleanCarNum);
                    dto.setModel(cleanModel);

                    MemberCar car = MemberMapper.toEntityCar(dto, member);
                    this.memberCarRepository.save(car);
                }
                log.info("[MemberService >> saveCar] save complete");
            } else {
                log.info("[MemberService >> saveCar] car info empty");
            }
        } catch (Exception e) {
            log.error("[MemberService >> saveCar] error: {}", e.getMessage(), e);
        }
    }

    // 약관정보 저장
    public void saveCondition(Member member, List<MemberConditionDto> dtos) {
        try {
            if (ObjectUtils.isEmpty(dtos)) {
                throw new IllegalArgumentException("약관 정보가 없습니다.");
            }

            log.info("[MemberService >> saveCondition] condition info: {}", dtos.toString());
            for (MemberConditionDto dto : dtos) {
                String conCode = dto.getConditionCode();
                ConditionCode condition = this.conditionRepository.findByConditionCode(conCode);
                MemberCondition memCondition = MemberMapper.toEntityCondition(dto, member, condition);
                this.memberConditionRepository.save(memCondition);
            }

            log.info("[MemberService >> saveCondition] save complete");
        } catch (IllegalArgumentException e) {
            log.warn("[MemberService >> saveCondition] invalid argument: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("[MemberService >> saveCondition] error: {}", e.getMessage(), e);
            throw new RuntimeException("약관 정보 저장 중 오류가 발생했습니다.");
        }
    }

    // 회원 삭제
    @Transactional
    public void deleteMember(Long memberId) {
        try {

            Member member = this.memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
            
            // member_auth delete
            MemberAuth memberAuth = this.memberAuthRepository.findByIdTag(member.getIdTag());
            if (memberAuth != null) {
                this.memberAuthRepository.delete(memberAuth);
            }

            // TODO: 회원정보 삭제(회원탈퇴)
            // member_creditcard delete
            
            // member_car delete

            // member_condition delete

            // member_auth_hist delete

            // member delete
            this.memberRepository.delete(member);
            log.info("[MemberService >> deleteMember] delete complete memberId: {}", memberId);
        } catch (Exception e) {
            log.error("[MemberService >> deleteMember] error: {}", e.getMessage(), e);
            throw e;
        }
    }

    // 회원ID 중복체크
    public boolean isMemLoginIdDuplicate(String memLoginId) {
        return this.memberRepository.existsByMemLoginId(memLoginId);
    }

    // 무작위 16자리 숫자 생성
    public String generateRandomNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 16; i++) {
            int digit = random.nextInt(10);
            sb.append(digit);
        }

        return sb.toString();
    }

    // 중복되지 않게 숫자 생성
    public String generateIdTag() {
        String randomNumber;

        do {
            randomNumber = generateRandomNumber();
        } while (memberRepository.existsByIdTag(randomNumber));

        return randomNumber;
    }

    // 법인 조회
    public List<BizBaseDto> findBizInfo(String bizName) {
        try {
            List<BizBaseDto> bizList = this.bizRepository.findBizInfoByBizName(bizName);
            log.info("[MemberService >> findBizInfo] bizList: {}", bizList.toString());
            return bizList;
        } catch (Exception e) {
            log.error("[MemberService >> findBizInfo] error: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    // 아이디 찾기
    public Optional<String> findMemLoginId(String name, String phone) {
        try {
            Optional<String> memLoginId = this.memberRepository.findMemLoginIdByNameAndPhone(name, phone);
            log.info("[MemberService >> findMemLoginId] memLoinId: {}", memLoginId);
            return memLoginId;
        } catch (Exception e) {
            log.error("[MemberService >> findMemLoginId] error: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    // 회원정보가 있는지 확인(비밀번호 찾기)
    public Boolean findMemberInfoInPw(String name, String memLoginId, String phone) {
        try {
            Optional<Member> member = this.memberRepository.findMemberByNameAndIdAndPhone(name, memLoginId, phone);

            if (ObjectUtils.isEmpty(member)) {
                log.warn("[MemberService >> findMemberInfoInPw] member is empty");
                return false;
            }

            log.info("[MemberService >> findMemberInfoInPw] member: {}", member.toString());
            return true;
        } catch (Exception e) {
            log.error("[MemberService >> findMemberInfoInPw] error: {}", e.getMessage(), e);
            return false;
        }
    }

    // 비밀번호 변경
    @Transactional
    public Boolean updatePassword(String memLoginId, String newPw) {
        try {
            Member member = this.memberRepository.findMemberByMemLoginId(memLoginId);

            if (ObjectUtils.isEmpty(member)) {
                log.warn("[MemberService >> updatePassword] member is empty");
                return false;
            }

            member.updatePasswordInfo(EncryptionUtils.encryptSHA256(newPw));
            log.info("[MemberService >> updatePassword] update password complete");
            return true;
        } catch (Exception e) {
            log.error("[MemberService >> updatePassword] error: {}", e.getMessage(), e);
            return false;
        }
    }
}
