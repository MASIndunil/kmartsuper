package lk.kmart_super.asset.invoice.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lk.kmart_super.asset.common_asset.model.enums.LiveDead;
import lk.kmart_super.asset.invoice.dao.InvoiceDao;
import lk.kmart_super.asset.invoice.entity.Invoice;
import lk.kmart_super.asset.invoice.entity.enums.PaymentMethod;
import lk.kmart_super.util.interfaces.AbstractService;
import lk.kmart_super.util.service.OperatorService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceService implements AbstractService< Invoice, Integer > {
  private final InvoiceDao invoiceDao;

  public InvoiceService(InvoiceDao invoiceDao, OperatorService operatorService) {
    this.invoiceDao = invoiceDao;
    this.operatorService = operatorService;
  }
  private final OperatorService operatorService;

  public List< Invoice > findAll() {
    return invoiceDao.findAll().stream()
        .filter(x -> LiveDead.ACTIVE.equals(x.getLiveDead()))
        .collect(Collectors.toList());
  }

  public Invoice findById(Integer id) {
    return invoiceDao.getOne(id);
  }

  public Invoice persist(Invoice invoice) {
    if ( invoice.getId() == null ) {
      invoice.setLiveDead(LiveDead.ACTIVE);
    }
    return invoiceDao.save(invoice);
  }

  public boolean delete(Integer id) {
    Invoice invoice = invoiceDao.getOne(id);
    invoice.setLiveDead(LiveDead.STOP);
    invoiceDao.save(invoice);
    return false;
  }

  public List< Invoice > search(Invoice invoice) {
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< Invoice > invoiceExample = Example.of(invoice, matcher);
    return invoiceDao.findAll(invoiceExample);

  }

  public List< Invoice > findByCreatedAtIsBetween(LocalDateTime from, LocalDateTime to) {
    return invoiceDao.findByCreatedAtIsBetween(from, to).stream()
            .filter(x -> LiveDead.ACTIVE.equals(x.getLiveDead()))
            .collect(Collectors.toList());
  }

  public Invoice findByLastInvoice() {
    return invoiceDao.findFirstByOrderByIdDesc();
  }

  public List< Invoice > findByCreatedAtIsBetweenAndCreatedBy(LocalDateTime from, LocalDateTime to, String userName) {
    return invoiceDao.findByCreatedAtIsBetweenAndCreatedBy(from, to, userName);
  }

  public ByteArrayInputStream createPDF(Integer id) throws DocumentException {
    Invoice invoice = invoiceDao.getOne(id);
    ByteArrayOutputStream out = new ByteArrayOutputStream();

// Document details
    Document document = new Document(PageSize.A7, 1, 1, 3, 3);
    PdfWriter.getInstance(document, out);
    document.open();

// Font details
    Font Font1 = FontFactory.getFont("times-bolditalic", 12, BaseColor.BLACK);
    Font Font2 = FontFactory.getFont("Arial", 5, BaseColor.BLACK);
    Font Font3 = FontFactory.getFont("times-roman", 6, BaseColor.BLACK);
    Font highLiltedFont = FontFactory.getFont("Arial", 5, BaseColor.BLACK);

// Available font names
    /*Set<String> regFonts = FontFactory.getRegisteredFonts();
    for (String font:regFonts) {
      Paragraph fontName = new Paragraph(font, Font2);
      document.add(fontName);
    }*/

//Shop name
    Paragraph shopName = new Paragraph("Kmart Super", Font1);
    shopName.setAlignment(Element.ALIGN_CENTER);
    shopName.setIndentationLeft(50);
    shopName.setIndentationRight(50);
    shopName.setSpacingAfter(3);
    document.add(shopName);

//Shop address and contact details
    Paragraph shopAddress = new Paragraph("No. 409/A, Colombo Rd, Bandiyamulle, Gampaha. \n Tel: 033-2232699 / 071-4931346", Font3);
    shopAddress.setAlignment(Element.ALIGN_CENTER);
    shopAddress.setIndentationLeft(10);
    shopAddress.setIndentationRight(10);
    shopAddress.setSpacingAfter(1);
    document.add(shopAddress);

//Date-Time and User details
    Paragraph DateTimeUser = new Paragraph(new Phrase(invoice.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss"))+"  C:"+invoice.getCreatedBy(), Font3));
    DateTimeUser.setAlignment(Element.ALIGN_CENTER);
    DateTimeUser.setIndentationLeft(10);
    DateTimeUser.setIndentationRight(10);
    DateTimeUser.setSpacingAfter(1);
    document.add(DateTimeUser);

//Invoice Number
    Paragraph invoiceNumber = new Paragraph(new Phrase("Invoice Number: " + invoice.getCode(), Font3));
    invoiceNumber.setAlignment(Element.ALIGN_CENTER);
    invoiceNumber.setIndentationLeft(10);
    invoiceNumber.setIndentationRight(10);
    invoiceNumber.setSpacingAfter(0);
    document.add(invoiceNumber);

//DashLine1
    Paragraph dashLine1 = new Paragraph(new Phrase(
            "------------------------------------------------------------------------------------------" +
                    "----------------------------------", Font2));
    dashLine1.setAlignment(Element.ALIGN_LEFT);
    dashLine1.setIndentationLeft(0);
    dashLine1.setIndentationRight(0);
    dashLine1.setSpacingAfter(0);
    document.add(dashLine1);

//Item table details
    Rectangle page = document.getPageSize();

    //Table settings
    PdfPTable ledgerItemDisplay = new PdfPTable(7);
    ledgerItemDisplay.setWidthPercentage(100);
    ledgerItemDisplay.setSpacingBefore(0);
    ledgerItemDisplay.setSpacingAfter(2);
    ledgerItemDisplay.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());

    //Table font settings
    Font theadFont = FontFactory.getFont("courier-bold", 6, BaseColor.BLACK);
    Font tbodyFont = FontFactory.getFont("courier", 5, BaseColor.BLACK);

    /*PdfPCell codeIndex = new PdfPCell(new Paragraph("Ln", theadFont));
    pdfCellHeaderCommonStyle(codeIndex);
    ledgerItemDisplay.addCell(codeIndex);*/

    //Table headers
    PdfPCell itemNameHeader = new PdfPCell(new Paragraph("Item", theadFont));
    itemNameHeader.setColspan(4);
    pdfCellHeaderCommonStyle(itemNameHeader);
    itemNameHeader.setHorizontalAlignment(Element.ALIGN_LEFT);
    ledgerItemDisplay.addCell(itemNameHeader);

    PdfPCell unitPriceHeader = new PdfPCell(new Paragraph("Price", theadFont));
    pdfCellHeaderCommonStyle(unitPriceHeader);
    ledgerItemDisplay.addCell(unitPriceHeader);

    PdfPCell quantityHeader = new PdfPCell(new Paragraph("Qty", theadFont));
    pdfCellHeaderCommonStyle(quantityHeader);
    ledgerItemDisplay.addCell(quantityHeader);

    /*PdfPCell discountHeader = new PdfPCell(new Paragraph("Discount", theadFont));
    pdfCellHeaderCommonStyle(discountHeader);
    ledgerItemDisplay.addCell(discountHeader);*/

    PdfPCell lineTotalHeader = new PdfPCell(new Paragraph("Amount", theadFont));
    pdfCellHeaderCommonStyle(lineTotalHeader);
    ledgerItemDisplay.addCell(lineTotalHeader);

    //Table body
    for ( int i = 0; i < invoice.getInvoiceLedgers().size(); i++ ) {

      /*PdfPCell index = new PdfPCell(new Paragraph(Integer.toString(i+1), tbodyFont));
      pdfCellBodyCommonStyle(index);
      ledgerItemDisplay.addCell(index);*/

      PdfPCell itemName = new PdfPCell(new Paragraph(invoice.getInvoiceLedgers().get(i).getLedger().getItem().getName(), tbodyFont));
      itemName.setColspan(4);
      pdfCellBodyCommonStyle(itemName);
      itemName.setHorizontalAlignment(Element.ALIGN_LEFT);
      ledgerItemDisplay.addCell(itemName);

      PdfPCell unitPrice = new PdfPCell(new Paragraph(invoice.getInvoiceLedgers().get(i).getLedger().getSellPrice().toString(), tbodyFont));
      pdfCellBodyCommonStyle(unitPrice);
      ledgerItemDisplay.addCell(unitPrice);

      PdfPCell quantity = new PdfPCell(new Paragraph(invoice.getInvoiceLedgers().get(i).getQuantity(), tbodyFont));
      pdfCellBodyCommonStyle(quantity);
      quantity.setHorizontalAlignment(Element.ALIGN_CENTER);
      ledgerItemDisplay.addCell(quantity);

     /* PdfPCell discount = new PdfPCell(new Paragraph(
              operatorService.subtraction(
                      operatorService.multiply(
                              invoice.getInvoiceLedgers().get(i).getLedger().getSellPrice(),
                              invoice.getInvoiceLedgers().get(i).getQuantity()
                      )
                      ,invoice.getInvoiceLedgers().get(i).getLineTotal()).toString()
              , tbodyFont));
      pdfCellBodyCommonStyle(discount);
      ledgerItemDisplay.addCell(discount);*/

      PdfPCell lineTotal = new PdfPCell(new Paragraph(invoice.getInvoiceLedgers().get(i).getLineTotal().toString(), tbodyFont));
      pdfCellBodyCommonStyle(lineTotal);
      ledgerItemDisplay.addCell(lineTotal);
    }

    document.add(ledgerItemDisplay);

//DashLine2
    Paragraph dashLine2 = new Paragraph(new Phrase(
            "------------------------------------------------------------------------------------------" +
                    "----------------------------------", Font2));
    dashLine2.setAlignment(Element.ALIGN_LEFT);
    dashLine2.setIndentationLeft(0);
    dashLine2.setIndentationRight(0);
    dashLine2.setSpacingAfter(1);
    document.add(dashLine2);

//Payment Details
    //Table settings
    PdfPTable invoiceTable = new PdfPTable(new float[]{3f, 0.75f});
    invoiceTable.setWidthPercentage(100);
    ledgerItemDisplay.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());

    //Row-1
    PdfPCell totalAmount = new PdfPCell(new Phrase("Gross Amount(Rs.) : ", Font2));
    commonStyleForPdfPCellLastOne(totalAmount);
    invoiceTable.addCell(totalAmount);

    PdfPCell totalAmountRs = new PdfPCell(new Phrase(invoice.getTotalPrice().setScale(2, BigDecimal.ROUND_CEILING).toString(), Font2));
    commonStyleForPdfPCellLastOne(totalAmountRs);
    invoiceTable.addCell(totalAmountRs);

    //Row-2
    PdfPCell discountRadioAndAmount = new PdfPCell(new Phrase("Discount ( " + invoice.getDiscountRatio().getAmount() + "% )(Rs.) : ", Font2));
    commonStyleForPdfPCellLastOne(discountRadioAndAmount);
    invoiceTable.addCell(discountRadioAndAmount);

    PdfPCell discountRadioAndAmountRs = new PdfPCell(new Phrase(invoice.getDiscountAmount().toString()+"\n----------------", Font2));
    commonStyleForPdfPCellLastOne(discountRadioAndAmountRs);
    invoiceTable.addCell(discountRadioAndAmountRs);

    //Row-3
    PdfPCell amount = new PdfPCell(new Phrase("Net Amount(Rs.) : ", highLiltedFont));
    commonStyleForPdfPCellLastOne(amount);
    invoiceTable.addCell(amount);

    PdfPCell amountRs = new PdfPCell(new Phrase(invoice.getTotalAmount().toString(), highLiltedFont));
    commonStyleForPdfPCellLastOne(amountRs);
    invoiceTable.addCell(amountRs);

    //Row-4
    PdfPCell paymentMethodOnBill = new PdfPCell(new Phrase("\nPayment Method : ", Font2));
    commonStyleForPdfPCellLastOne(paymentMethodOnBill);
    invoiceTable.addCell(paymentMethodOnBill);

    PdfPCell paymentMethodOnBillState = new PdfPCell(new Phrase("=========\n" + invoice.getPaymentMethod().getPaymentMethod(), Font2));
    commonStyleForPdfPCellLastOne(paymentMethodOnBillState);
    invoiceTable.addCell(paymentMethodOnBillState);

    //Row-5,6
    if (invoice.getPaymentMethod().equals(PaymentMethod.CASH)) {
      PdfPCell amountTendered = new PdfPCell(new Phrase("Cash(Rs.) : ", Font2));
      commonStyleForPdfPCellLastOne(amountTendered);
      invoiceTable.addCell(amountTendered);

      PdfPCell amountTenderedRs = new PdfPCell(new Phrase(invoice.getAmountTendered().toString(), Font2));
      commonStyleForPdfPCellLastOne(amountTenderedRs);
      invoiceTable.addCell(amountTenderedRs);

      PdfPCell balance = new PdfPCell(new Phrase("Change(Rs.) : ", highLiltedFont));
      commonStyleForPdfPCellLastOne(balance);
      invoiceTable.addCell(balance);

      PdfPCell balanceRs = new PdfPCell(new Phrase(invoice.getBalance().toString(), highLiltedFont));
      commonStyleForPdfPCellLastOne(balanceRs);
      invoiceTable.addCell(balanceRs);

    } else {
      PdfPCell bank = new PdfPCell(new Phrase("Bank Name : ", Font2));
      commonStyleForPdfPCellLastOne(bank);
      invoiceTable.addCell(bank);

      PdfPCell bankName = new PdfPCell(new Phrase(invoice.getBankName(), Font2));
      commonStyleForPdfPCellLastOne(bankName);
      invoiceTable.addCell(bankName);
    }

    document.add(invoiceTable);

//DashLine3
    Paragraph dashLine3 = new Paragraph(new Phrase(
            "------------------------------------------------------------------------------------------" +
                    "----------------------------------", Font2));
    dashLine3.setAlignment(Element.ALIGN_LEFT);
    dashLine3.setIndentationLeft(0);
    dashLine3.setIndentationRight(0);
    dashLine3.setSpacingAfter(1);
    document.add(dashLine3);

//Remarks and Message
    /*Paragraph remarks = new Paragraph("Remarks : " + invoice.getRemarks(), Font2);
    commonStyleForParagraphTwo(remarks);
    document.add(remarks);

    Paragraph message = new Paragraph("\nWe will not accept returns without  the invoice. \n\n ------------------------------------\n            ( " + invoice.getCreatedBy() + " )", Font2);
    commonStyleForParagraphTwo(message);
    document.add(message);*/

    //customer details and invoice main details
    /*float[] columnWidths = {200f, 200f};//column amount{column 1 , column 2 }
    PdfPTable mainTable = new PdfPTable(columnWidths);
    // add cell to table
    PdfPCell cell = new PdfPCell(new Phrase("Date : \t" + invoice.getCreatedAt().format(DateTimeFormatter.ofPattern(
        "yyyy-MM-dd HH:mm:ss")), Font2));
    pdfCellBodyCommonStyle(cell);
    mainTable.addCell(cell);

    PdfPCell cell1 = new PdfPCell(new Phrase("Invoice Number : " + invoice.getCode(), Font2));
    pdfCellBodyCommonStyle(cell1);
    mainTable.addCell(cell1);

    PdfPCell cell2;
    if ( invoice.getCustomer() != null ) {
      cell2 =
          new PdfPCell(new Phrase("Name : " + invoice.getCustomer().getTitle().getTitle() + " " + invoice.getCustomer().getName(), Font2));
    } else {
      cell2 = new PdfPCell(new Phrase("Name : Anonymous Customer ", Font2));
    }
    pdfCellBodyCommonStyle(cell2);
    mainTable.addCell(cell2);
    document.add(mainTable);*/

//Customer Thank
    Paragraph  customerName;
    if ( invoice.getCustomer() != null ) {
        customerName = new Paragraph("THANK YOU, COME AGAIN !!!\n" + invoice.getCustomer().getTitle().getTitle() + " "
              + invoice.getCustomer().getName(), Font3);
    }else {
        customerName = new Paragraph("THANK YOU, COME AGAIN !!!", Font3);
    }
    customerName.setAlignment(Element.ALIGN_CENTER);
    customerName.setIndentationLeft(50);
    customerName.setIndentationRight(50);
    customerName.setSpacingAfter(1);
    document.add(customerName);

//Important Notice
    Paragraph impNotice = new Paragraph("IMPORTANT NOTICE:\nIn case of price discrepancy, " +
            "return the item & bill within 7 days for refund of difference.\n *****CopyRights @ Kmart***** ", Font2);
    impNotice.setAlignment(Element.ALIGN_CENTER);
    impNotice.setIndentationLeft(5);
    impNotice.setIndentationRight(5);
    impNotice.setSpacingAfter(3);
    document.add(impNotice);

    document.close();
    return new ByteArrayInputStream(out.toByteArray());
  }

  //Styles
  private void pdfCellHeaderCommonStyle(PdfPCell pdfPCell) {
    pdfPCell.setBorder(0);
    pdfPCell.setPaddingLeft(1);
    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    pdfPCell.setVerticalAlignment(Element.ALIGN_TOP);
    pdfPCell.setBackgroundColor(BaseColor.WHITE);
    //pdfPCell.setExtraParagraphSpace(5f);
  }

  private void pdfCellBodyCommonStyle(PdfPCell pdfPCell) {
    pdfPCell.setBorder(0);
    pdfPCell.setPaddingLeft(1);
    pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    pdfPCell.setVerticalAlignment(Element.ALIGN_TOP);
    pdfPCell.setBackgroundColor(BaseColor.WHITE);
    //pdfPCell.setExtraParagraphSpace(5f);
  }

  private void commonStyleForPdfPCellLastOne(PdfPCell pdfPCell) {
    pdfPCell.setBorder(0);
    pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    pdfPCell.setBorderColor(BaseColor.WHITE);
  }

  /*private void commonStyleForParagraphTwo(Paragraph paragraph) {
    paragraph.setAlignment(Element.ALIGN_LEFT);
    paragraph.setIndentationLeft(50);
    paragraph.setIndentationRight(50);
  }*/
}
