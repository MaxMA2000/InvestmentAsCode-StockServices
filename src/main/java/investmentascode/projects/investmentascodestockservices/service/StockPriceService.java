package investmentascode.projects.investmentascodestockservices.service;

import investmentascode.projects.investmentascodestockservices.dto.StockPriceDTO;
import investmentascode.projects.investmentascodestockservices.exception.StockPriceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
public class StockPriceService {

  @Value("${DAL.url}")
  private String DALUrl;


  public StockPriceDTO getSingleDayStockPrice(String id, String date) throws StockPriceNotFoundException {
    String url = UriComponentsBuilder
      .fromHttpUrl(DALUrl + "/data/v1/stock/byAssetIdAndDateRange")
      .queryParam("asset_id", id)
      .queryParam("from", date)
      .queryParam("to", date)
      .toUriString();

    try {
      RestTemplate restTemplate = new RestTemplate();
      ResponseEntity<StockPriceDTO[]> response = restTemplate.getForEntity(url, StockPriceDTO[].class);

      if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
        throw new StockPriceNotFoundException();
      }

      StockPriceDTO[] periodStockPrice = response.getBody();

      if (periodStockPrice.length > 1) {
        throw new IllegalStateException("There are more than 1 item returned from DAL, please check.");
      }

      return periodStockPrice[0];
    } catch (HttpClientErrorException.NotFound ex) {
      throw new StockPriceNotFoundException();
    } catch (RestClientException ex) {
      throw new IllegalStateException("Failed to retrieve stock price", ex);
    }
  }


  public Boolean isFromDateBeforeToDate(String from, String to) {

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

  public List<StockPriceDTO> getPeriodStockPrice(String id, String from, String to) {

    // Build the URL with parameters using UriComponentsBuilder
    String url = UriComponentsBuilder
      .fromHttpUrl(DALUrl + "/data/v1/stock/byAssetIdAndDateRange")
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
