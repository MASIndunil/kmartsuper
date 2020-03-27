package lk.kmartsuper.kmartsuper.asset.item.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.kmartsuper.kmartsuper.asset.commonAsset.model.Enum.Title;
import lk.kmartsuper.kmartsuper.asset.item.entity.Enum.Category;
import lk.kmartsuper.kmartsuper.asset.item.entity.Enum.Status;
import lk.kmartsuper.kmartsuper.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "Item" )
public class Item extends AuditEntity {

    @Enumerated( EnumType.STRING )
    private Category category;

    @Enumerated( EnumType.STRING )
    private Status status;

    @Size( min = 5, message = "Your name cannot be accepted" )
    private String name;

    private BigDecimal price;

    @DateTimeFormat( pattern = "yyyy-MM-dd" )
    private LocalDate mDate;

    @DateTimeFormat( pattern = "yyyy-MM-dd" )
    private LocalDate eDate;

    private String batch;

    private Integer rop;

}
