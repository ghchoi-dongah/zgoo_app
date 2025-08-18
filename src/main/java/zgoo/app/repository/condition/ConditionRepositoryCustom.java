package zgoo.app.repository.condition;

import java.util.List;

import zgoo.app.dto.member.MemberDto.MemberConditionDto;

public interface ConditionRepositoryCustom {
    List<MemberConditionDto> findAllConditionList();
    MemberConditionDto findConditionByConditionCode(String code);
}
