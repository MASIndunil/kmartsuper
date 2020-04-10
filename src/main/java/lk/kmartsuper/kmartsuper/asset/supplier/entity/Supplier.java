package lk.kmartsuper.kmartsuper.asset.supplier.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.kmartsuper.kmartsuper.asset.commonAsset.model.Enum.Gender;
import lk.kmartsuper.kmartsuper.asset.commonAsset.model.Enum.Title;
import lk.kmartsuper.kmartsuper.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("Supplier")
public class Supplier extends AuditEntity {

    @Size(min = 5, message = "Your Company name cannot be accepted")
    private String name;

    @Column(unique = true)
    private String code;

    @Size(min = 2, message = "Your BRN cannot be accepted")
    private String brn;

    @Size(max = 10, min = 9, message = "Mobile number length should be contained 10 and 9")
    private String contactOne;

    private String contactTwo;

    @Column(unique = true)
    private String email;

    @Column(columnDefinition = "VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin NULL", length = 255)
    private String address;

}
