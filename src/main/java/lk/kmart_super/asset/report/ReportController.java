package lk.kmart_super.asset.report;

import lk.kmart_super.asset.common_asset.model.NameCount;
import lk.kmart_super.asset.common_asset.model.ParameterCount;
import lk.kmart_super.asset.common_asset.model.TwoDate;
import lk.kmart_super.asset.employee.entity.Employee;
import lk.kmart_super.asset.invoice.entity.Invoice;
import lk.kmart_super.asset.invoice.entity.enums.PaymentMethod;
import lk.kmart_super.asset.invoice.service.InvoiceService;
import lk.kmart_super.asset.invoice_ledger.entity.InvoiceLedger;
import lk.kmart_super.asset.invoice_ledger.service.InvoiceLedgerService;
import lk.kmart_super.asset.item.entity.Item;
import lk.kmart_super.asset.payment.entity.Payment;
import lk.kmart_super.asset.payment.service.PaymentService;
import lk.kmart_super.asset.user_management.user.service.UserService;
import lk.kmart_super.util.service.DateTimeAgeService;
import lk.kmart_super.util.service.OperatorService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Controller
@RequestMapping( "/report" )
public class ReportController {
  private final PaymentService paymentService;
  private final InvoiceService invoiceService;
  private final OperatorService operatorService;
  private final DateTimeAgeService dateTimeAgeService;
  private final UserService userService;
  private final InvoiceLedgerService invoiceLedgerService;

  public ReportController(PaymentService paymentService, InvoiceService invoiceService, OperatorService operatorService, DateTimeAgeService dateTimeAgeService, UserService userService, InvoiceLedgerService invoiceLedgerService) {
    this.paymentService = paymentService;
    this.invoiceService = invoiceService;
    this.operatorService = operatorService;
    this.dateTimeAgeService = dateTimeAgeService;
    this.userService = userService;
    this.invoiceLedgerService = invoiceLedgerService;
  }

  private String commonAll(List< Payment > payments, List< Invoice > invoices, Model model, String message,
                           LocalDateTime startDateTime, LocalDateTime endDateTime) {
    //according to payment type -> invoice
    commonInvoices(invoices, model);
    //according to payment type -> payment
    commonPayments(payments, model);
    // invoice count by cashier
    commonCashierInvoice(invoices, model);
    // payment count by account department
    commonCashierPayment(payments, model);
    // item count according to item
    commonPerItem(startDateTime, endDateTime, model);

    model.addAttribute("message", message);
    return "report/managerReport";
  }

  @GetMapping( "/manager" )
  public String summaryToday(Model model) {
    LocalDate localDate = LocalDate.now();
    String message = localDate.toString();
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(localDate);
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(localDate);

    return commonAll(paymentService.findByCreatedAtIsBetween(startDateTime, endDateTime),
                     invoiceService.findByCreatedAtIsBetween(startDateTime, endDateTime), model, message,
                     startDateTime, endDateTime);

  }

  @PostMapping( "/manager/search" )
  public String summarySearch(@ModelAttribute( "twoDate" ) TwoDate twoDate, Model model) {
    String message = twoDate.getStartDate().toString() + " to " + twoDate.getEndDate().toString();
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(twoDate.getStartDate());
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(twoDate.getEndDate());
    return commonAll(paymentService.findByCreatedAtIsBetween(startDateTime, endDateTime),
                     invoiceService.findByCreatedAtIsBetween(startDateTime, endDateTime), model, message,
                     startDateTime, endDateTime);
  }

