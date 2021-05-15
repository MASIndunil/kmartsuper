package lk.kmart_super.asset.invoice.entity.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InvoicePrintOrNot {
  PRINTED("No"),
  NOT_PRINTED("Yes");
  private final String invoicePrintOrNot;
}
