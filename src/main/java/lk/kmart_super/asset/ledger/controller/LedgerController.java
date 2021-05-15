package lk.kmart_super.asset.ledger.controller;


import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lk.kmart_super.asset.common_asset.model.TwoDate;
import lk.kmart_super.asset.ledger.service.LedgerService;
import lk.kmart_super.util.service.DateTimeAgeService;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Controller
@RequestMapping( "/ledger" )
public class LedgerController {
  private final LedgerService ledgerService;
  private final DateTimeAgeService dateTimeAgeService;

  public LedgerController(LedgerService ledgerService, DateTimeAgeService dateTimeAgeService) {
    this.ledgerService = ledgerService;
    this.dateTimeAgeService = dateTimeAgeService;
  }

  //all ledgers
  @GetMapping
  public String findAllLed(Model model) {
    model.addAttribute("title", "All items in the stock");
    model.addAttribute("ledgers", ledgerService.findAll());
    model.addAttribute("twoDate", new TwoDate());
    return "ledger/ledger";
  }

  //reorder point < item count
  /*@GetMapping( "/reorderPoint" )
  public String reorderPoint(Model model) {
    model.addAttribute("title", "Reorder Point Limit Exceeded");
    model.addAttribute("ledgers", ledgerService.findAll()
        .stream()
        .filter(x -> Integer.parseInt(x.getQuantity()) <= Integer.parseInt(x.getItem().getRop()))
        .collect(Collectors.toList()));
    model.addAttribute("twoDate", new TwoDate());
    return "ledger/ledger";
  }*/

  //near expired date
  @PostMapping( "/search" )
  public String expiredDate(@ModelAttribute TwoDate twoDate, Model model) {
    System.out.println("star date " + twoDate.getStartDate() + " end " + twoDate.getEndDate());
    model.addAttribute("title",
                       twoDate.getStartDate() + "  to  " + twoDate.getEndDate());
    model.addAttribute("ledgers",
                       ledgerService.findByExpiredDateBetween(twoDate.getStartDate(), twoDate.getEndDate()));
    System.out.println("star date " + dateTimeAgeService.dateTimeToLocalDateStartInDay(twoDate.getStartDate()) + " " +
                           "end " + dateTimeAgeService.dateTimeToLocalDateEndInDay(twoDate.getEndDate()));
    model.addAttribute("twoDate", new TwoDate());
    return "ledger/ledger";
  }

  @GetMapping( "/{id}" )
  @ResponseBody
  public MappingJacksonValue findId(@PathVariable Integer id) {
    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(ledgerService.findById(id));
    SimpleBeanPropertyFilter simpleBeanPropertyFilterOne = SimpleBeanPropertyFilter
        .filterOutAllExcept("id", "quantity", "sellPrice", "item","expiredDate");

    SimpleBeanPropertyFilter simpleBeanPropertyFilterTwo = SimpleBeanPropertyFilter
        .filterOutAllExcept("id", "name");

    FilterProvider filters = new SimpleFilterProvider()
        .addFilter("Ledger", simpleBeanPropertyFilterOne)
        .addFilter("Item", simpleBeanPropertyFilterTwo);
    mappingJacksonValue.setFilters(filters);
    return mappingJacksonValue;
  }
}
