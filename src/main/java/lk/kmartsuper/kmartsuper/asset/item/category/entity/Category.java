package lk.kmartsuper.kmartsuper.asset.item.category.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.kmartsuper.kmartsuper.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "Category" )
public class Category extends AuditEntity {

    @Size( min = 5, message = "Your name cannot be accepted" )
    private String name;

}
