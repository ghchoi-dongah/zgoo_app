package zgoo.app.repository.code;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.app.domain.code.CommonCode;

public interface CommonCodeRepository extends JpaRepository<CommonCode, String>, CommonCodeRepositoryCustom {

}
