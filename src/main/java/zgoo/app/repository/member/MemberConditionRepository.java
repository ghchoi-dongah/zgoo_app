package zgoo.app.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.app.domain.member.MemberCondition;

public interface MemberConditionRepository extends JpaRepository<MemberCondition, Long> {

}
