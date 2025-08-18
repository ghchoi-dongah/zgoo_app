package zgoo.app.repository.condition;

import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.app.domain.condition.QConditionCode;
import zgoo.app.domain.condition.QConditionVersionHist;
import zgoo.app.dto.member.MemberDto.MemberConditionDto;

@Slf4j
@RequiredArgsConstructor
public class ConditionRepositoryCustomImpl implements ConditionRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QConditionCode conditionCode = QConditionCode.conditionCode1;
    QConditionVersionHist conditionVersion = QConditionVersionHist.conditionVersionHist;

    @Override
    public List<MemberConditionDto> findAllConditionList() {
        return queryFactory
                .select(Projections.fields(MemberConditionDto.class,
                    conditionCode.conditionCode.as("conditionCode"),
                    conditionCode.conditionName.as("conditionName"),
                    conditionCode.section.as("section"),
                    Expressions.stringTemplate("IF({0} = 'Y', '필수', '선택')", conditionCode.section).as("sectionName"),
                    conditionVersion.version.as("agreeVersion")))
                .from(conditionCode)
                .leftJoin(conditionVersion).on(conditionCode.eq(conditionVersion.condition),
                    conditionVersion.applyYn.eq("Y"))
                .orderBy(conditionCode.section.desc(), conditionCode.conditionCode.desc())
                .fetch();
    }

    @Override
    public MemberConditionDto findConditionByConditionCode(String code) {
        return queryFactory
                .select(Projections.fields(MemberConditionDto.class,
                    conditionCode.conditionCode.as("conditionCode"),
                    conditionCode.conditionName.as("conditionName"),
                    conditionCode.section.as("section"),
                    Expressions.stringTemplate("IF({0} = 'Y', '필수', '선택')", conditionCode.section).as("sectionName"),
                    conditionVersion.version.as("agreeVersion"),
                    conditionVersion.applyDt.as("applyDt")))
                .from(conditionCode)
                .leftJoin(conditionVersion).on(conditionCode.eq(conditionVersion.condition),
                    conditionVersion.applyYn.eq("Y"))
                .where(conditionCode.conditionCode.eq(code))
                .fetchOne();
    }
}
