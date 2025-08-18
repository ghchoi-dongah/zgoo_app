package zgoo.app.dto.hist;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ChargingHistDto {
    private String stationId;
    private String stationName;
    private String memberName;
    private String idTag;
    private Integer chgTime;
    private Integer soc;
    private Integer prepayCost;
    private Integer cancelCost;
    private Integer realCost;
    private BigDecimal chgAmount;
    private BigDecimal chgPrice;
    private BigDecimal unitCost;
    private LocalDateTime chgStartTime;
    private LocalDateTime chgEndTime;
}
