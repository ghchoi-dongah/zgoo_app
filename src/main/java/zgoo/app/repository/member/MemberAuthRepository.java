package zgoo.app.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.app.domain.member.MemberAuth;

public interface MemberAuthRepository extends JpaRepository<MemberAuth, String> {
    MemberAuth findByIdTag(String idTag);
}
