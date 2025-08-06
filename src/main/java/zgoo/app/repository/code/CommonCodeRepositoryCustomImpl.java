package zgoo.app.repository.code;

import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.app.domain.code.QCommonCode;
import zgoo.app.dto.code.CodeDto.CommCdBaseDto;

@Slf4j
@RequiredArgsConstructor
public class CommonCodeRepositoryCustomImpl implements CommonCodeRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QCommonCode commonCode = QCommonCode.commonCode1;

    @Override
    public List<CommCdBaseDto> findCommonCdNamesByGrpCode(String grpcode) {
        return queryFactory.select(Projections.fields(CommCdBaseDto.class,
                commonCode.group.grpCode.as("grpCode"),
                commonCode.commonCode.as("commonCode"),
                commonCode.name.as("commonCodeName")))
                .from(commonCode)
                .where(commonCode.group.grpCode.eq(grpcode))
                .fetch();
    }
}
