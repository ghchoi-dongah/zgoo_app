package zgoo.app.repository.member;

import java.util.Optional;

import zgoo.app.domain.member.Member;

public interface MemberRepositoryCustom {
    Optional<Member> findByMemLoginId(String memLoginId);
}
