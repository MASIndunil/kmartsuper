package lk.kmartsuper.kmartsuper.asset.item.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.kmartsuper.kmartsuper.asset.item.category.entity.Category;
import lk.kmartsuper.kmartsuper.asset.ledger.entity.Ledger;
import lk.kmartsuper.kmartsuper.asset.purchaseOrder.entity.PurchaseOrderItem;
import lk.kmartsuper.kmartsuper.asset.supplier.entity.Supplier;
import lk.kmartsuper.kmartsuper.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("Item")
public class Item extends AuditEntity {

    @Size(min = 5, message = "Your name cannot be accepted")
    private String name;

    private Integer rop;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

    @OneToMany(mappedBy = "item")
    private List<PurchaseOrderItem> purchaseOrderItems;

    @OneToMany(mappedBy = "item")
    private List<Ledger> ledgers;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "supplier_item",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "supplier_id"))
    private List<Supplier> suppliers;
}
