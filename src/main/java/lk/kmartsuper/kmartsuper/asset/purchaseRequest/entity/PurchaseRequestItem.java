package lk.kmartsuper.kmartsuper.asset.purchaseRequest.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.kmartsuper.kmartsuper.asset.employee.entity.Employee;
import lk.kmartsuper.kmartsuper.asset.item.entity.Item;
import lk.kmartsuper.kmartsuper.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "PurchaseRequestItem" )
public class PurchaseRequestItem extends AuditEntity {

    private Integer quantityRequested;

    @ManyToOne
    private PurchaseRequest purchaseRequest;

    @ManyToOne
    private Item item;
}
