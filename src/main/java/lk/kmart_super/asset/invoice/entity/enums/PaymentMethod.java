package lk.kmart_super.asset.invoice.entity.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentMethod {
    CASH("Cash"),
    CREDIT("Card");
    private final String paymentMethod;
}
