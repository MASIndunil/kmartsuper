package lk.kmartsuper.kmartsuper.asset.item.entity.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category {
    PROCESSED_MEAT("Processed_Meat"),
    BEVERAGES("Beverages"),
    CANNED_FOODS("Canned_Foods"),
    CONFECTIONERY_ITEMS("Confectionery_Items"),
    RICE_PULSES("Rice_Pulses"),
    CONDIMENTS_SPICES("Condiments_Spices"),
    MEAT_FISH_EGGS("Meat_Fish_Eggs"),
    OIL_FATS("Oil_Fats"),
    VEGETABLES_FRUITS("Vegetables_Fruits"),
    MILK_POWDER("Milk_Powder"),
    DAIRY_PRODUCTS("Dairy_Products"),
    BABY_PRODUCTS("Baby_Products"),
    PERSONAL_CARE("Personal_Care"),
    HOUSEHOLD("Household"),
    BREAKFAST_CERALS("Breakfast_Cerals"),
    GOURMET_INGREDIENTS("Gourmet_Ingredients"),
    DESSERT_INGREDIENTS("Dessert_Ingredients"),
    SPECIAL_SEA_FOODS("Special_Sea_Foods"),
    PARTY_FAVORS("Party_Favors"),
    ESSENTIAL_GROCERY_PACKS("Essential_Grocery_Packs");

    private final String category;
}
