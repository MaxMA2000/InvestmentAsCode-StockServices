package investmentascode.projects.investmentascodestockservices.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockPriceDTO {

  private Long id;
  private Long assetId;
  private String symbol;
  private Date date;
  private BigDecimal open;
  private BigDecimal high;
  private BigDecimal low;
  private BigDecimal close;
  private BigDecimal adjClose;
  private Integer volume;
  private Integer unadjustedVolume;
  private BigDecimal change;
  private BigDecimal changePercent;
  private Float vwap;
  private String label;
  private Float changeOverTime;
  private String asOfDate;

}
