package zgoo.app.repository.charge;

import java.util.List;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.app.domain.charge.QCsInfo;
import zgoo.app.dto.charge.CsInfoDto.CsInfoDetailDto;
import zgoo.app.util.CodeConstants;

@Slf4j
@RequiredArgsConstructor
public class CsRepositoryCustomImpl implements CsRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QCsInfo csInfo = QCsInfo.csInfo;

    @Override
    public List<CsInfoDetailDto> findStationsWithinRadius(double lat, double lng, double radius) {

        // NumberExpression<Double> distance = Expressions.numberTemplate(Double.class,
        //         "6371 * acos(cos(radians({0})) * cos(radians({1})) * cos(radians({2}) - radians({3})) + sin(radians({0})) * sin(radians({1})))",
        //         lat, csInfo.latitude, csInfo.longitude, lng);

        // km -> m
        NumberExpression<Integer> distance = Expressions.numberTemplate(Integer.class,
        "ROUND(6371 * 1000 * acos(cos(radians({0})) * cos(radians({1})) * cos(radians({2}) - radians({3})) + sin(radians({0})) * sin(radians({1}))))",
                lat, csInfo.latitude, csInfo.longitude, lng);

        double mRadius = radius * 1000;
        
        List<CsInfoDetailDto> csList = queryFactory.select(Projections.fields(CsInfoDetailDto.class,
                csInfo.id.as("stationId"),
                csInfo.address.as("address"),
                csInfo.addressDetail.as("addressDetail"),
                csInfo.latitude.as("latitude"),
                csInfo.longitude.as("longitude"),
                Expressions.stringTemplate("IF({0}  = 'Y', '유료', '무료')", csInfo.parkingFeeYn).as("parkingFee"),
                csInfo.openStartTime.as("openStartTime"),
                csInfo.openEndTime.as("openEndTime"),
                distance.as("distance")))
                .from(csInfo)
                .where(distance.loe(mRadius))
                .fetch();

        return csList;
    }

    @Override
    public List<CsInfoDetailDto> searchStations(String keyword, String option) {
        BooleanBuilder builder = new BooleanBuilder();

        if (option.equals(CodeConstants.STATION)) {
            builder.and(csInfo.stationName.containsIgnoreCase(keyword));
        } else if (option.equals(CodeConstants.ADDRESS)) {
            builder.and(csInfo.address.containsIgnoreCase(keyword));
        }

        List<CsInfoDetailDto> csList = queryFactory.select(Projections.fields(CsInfoDetailDto.class,
                csInfo.id.as("stationId"),
                csInfo.stationName.as("stationName"),
                csInfo.address.as("address")))
                .from(csInfo)
                .where(builder)
                .fetch();
        return csList;
    }

    @Override
    public CsInfoDetailDto findStationOne(String stationId) {
        CsInfoDetailDto dto = queryFactory.select(Projections.fields(CsInfoDetailDto.class,
                csInfo.id.as("stationId"),
                csInfo.stationName.as("stationName"),
                csInfo.address.as("address"),
                csInfo.addressDetail.as("addressDetail"),
                csInfo.latitude.as("latitude"),
                csInfo.longitude.as("longitude"),
                Expressions.stringTemplate("IF({0}  = 'Y', '유료', '무료')", csInfo.parkingFeeYn).as("parkingFee"),
                csInfo.openStartTime.as("openStartTime"),
                csInfo.openEndTime.as("openEndTime")))
                .from(csInfo)
                .where(csInfo.id.eq(stationId))
                .fetchOne();
        return dto;
    }
}
