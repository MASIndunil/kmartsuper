package lk.kmart_super.asset.common_asset.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Title {
    MR("Mr. "),
    MRS("Mrs. "),
    MISS("Miss. "),
    REV("Rev. "),
    SISTER("Sister. ");

    private final String title;
}
