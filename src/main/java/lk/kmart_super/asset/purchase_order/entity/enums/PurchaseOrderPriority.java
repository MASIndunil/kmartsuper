package lk.kmart_super.asset.purchase_order.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PurchaseOrderPriority {
    HIGH("3"),
    MEDIUM("5"),
    NORMAL("7");
    private final String purchaseOrderPriority;
}
