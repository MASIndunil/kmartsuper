package lk.kmartsuper.asset.commonAsset.model.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Title {
    Mr("Mr. "),
    Mrs("Mrs. "),
    Miss("Miss. "),
    Rev("Rev. "),
    Dr("Dr. "),
    Sister("Sister. ");

    private final String title;
}