  private void commonInvoices(List< Invoice > invoices, Model model) {
    // invoice count
    int invoiceTotalCount = invoices.size();
    model.addAttribute("invoiceTotalCount", invoiceTotalCount);
    //|-> card
    List< Invoice > invoiceCards =
        invoices.stream().filter(x -> x.getPaymentMethod().equals(PaymentMethod.CREDIT)).collect(Collectors.toList());
    int invoiceCardCount = invoiceCards.size();
    AtomicReference< BigDecimal > invoiceCardAmount = new AtomicReference<>(BigDecimal.ZERO);
    invoiceCards.forEach(x -> {
      BigDecimal addAmount = operatorService.addition(invoiceCardAmount.get(), x.getTotalAmount());
      invoiceCardAmount.set(addAmount);
    });
    model.addAttribute("invoiceCardCount", invoiceCardCount);
    model.addAttribute("invoiceCardAmount", invoiceCardAmount.get());
    //|-> cash
    List< Invoice > invoiceCash =
        invoices.stream().filter(x -> x.getPaymentMethod().equals(PaymentMethod.CASH)).collect(Collectors.toList());
    int invoiceCashCount = invoiceCash.size();
    AtomicReference< BigDecimal > invoiceCashAmount = new AtomicReference<>(BigDecimal.ZERO);
    invoiceCash.forEach(x -> {
      BigDecimal addAmount = operatorService.addition(invoiceCashAmount.get(), x.getTotalAmount());
      invoiceCashAmount.set(addAmount);
    });
    model.addAttribute("invoiceCashCount", invoiceCashCount);
    model.addAttribute("invoiceCashAmount", invoiceCashAmount.get());
    model.addAttribute("cashierName", SecurityContextHolder.getContext().getAuthentication().getName());
  }

  @GetMapping( "/allInvoice" )
  public String invoiceToday(Model model) {
    LocalDate localDate = LocalDate.now();
    String message = localDate.toString();
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(localDate);
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(localDate);
    commonInvoices(invoiceService.findByCreatedAtIsBetweenAndCreatedBy(startDateTime, endDateTime,
                                                                       SecurityContextHolder.getContext().getAuthentication().getName()), model);
    model.addAttribute("message", message);
    return "report/invoiceReport";
  }

  @PostMapping( "/allInvoice/search" )
  public String invoiceSearch(@ModelAttribute( "twoDate" ) TwoDate twoDate, Model model) {
    String message = twoDate.getStartDate().toString() + " to " + twoDate.getEndDate().toString();
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(twoDate.getStartDate());
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(twoDate.getEndDate());
    commonInvoices(invoiceService.findByCreatedAtIsBetweenAndCreatedBy(startDateTime, endDateTime,
                                                                       SecurityContextHolder.getContext().getAuthentication().getName()), model);
    model.addAttribute("message", message);
    return "report/invoiceReport";
  }

  private void commonPayments(List< Payment > payments, Model model) {
    // payment count
    int paymentTotalCount = payments.size();
    model.addAttribute("paymentTotalCount", paymentTotalCount);
    //|-> card
    List< Payment > paymentCards =
        payments.stream().filter(x -> x.getPaymentMethod().equals(PaymentMethod.CREDIT)).collect(Collectors.toList());
    int paymentCardCount = paymentCards.size();
    AtomicReference< BigDecimal > paymentCardAmount = new AtomicReference<>(BigDecimal.ZERO);
    paymentCards.forEach(x -> {
      BigDecimal addAmount = operatorService.addition(paymentCardAmount.get(), x.getAmount());
      paymentCardAmount.set(addAmount);
    });
    model.addAttribute("paymentCardCount", paymentCardCount);
    model.addAttribute("paymentCardAmount", paymentCardAmount.get());
    //|-> cash
    List< Payment > paymentCash =
        payments.stream().filter(x -> x.getPaymentMethod().equals(PaymentMethod.CASH)).collect(Collectors.toList());
    int paymentCashCount = paymentCash.size();
    AtomicReference< BigDecimal > paymentCashAmount = new AtomicReference<>(BigDecimal.ZERO);
    paymentCash.forEach(x -> {
      BigDecimal addAmount = operatorService.addition(paymentCashAmount.get(), x.getAmount());
      paymentCashAmount.set(addAmount);
    });
    model.addAttribute("paymentCashCount", paymentCashCount);
    model.addAttribute("paymentCashAmount", paymentCashAmount.get());
    model.addAttribute("cashierName", SecurityContextHolder.getContext().getAuthentication().getName());
  }

