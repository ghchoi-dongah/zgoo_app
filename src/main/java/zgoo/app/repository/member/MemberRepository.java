package zgoo.app.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.app.domain.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    boolean existsByMemLoginId(String memLoginId);
    boolean existsByIdTag(String idTag);
}
