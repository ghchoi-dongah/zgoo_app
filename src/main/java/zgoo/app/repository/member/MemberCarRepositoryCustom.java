package zgoo.app.repository.member;

import java.util.List;

import zgoo.app.dto.member.MemberDto.MemberCarDto;

public interface MemberCarRepositoryCustom {
    List<MemberCarDto> findAllByMemberId(Long memberId);
}