  @GetMapping( "/allPayment" )
  public String paymentToday(Model model) {
    LocalDate localDate = LocalDate.now();
    String message = localDate.toString();
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(localDate);
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(localDate);
    commonPayments(paymentService.findByCreatedAtIsBetweenAndCreatedBy(startDateTime, endDateTime,
                                                                      SecurityContextHolder.getContext().getAuthentication().getName()), model);
    model.addAttribute("message", message);
    return "report/paymentReport";
  }

  @PostMapping( "/allPayment/search" )
  public String paymentSearch(@ModelAttribute( "twoDate" ) TwoDate twoDate, Model model) {
    String message = twoDate.getStartDate().toString() + " to " + twoDate.getEndDate().toString();
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(twoDate.getStartDate());
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(twoDate.getEndDate());
    commonPayments(paymentService.findByCreatedAtIsBetweenAndCreatedBy(startDateTime, endDateTime,
                                                                      SecurityContextHolder.getContext().getAuthentication().getName()), model);
    model.addAttribute("message", message);
    return "report/paymentReport";
  }

  private void commonCashierInvoice(List< Invoice > invoices, Model model) {
    List< NameCount > invoiceByCashierAndTotalAmount = new ArrayList<>();
//name, count, total
    HashSet< String > relatedCashiers = new HashSet<>();
    invoices.forEach(x -> relatedCashiers.add(x.getCreatedBy()));

    relatedCashiers.forEach(x -> {
      NameCount nameCount = new NameCount();
      Employee employee = userService.findByUserName(x).getEmployee();
      nameCount.setCode(employee.getCode());
      nameCount.setName(employee.getTitle().getTitle() + " " + employee.getCallingName());
      AtomicReference< BigDecimal > cashierTotalAmount = new AtomicReference<>(BigDecimal.ZERO);
      List< Invoice > cashierInvoices =
          invoices.stream().filter(a -> a.getCreatedBy().equals(x)).collect(Collectors.toList());
      nameCount.setCount(cashierInvoices.size());
      cashierInvoices.forEach(a -> {
        BigDecimal addAmount = operatorService.addition(cashierTotalAmount.get(), a.getTotalAmount());
        cashierTotalAmount.set(addAmount);
      });
      nameCount.setTotal(cashierTotalAmount.get());
      invoiceByCashierAndTotalAmount.add(nameCount);
    });
    model.addAttribute("invoiceByCashierAndTotalAmount", invoiceByCashierAndTotalAmount);
  }

  @GetMapping( "/perCashierInvoice" )
  public String cashierInvoiceToday(Model model) {
    LocalDate localDate = LocalDate.now();
    String message = localDate.toString();
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(localDate);
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(localDate);
    commonCashierInvoice(invoiceService.findByCreatedAtIsBetween(startDateTime, endDateTime), model);
    model.addAttribute("message", message);
    return "report/invoiceByCashierReport";
  }

  @PostMapping( "/perCashierInvoice/search" )
  public String cashierInvoiceSearch(@ModelAttribute( "twoDate" ) TwoDate twoDate, Model model) {
    String message = twoDate.getStartDate().toString() + " to " + twoDate.getEndDate().toString();
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(twoDate.getStartDate());
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(twoDate.getEndDate());
    commonCashierInvoice(invoiceService.findByCreatedAtIsBetween(startDateTime, endDateTime), model);
    model.addAttribute("message", message);
    return "report/invoiceByCashierReport";
  }

