package investmentascode.projects.investmentascodestockservices.controller;

import investmentascode.projects.investmentascodestockservices.dto.StockInfoDTO;
import investmentascode.projects.investmentascodestockservices.dto.StockPriceDTO;
import investmentascode.projects.investmentascodestockservices.service.StockPriceService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Controller
public class StockPriceController {

  private StockPriceService stockPriceService;

  public StockPriceController(StockPriceService stockPriceService) {
    this.stockPriceService = stockPriceService;
  }

  @GetMapping("/v1/stock/{id}/from/{from}/to/{to}")
  public ResponseEntity<?> getPeriodStockPrice(
    @PathVariable String id,
    @PathVariable String from,
    @PathVariable String to) {

    if (stockPriceService.isFromDateBeforeToDate(from, to).equals(Boolean.FALSE)) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("from Date can't be After or Same as to Date , Please Check.");
    }

    List<StockPriceDTO> stockPriceList = stockPriceService.getPeriodStockPrice(id, from, to);

    return new ResponseEntity<List<StockPriceDTO>>(stockPriceList, HttpStatus.OK);

  }

}
