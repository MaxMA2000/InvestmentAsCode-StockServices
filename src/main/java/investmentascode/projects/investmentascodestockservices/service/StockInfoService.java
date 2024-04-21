package investmentascode.projects.investmentascodestockservices.service;


import investmentascode.projects.investmentascodestockservices.dto.StockInfoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class StockInfoService {

  private String BASE_URL = "http://localhost:3000";

  public List<StockInfoDTO> getAllStocks() {
    String url = BASE_URL + "/v1/asset/byType?type=stock";
    RestTemplate restTemplate = new RestTemplate();

    ResponseEntity<StockInfoDTO[]> response = restTemplate.getForEntity(url, StockInfoDTO[].class);
    StockInfoDTO[] stockInfoArray = response.getBody();

    return Arrays.asList(stockInfoArray);
  }


  public StockInfoDTO getStockById(String id) {
    String url = BASE_URL + "/v1/asset/byId?id=" + id;
    RestTemplate restTemplate = new RestTemplate();

    ResponseEntity<StockInfoDTO> response = restTemplate.getForEntity(url, StockInfoDTO.class);
    StockInfoDTO stockInfo = response.getBody();

    return stockInfo;

  }


}
