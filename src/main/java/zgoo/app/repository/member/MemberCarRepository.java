package zgoo.app.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.app.domain.member.MemberCar;

public interface MemberCarRepository extends JpaRepository<MemberCar, Long>, MemberCarRepositoryCustom {

}
