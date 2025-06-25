package zgoo.app.repository.condition;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.app.domain.condition.ConditionCode;

public interface ConditionRepository extends JpaRepository<ConditionCode, String>, ConditionRepositoryCustom {
    ConditionCode findByConditionCode(String conditionCode);
}
