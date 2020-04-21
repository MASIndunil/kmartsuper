package lk.kmartsuper.asset.employee.entity.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Designation {
    //All station can check
    Cashier("Cashier"),
    Sales_Ass("Sales Assistant"),
    Sales_Man("Sales Manager"),
    Store_Ass("Store Assistant"),
    Store_Man("Store Manager"),
    Owner("Owner");


    private final String designation;
}
