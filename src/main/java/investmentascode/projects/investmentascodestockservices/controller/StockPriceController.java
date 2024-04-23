package investmentascode.projects.investmentascodestockservices.controller;

import investmentascode.projects.investmentascodestockservices.dto.StockPriceDTO;
import investmentascode.projects.investmentascodestockservices.exception.StockPriceNotFoundException;
import investmentascode.projects.investmentascodestockservices.service.StockPriceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Controller
public class StockPriceController {

  private StockPriceService stockPriceService;

  public StockPriceController(StockPriceService stockPriceService) {
    this.stockPriceService = stockPriceService;
  }

  @GetMapping("/v1/stock/{id}/{date}")
  public ResponseEntity<?> getSingleDayStockPrice(
    @PathVariable String id,
    @PathVariable String date
  ) {
    try {

      StockPriceDTO singleDayStockPrice = stockPriceService.getSingleDayStockPrice(id, date);
      return ResponseEntity.ok(singleDayStockPrice);

    } catch (StockPriceNotFoundException ex) {

      String errorMsg = "Error: Stock price not found on this date: " + date;
      return new ResponseEntity<>(errorMsg, HttpStatus.NOT_FOUND);

    } catch (Exception ex) {

      String errorMsg = "Error: Failed to retrieve stock price";
      return new ResponseEntity<>(errorMsg, HttpStatus.INTERNAL_SERVER_ERROR);

    }
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
