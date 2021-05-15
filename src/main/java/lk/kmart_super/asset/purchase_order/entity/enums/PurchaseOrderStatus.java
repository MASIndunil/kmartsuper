package lk.kmart_super.asset.purchase_order.entity.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PurchaseOrderStatus {

    NOT_COMPLETED("Goods Not Received"),
    NOT_PROCEED("Payments Not Completed"),
    COMPLETED("Completed");

    private final String purchaseOrderStatus;

}
