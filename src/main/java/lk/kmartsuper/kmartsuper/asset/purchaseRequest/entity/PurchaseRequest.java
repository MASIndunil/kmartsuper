package lk.kmartsuper.kmartsuper.asset.purchaseRequest.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.kmartsuper.kmartsuper.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "PurchaseRequest" )
public class PurchaseRequest extends AuditEntity {

    @DateTimeFormat( pattern = "yyyy-MM-dd" )
    private LocalDate prDate;

    @OneToMany(cascade = CascadeType.ALL)
    private List< PurchaseRequestItem > purchaseRequestItems;
}
