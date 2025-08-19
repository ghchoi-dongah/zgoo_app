package zgoo.app.repository.hist;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.app.domain.charge.QCpInfo;
import zgoo.app.domain.charge.QCsInfo;
import zgoo.app.domain.hist.QChargingHist;
import zgoo.app.domain.member.QMember;
import zgoo.app.dto.hist.ChargingHistDto.ChgHistListDto;
import zgoo.app.dto.hist.ChargingHistDto.ChgHistSummaryDto;
import zgoo.app.type.UseYn;

@Slf4j
@RequiredArgsConstructor
public class ChargingHistRepositoryCustomImpl implements ChargingHistRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QChargingHist hist = QChargingHist.chargingHist;
    QMember member = QMember.member;
    QCsInfo csInfo = QCsInfo.csInfo;
    QCpInfo cpInfo = QCpInfo.cpInfo;

    @Override
    public List<ChgHistListDto> findAllByIdTag(String idTag, LocalDateTime startDate, LocalDateTime endDate) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(hist.idTag.eq(idTag));
        builder.and(hist.startTime.goe(startDate));
        builder.and(hist.startTime.loe(endDate));

        return queryFactory.select(Projections.fields(ChgHistListDto.class,
                hist.idTag.as("idTag"),
                hist.startTime.as("chgStartTime"),
                hist.endTime.as("chgEndTime"),
                hist.chargingTime.as("chgTime"),
                hist.soc.as("soc"),
                hist.chargeAmount.as("chgAmount"),
                hist.chargePrice.as("chgPrice"),
                hist.unitCost.as("unitCost"),
                hist.preAmount.as("prepayCost"),
                hist.cancelAmount.as("cancelCost"),
                hist.realAmount.as("realCost"),
                csInfo.id.as("stationId"),
                csInfo.stationName.as("stationName"),
                member.name.as("memberName")))
                .from(hist)
                .leftJoin(cpInfo).on(hist.chargerID.eq(cpInfo.id))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(member).on(hist.idTag.eq(member.idTag))
                .where(builder)
                .orderBy(hist.startTime.desc())
                .fetch();
    }

    @Override
    public ChgHistSummaryDto getCurrentMonthChgSummary(String idTag) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(hist.idTag.eq(idTag));
        builder.and(hist.paymentYn.eq(UseYn.Y));

        // 월 1일 ~ 마지막일
        LocalDate today = LocalDate.now();
        LocalDateTime startOfMonth = today.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = today.withDayOfMonth(today.lengthOfMonth()).atTime(23, 59, 59);
        builder.and(hist.startTime.goe(startOfMonth));
        builder.and(hist.startTime.loe(endOfMonth));

        return queryFactory.select(Projections.fields(ChgHistSummaryDto.class,
                hist.count().as("chgCnt"),
                hist.realAmount.sum().coalesce(Expressions.constant(0)).intValue().as("realCost"),
                hist.chargeAmount.sum().coalesce(Expressions.constant(BigDecimal.ZERO)).as("chgAmount")))
                .from(hist)
                .where(builder)
                .fetchOne();
    }
}
