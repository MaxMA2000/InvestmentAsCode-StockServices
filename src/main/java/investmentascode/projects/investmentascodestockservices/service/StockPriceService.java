package investmentascode.projects.investmentascodestockservices.service;

import investmentascode.projects.investmentascodestockservices.dto.StockPriceDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
public class StockPriceService {

  public Boolean isFromDateBeforeToDate(String from, String to){

    // Define a DateTimeFormatter for the input format
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Convert user input to LocalDate
    LocalDate fromDate = LocalDate.parse(from, formatter);
    LocalDate toDate = LocalDate.parse(to, formatter);

    if (fromDate.isBefore(toDate)) {
      return Boolean.TRUE;
    } else {
      return Boolean.FALSE;
    }
  }

  public List<StockPriceDTO> getPeriodStockPrice(String id, String from, String to){

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

    return stockPriceList;
  }





}
