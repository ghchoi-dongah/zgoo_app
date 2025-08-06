package zgoo.app.repository.support;

import java.time.LocalDate;
import java.util.List;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.app.domain.code.QCommonCode;
import zgoo.app.domain.support.QNotice;
import zgoo.app.dto.support.NoticeDto.NoticeDetailDto;
import zgoo.app.dto.support.NoticeDto.NoticeListDto;

@Slf4j
@RequiredArgsConstructor
public class NoticeRepositoryCustomImpl implements NoticeRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QNotice notice = QNotice.notice;
    QCommonCode typeCommonCode = new QCommonCode("type");
    LocalDate today = LocalDate.now();

    @Override
    public List<NoticeListDto> findNotices() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(notice.delYn.eq("N"));
        builder.and(notice.startDate.loe(today));
        builder.and(notice.endDate.goe(today));

        return queryFactory.select(Projections.fields(NoticeListDto.class,
                notice.id.as("noticeId"),
                notice.title.as("title"),
                notice.content.as("content"),
                notice.views.as("views"),
                notice.regDt.as("regDt"),
                notice.startDate.as("startDate"),
                typeCommonCode.name.as("typeName")))
                .from(notice)
                .leftJoin(typeCommonCode).on(notice.type.eq(typeCommonCode.commonCode))
                .where(builder)
                .orderBy(notice.regDt.desc())
                .fetch();
    }
    
    @Override
    public List<NoticeListDto> getRecentNotices() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(notice.delYn.eq("N"));
        builder.and(notice.startDate.loe(today));
        builder.and(notice.endDate.goe(today));

        return queryFactory.select(Projections.fields(NoticeListDto.class,
                notice.id.as("noticeId"),
                notice.title.as("title"),
                notice.content.as("content"),
                notice.views.as("views"),
                notice.regDt.as("regDt"),
                notice.startDate.as("startDate"),
                typeCommonCode.name.as("typeName")))
                .from(notice)
                .leftJoin(typeCommonCode).on(notice.type.eq(typeCommonCode.commonCode))
                .where(builder)
                .orderBy(notice.regDt.desc())
                .limit(5)
                .fetch();
    }

    @Override
    public List<NoticeListDto> getNoticesByType(String typeCode) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(notice.delYn.eq("N"));
        builder.and(notice.startDate.loe(today));
        builder.and(notice.endDate.goe(today));
        builder.and(notice.type.eq(typeCode));

        return queryFactory.select(Projections.fields(NoticeListDto.class,
                notice.id.as("noticeId"),
                notice.title.as("title"),
                notice.content.as("content"),
                notice.views.as("views"),
                notice.regDt.as("regDt"),
                notice.startDate.as("startDate"),
                typeCommonCode.name.as("typeName")))
                .from(notice)
                .leftJoin(typeCommonCode).on(notice.type.eq(typeCommonCode.commonCode))
                .where(builder)
                .orderBy(notice.regDt.desc())
                .fetch();
    }

    @Override
    public NoticeDetailDto findNoticeDetailOne(Long id) {
        return queryFactory.select(Projections.fields(NoticeDetailDto.class,
                notice.id.as("noticeId"),
                notice.title.as("title"),
                notice.content.as("content"),
                notice.views.as("views"),
                notice.regDt.as("regDt"),
                typeCommonCode.name.as("typeName")))
                .from(notice)
                .leftJoin(typeCommonCode).on(notice.type.eq(typeCommonCode.commonCode))
                .where(notice.id.eq(id))
                .fetchOne();
    }
}
