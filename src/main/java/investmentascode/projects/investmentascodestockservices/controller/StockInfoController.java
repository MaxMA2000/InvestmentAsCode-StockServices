package investmentascode.projects.investmentascodestockservices.controller;


import investmentascode.projects.investmentascodestockservices.dto.StockInfoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Controller
public class StockInfoController {

  @GetMapping("/v1/stocks")
  public ResponseEntity<List<StockInfoDTO>> getStocks() {
    String url = "http://localhost:3000/v1/asset/byType?type=stock";
    RestTemplate restTemplate = new RestTemplate();

    ResponseEntity<StockInfoDTO[]> response = restTemplate.getForEntity(url, StockInfoDTO[].class);
    StockInfoDTO[] stockInfoArray = response.getBody();
    List<StockInfoDTO> stockInfoList = Arrays.asList(stockInfoArray);

    return new ResponseEntity<>(stockInfoList, HttpStatus.OK);
  }


  @GetMapping("/v1/stocks/{id}")
  public ResponseEntity<?> getStockById(@PathVariable("id") String id){
    String url = "http://localhost:3000/v1/asset/byId?id=" + id;
    RestTemplate restTemplate = new RestTemplate();

    ResponseEntity<StockInfoDTO> response = restTemplate.getForEntity(url, StockInfoDTO.class);
    StockInfoDTO stockInfo = response.getBody();

    if (!stockInfo.getType().equals("stock")) {
      System.out.println("asset id = " + id + " is not stock, please check.");
      return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body("Asset with ID " + id + " is not a stock asset. Please check.");
    }

    return new ResponseEntity<StockInfoDTO>(stockInfo, HttpStatus.OK);
  }
}
