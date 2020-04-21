package lk.kmartsuper.asset.commonAsset.model.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CivilStatus {
    Single("Single"),
    Married("Married");

    private final String civilStatus;
}
