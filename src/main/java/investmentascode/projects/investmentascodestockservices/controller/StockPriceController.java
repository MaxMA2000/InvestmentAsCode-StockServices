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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
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

  @GetMapping("/v1/stock/{id}/{date}")
  public ResponseEntity<?> getSingleDayStockPrice(
    @PathVariable String id,
    @PathVariable String date
  ) {

    // Build the URL with parameters using UriComponentsBuilder
    String url = UriComponentsBuilder
      .fromHttpUrl("http://localhost:8080/data/v1/stock/byAssetIdAndDateRange")
      .queryParam("asset_id", id)
      .queryParam("from", date)
      .queryParam("to", date)
      .toUriString();


    RestTemplate restTemplate = new RestTemplate();

    try {
      ResponseEntity<StockPriceDTO[]> response = restTemplate.getForEntity(url, StockPriceDTO[].class);

      if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
        System.out.println("Error: Stock price not found on this date");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      }

      StockPriceDTO[] periodStockPrice = response.getBody();

      if (periodStockPrice.length > 1) {
        System.out.println("There are more than 1 item returned from DAL, Please Check.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }

      StockPriceDTO singleDayStockPrice = periodStockPrice[0];
      return ResponseEntity.ok(singleDayStockPrice);

    } catch (HttpClientErrorException.NotFound ex) {

      String errorMsg = "Error: Stock price not found on this date: " + date;
      return new ResponseEntity<String>(errorMsg, HttpStatus.NOT_FOUND);

    } catch (RestClientException ex) {

      String errorMsg = "Error: Failed to retrieve stock price";
      return new ResponseEntity<String>(errorMsg, HttpStatus.INTERNAL_SERVER_ERROR);

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
