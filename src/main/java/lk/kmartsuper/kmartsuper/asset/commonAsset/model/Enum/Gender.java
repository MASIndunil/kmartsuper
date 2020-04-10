package lk.kmartsuper.kmartsuper.asset.commonAsset.model.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
    Male("Male"),
    Female("Female");

    private final String gender;
}
