package zgoo.app.domain.charge;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "CS_INFO")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class CsInfo {

    @Id
    @Column(name = "station_id")
    private String id;

    @Column(name = "station_name")
    private String stationName;

    @Column(name = "station_type")
    private String stationType;

    @Column(name = "facility_type")
    private String facilityType;

    @Column(name = "as_num")
    private String asNum;

    @Column(name = "op_status")
    private String opStatus;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "zipcode")
    private String zipcode;

    @Column(name = "address")
    private String address;

    @Column(name = "address_detail")
    private String addressDetail;

    @Column(name = "open_start_time")
    private LocalTime openStartTime;

    @Column(name = "open_end_time")
    private LocalTime openEndTime;

    @Column(name = "parking_fee_yn")
    private String parkingFeeYn;

    @Column(name = "sido")
    private String sido;

    @Column(name = "safety_management_fee")
    private Integer safetyManagementFee;
}
