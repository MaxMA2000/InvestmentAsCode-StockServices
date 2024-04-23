package investmentascode.projects.investmentascodestockservices.controller;

import investmentascode.projects.investmentascodestockservices.dto.StockInfoDTO;
import investmentascode.projects.investmentascodestockservices.dto.StockPriceDTO;
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


  @GetMapping("/v1/stock/{id}/from/{from}/to/{to}")
  public ResponseEntity<?> getPeriodStockPrice(
    @PathVariable String id,
    @PathVariable String from,
    @PathVariable String to) {


    // Define a DateTimeFormatter for the input format
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Convert user input to LocalDate
    LocalDate fromDate = LocalDate.parse(from, formatter);
    LocalDate toDate = LocalDate.parse(to, formatter);

    if (fromDate.isAfter(toDate)) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("from Date can't be After to Date , Please Check.");
    }
    if (fromDate.isEqual(toDate)) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("from Date can't be same as to Date, Please Check.");
    }

    // Build the URL with parameters using UriComponentsBuilder
    String url = UriComponentsBuilder
      .fromHttpUrl("http://localhost:8080/data/v1/stock/byAssetIdAndDateRange")
      .queryParam("asset_id", id)
      .queryParam("from", from)
      .queryParam("to", to)
      .toUriString();


    RestTemplate restTemplate = new RestTemplate();


    ResponseEntity<StockPriceDTO[]> response = restTemplate.getForEntity(url, StockPriceDTO[].class);

    StockPriceDTO[] periodStockPrice = response.getBody();

    List<StockPriceDTO> stockPriceList = Arrays.asList(periodStockPrice);


    return new ResponseEntity<List<StockPriceDTO>>(stockPriceList, HttpStatus.OK);

  }


}
