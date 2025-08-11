package zgoo.app.repository.support;

import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.app.domain.code.QCommonCode;
import zgoo.app.domain.support.QVoc;
import zgoo.app.dto.support.VocDto.VocDetailDto;
import zgoo.app.dto.support.VocDto.VocListDto;

@Slf4j
@RequiredArgsConstructor
public class VocRepositoryCustomImpl implements VocRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QVoc voc = QVoc.voc;
    QCommonCode typeName = new QCommonCode("type");
    QCommonCode replyStatName = new QCommonCode("replyStatName");

    @Override
    public List<VocListDto> findVocAll(Long memberId) {
        return queryFactory.select(Projections.fields(VocListDto.class,
                voc.id.as("vocId"),
                voc.title.as("title"),
                voc.regDt.as("regDt"),
                voc.replyStat.as("replyStat"),
                typeName.name.as("typeName"),
                replyStatName.name.as("replyStatName")))
                .from(voc)
                .leftJoin(typeName).on(voc.type.eq(typeName.commonCode))
                .leftJoin(replyStatName).on(voc.replyStat.eq(replyStatName.commonCode))
                .where(voc.delYn.eq("N").and(voc.member.id.eq(memberId)))
                .orderBy(voc.regDt.desc())
                .fetch();
    }

    @Override
    public List<VocListDto> getVocByType(String type, Long memberId) {
        return queryFactory.select(Projections.fields(VocListDto.class,
                voc.id.as("vocId"),
                voc.title.as("title"),
                voc.content.as("content"),
                voc.regDt.as("regDt"),
                voc.replyStat.as("replyStat"),
                typeName.name.as("typeName"),
                replyStatName.name.as("replyStatName")))
                .from(voc)
                .leftJoin(typeName).on(voc.type.eq(typeName.commonCode))
                .leftJoin(replyStatName).on(voc.replyStat.eq(replyStatName.commonCode))
                .where(voc.delYn.eq("N").and(voc.type.eq(type)).and(voc.member.id.eq(memberId)))
                .orderBy(voc.regDt.desc())
                .fetch();
    }

    @Override
    public VocDetailDto findVocDetailOne(Long id) {
        return queryFactory.select(Projections.fields(VocDetailDto.class,
                voc.id.as("vocId"),
                voc.title.as("title"),
                voc.content.as("content"),
                voc.regDt.as("regDt"),
                voc.replyDt.as("replyDt"),
                typeName.name.as("typeName"),
                replyStatName.name.as("replyStatName")))
                .from(voc)
                .leftJoin(typeName).on(voc.type.eq(typeName.commonCode))
                .leftJoin(replyStatName).on(voc.replyStat.eq(replyStatName.commonCode))
                .where(voc.id.eq(id))
                .fetchOne();
    }

    @Override
    public Long updateDelYn(Long id) {
        return queryFactory
                .update(voc)
                .set(voc.delYn, "Y")
                .where(voc.id.eq(id))
                .execute();
    }
}
