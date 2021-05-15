package lk.kmart_super.asset.discount_ratio.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.kmart_super.asset.common_asset.model.enums.LiveDead;
import lk.kmart_super.asset.discount_ratio.entity.enums.DiscountRatioStatus;
import lk.kmart_super.asset.invoice.entity.Invoice;
import lk.kmart_super.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "DiscountRatio" )
public class DiscountRatio extends AuditEntity {

    @Column( nullable = false, length = 45 )
    private String name;

    @Column( nullable = false, precision = 10, scale = 2 )
    private BigDecimal amount;

    @Enumerated( EnumType.STRING)
    private LiveDead liveDead;

    @Enumerated( EnumType.STRING)
    private DiscountRatioStatus discountRatioStatus;

    @OneToMany( mappedBy = "discountRatio" )
    private List< Invoice > invoices;

}

