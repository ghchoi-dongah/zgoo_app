package zgoo.app.repository.biz;

import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.app.domain.biz.QBizInfo;
import zgoo.app.dto.member.BizDto.BizBaseDto;

@Slf4j
@RequiredArgsConstructor
public class BizRepositoryCustomImpl implements BizRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QBizInfo bizInfo = QBizInfo.bizInfo;

    @Override
    public List<BizBaseDto> findBizInfoByBizName(String bizName) {
        return queryFactory.select(Projections.fields(BizBaseDto.class,
                bizInfo.id.as("bizId"),
                bizInfo.bizName.as("bizName"),
                Expressions.stringTemplate("CONCAT(IF({0} IS NULL OR {0} = '', 'N', 'Y'), '')", bizInfo.bid)
                    .as("confirmCard")))
                .from(bizInfo)
                .where(bizInfo.bizName.contains(bizName),
                    bizInfo.bid.isNotNull().and(bizInfo.bid.ne("")))
                .orderBy(bizInfo.bizName.asc())
                .fetch();
    }
}
