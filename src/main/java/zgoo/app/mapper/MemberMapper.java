package zgoo.app.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import zgoo.app.domain.biz.BizInfo;
import zgoo.app.domain.condition.ConditionCode;
import zgoo.app.domain.member.Member;
import zgoo.app.domain.member.MemberAuth;
import zgoo.app.domain.member.MemberCar;
import zgoo.app.domain.member.MemberCondition;
import zgoo.app.domain.member.MemberCreditCard;
import zgoo.app.dto.member.MemberDto.MemberCarDto;
import zgoo.app.dto.member.MemberDto.MemberConditionDto;
import zgoo.app.dto.member.MemberDto.MemberCreditCardDto;
import zgoo.app.dto.member.MemberDto.MemberRegDto;
import zgoo.app.util.CodeConstants;

public class MemberMapper {

    /* 
     * member(dto >> entity)
     */
    public static Member toEntity(MemberRegDto dto) {
        Member member = Member.builder()
                .memLoginId(dto.getMemLoginId())
                .password(dto.getPassword())
                .name(dto.getName())
                .bizType(dto.getBizType())
                .phoneNo(dto.getPhoneNo())
                .idTag(dto.getIdTag())
                .email(dto.getEmail())
                .birth(dto.getBirth())
                .zipCode(dto.getZipCode())
                .address(dto.getAddress())
                .addressDetail(dto.getAddressDetail())
                .userState(CodeConstants.MSTNORMAL)
                .joinedDt(LocalDateTime.now())
                .creditcardStat(dto.getCreditcardStat())
                .build();
        return member;
    }

    /* 
     * biz member(dto >> entity)
     */
    public static Member toEntityBiz(MemberRegDto dto, BizInfo biz) {
        Member member = Member.builder()
                .biz(biz)
                .memLoginId(dto.getMemLoginId())
                .password(dto.getPassword())
                .name(dto.getName())
                .bizType(dto.getBizType())
                .phoneNo(dto.getPhoneNo())
                .idTag(dto.getIdTag())
                .email(dto.getEmail())
                .birth(dto.getBirth())
                .zipCode(dto.getZipCode())
                .address(dto.getAddress())
                .addressDetail(dto.getAddressDetail())
                .userState(CodeConstants.MSTNORMAL)
                .joinedDt(LocalDateTime.now())
                .creditcardStat(dto.getCreditcardStat())
                .build();
        return member;
    }

    /* 
     * member car(dto >> entity)
     */
    public static MemberCar toEntityCar(MemberCarDto dto, Member member) {
        MemberCar car = MemberCar.builder()
                .member(member)
                .carType(dto.getCarType())
                .carNum(dto.getCarNum())
                .model(dto.getModel())
                .regDt(LocalDateTime.now())
                .build();
        return car;
    }

    /* 
     * member condition(dto >> entity)
     */
    public static MemberCondition toEntityCondition(MemberConditionDto dto, Member member,
            ConditionCode conditionCode) {
        MemberCondition condition = MemberCondition.builder()
                .member(member)
                .condition(conditionCode)
                .agreeVersion(dto.getAgreeVersion())
                .agreeYn(dto.getAgreeYn())
                .agreeDt(LocalDateTime.now())
                .build();
        return condition;
    }

    /* 
     * member credit card(dto >> entity)
     */
    public static MemberCreditCard toEntityCard(MemberCreditCardDto dto, Member member) {
        MemberCreditCard card = MemberCreditCard.builder()
                .member(member)
                .tid(dto.getTid())
                .cardNum(dto.getCardNum())
                .fnCode(dto.getFnCode())
                .representativeCard(dto.getRepresentativeCard())
                .regDt(LocalDateTime.now())
                .build();
        return card;
    }

    /* 
     * auth(dto >> entity)
     */
    public static MemberAuth toEntityAuth(Member member) {
                MemberAuth auth = MemberAuth.builder()
                .member(member)
                .idTag(member.getIdTag())
                .expireDate(LocalDate.parse("2099-12-31"))
                .useYn("Y")
                .parentIdTag("1001")
                .totalChargingPower(BigDecimal.ZERO)
                .status("Accepted")
                .totalChargingPrice(0L)
                .regDt(LocalDateTime.now())
                .build();
        return auth;
    }
}
