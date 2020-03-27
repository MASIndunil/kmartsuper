package lk.kmartsuper.kmartsuper.asset.item.entity.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {

    AVAILABLE("Available"),
    NOT_AVAILABLE("Not_Available"),
    ORDERED("Ordered");

    private final String status;
}