  private void commonCashierPayment(List< Payment > payments, Model model) {
    List< NameCount > paymentsByCashierAndTotalAmount = new ArrayList<>();
//name, count, total
    HashSet< String > relatedCashiers = new HashSet<>();
    payments.forEach(x -> relatedCashiers.add(x.getCreatedBy()));

    relatedCashiers.forEach(x -> {
      NameCount nameCount = new NameCount();
      Employee employee = userService.findByUserName(x).getEmployee();
      nameCount.setCode(employee.getCode());
      nameCount.setName(employee.getTitle().getTitle() + " " + employee.getCallingName());
      AtomicReference< BigDecimal > cashierTotalPayment = new AtomicReference<>(BigDecimal.ZERO);
     // List< BigDecimal > userTotalCount = new ArrayList<>();
      List< Payment > cashierPayments =
          payments.stream().filter(a -> a.getCreatedBy().equals(x)).collect(Collectors.toList());
      nameCount.setCount(cashierPayments.size());
      cashierPayments.forEach(a -> {
        BigDecimal addAmount = operatorService.addition(cashierTotalPayment.get(), a.getAmount());
        cashierTotalPayment.set(addAmount);
        //userTotalCount.add(a.getAmount());
      });
      nameCount.setTotal(cashierTotalPayment.get());
      //nameCount.setTotal(userTotalCount.stream().reduce(BigDecimal.ZERO,BigDecimal::add));
      paymentsByCashierAndTotalAmount.add(nameCount);
    });

    model.addAttribute("paymentsByCashierAndTotalAmount", paymentsByCashierAndTotalAmount);

  }

  @GetMapping( "/perCashierPayment" )
  public String cashierPaymentToday(Model model) {
    LocalDate localDate = LocalDate.now();
    String message = localDate.toString();
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(localDate);
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(localDate);
    commonCashierPayment(paymentService.findByCreatedAtIsBetween(startDateTime, endDateTime), model);
    model.addAttribute("message", message);
    return "report/paymentByCashierReport";
  }

  @PostMapping( "/perCashierPayment/search" )
  public String cashierPaymentSearch(@ModelAttribute( "twoDate" ) TwoDate twoDate, Model model) {
    String message = twoDate.getStartDate().toString() + " to " + twoDate.getEndDate().toString();
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(twoDate.getStartDate());
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(twoDate.getEndDate());
    commonCashierPayment(paymentService.findByCreatedAtIsBetween(startDateTime, endDateTime), model);
    model.addAttribute("message", message);
    return "report/paymentByCashierReport";
  }



  private void commonPerItem(LocalDateTime startDateTime, LocalDateTime endDateTime, Model model) {
    HashSet< Item > invoiceItems = new HashSet<>();

    List< ParameterCount > itemNameAndItemCount = new ArrayList<>();

    List< InvoiceLedger > invoiceLedgers = invoiceLedgerService.findByCreatedAtIsBetween(startDateTime, endDateTime);
    invoiceLedgers.forEach(x -> invoiceItems.add(x.getLedger().getItem()));

    invoiceItems.forEach(x -> {
      ParameterCount parameterCount = new ParameterCount();
      parameterCount.setCode(x.getCode());
      parameterCount.setName(x.getName());
      List<InvoiceLedger> relatedILS = new ArrayList<>();
      for (InvoiceLedger invoiceLedger:invoiceLedgers) {
        if (invoiceLedger.getLedger().getItem().equals(x)){
          relatedILS.add(invoiceLedger);
        }
      }
      int totalQuantity = 0;
      for (InvoiceLedger relatedIL:relatedILS) {
        totalQuantity += Integer.parseInt(relatedIL.getQuantity());
      }
      parameterCount.setQuantity(totalQuantity);
      /*parameterCount.setCount((int) invoiceLedgers
          .stream()
          .filter(a -> a.getLedger().getItem().equals(x))
          .count());*/
      itemNameAndItemCount.add(parameterCount);
    });
    model.addAttribute("itemNameAndItemCount", itemNameAndItemCount);

  }

  @GetMapping( "/perItem" )
  public String perItemToday(Model model) {
    LocalDate localDate = LocalDate.now();
    String message = localDate.toString();
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(localDate);
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(localDate);
    commonPerItem(startDateTime, endDateTime, model);
    model.addAttribute("message", message);
    return "report/perItemReport";
  }

  @PostMapping( "/perItem/search" )
  public String getPerItemSearch(@ModelAttribute( "twoDate" ) TwoDate twoDate, Model model) {
    String message = twoDate.getStartDate().toString() + " to " + twoDate.getEndDate().toString();
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(twoDate.getStartDate());
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(twoDate.getEndDate());
    commonPerItem(startDateTime, endDateTime, model);
    model.addAttribute("message", message);
    return "report/perItemReport";
  }

}
