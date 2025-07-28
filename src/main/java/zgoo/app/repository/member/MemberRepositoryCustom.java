package zgoo.app.repository.member;

import java.util.Optional;

import zgoo.app.domain.member.Member;
import zgoo.app.dto.member.MemberDto.MemberRegDto;

public interface MemberRepositoryCustom {
    Optional<Member> findByMemLoginId(String memLoginId);
    Member findMemberByMemLoginId(String memLoginId);
    Optional<String> findMemLoginIdByNameAndPhone(String name, String phone);
    Optional<Member> findMemberByNameAndIdAndPhone(String name, String memLoginId, String phone);
    Optional<String> getName(String memLoginId);
    MemberRegDto findMemberInfo(String memLoginId);
}
