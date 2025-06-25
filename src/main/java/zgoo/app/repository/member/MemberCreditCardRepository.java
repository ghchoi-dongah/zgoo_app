package zgoo.app.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.app.domain.member.MemberCreditCard;

public interface MemberCreditCardRepository extends JpaRepository<MemberCreditCard, Long> {

}
