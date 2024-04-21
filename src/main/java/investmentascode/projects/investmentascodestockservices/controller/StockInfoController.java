package investmentascode.projects.investmentascodestockservices.controller;


import investmentascode.projects.investmentascodestockservices.dto.StockInfoDTO;
import investmentascode.projects.investmentascodestockservices.service.StockInfoService;
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

  private StockInfoService stockInfoService;

  public StockInfoController(StockInfoService stockInfoService) {
    this.stockInfoService = stockInfoService;
  }

  @GetMapping("/v1/stocks")
  public ResponseEntity<List<StockInfoDTO>> getStocks() {

    List<StockInfoDTO> stockInfoList = stockInfoService.getAllStocks();

    return new ResponseEntity<>(stockInfoList, HttpStatus.OK);
  }


  @GetMapping("/v1/stocks/{id}")
  public ResponseEntity<?> getStockById(@PathVariable("id") String id){

    StockInfoDTO stockInfo = stockInfoService.getStockById(id);

    if (!stockInfo.getType().equals("stock")) {
      System.out.println("asset id = " + id + " is not stock, please check.");
      return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body("Asset with ID " + id + " is not a stock asset. Please check.");
    }

    return new ResponseEntity<StockInfoDTO>(stockInfo, HttpStatus.OK);
  }
}
