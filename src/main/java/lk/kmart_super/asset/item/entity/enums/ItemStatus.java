package lk.kmart_super.asset.item.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemStatus {

    AVAILABLE("Available"),
    UNAVAILABLE("Unavailable"),
    ORDERED("Ordered"),
    NEW("New");

    private final String itemStatus;
}
