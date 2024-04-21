package investmentascode.projects.investmentascodestockservices.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockInfoDTO {
  private Long assetId;
  private String symbol;
  private String name;
  private String exchange;
  private String exchangeShortName;
  private String type;

//  use string for now as receiving JSON response with asOfDate in string format
  private String asOfDate;
}
