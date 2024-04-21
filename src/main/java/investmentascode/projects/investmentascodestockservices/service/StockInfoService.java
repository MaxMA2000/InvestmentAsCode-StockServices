package investmentascode.projects.investmentascodestockservices.service;


import investmentascode.projects.investmentascodestockservices.dto.StockInfoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class StockInfoService {


  @Value("${assetService.url}")
  private String assetServiceUrl;


  public List<StockInfoDTO> getAllStocks() {
    String url = assetServiceUrl + "/v1/asset/byType?type=stock";
    RestTemplate restTemplate = new RestTemplate();

    ResponseEntity<StockInfoDTO[]> response = restTemplate.getForEntity(url, StockInfoDTO[].class);
    StockInfoDTO[] stockInfoArray = response.getBody();

    return Arrays.asList(stockInfoArray);
  }


  public StockInfoDTO getStockById(String id) {
    String url = assetServiceUrl + "/v1/asset/byId?id=" + id;
    RestTemplate restTemplate = new RestTemplate();

    ResponseEntity<StockInfoDTO> response = restTemplate.getForEntity(url, StockInfoDTO.class);
    StockInfoDTO stockInfo = response.getBody();

    return stockInfo;

  }


}
