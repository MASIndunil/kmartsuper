package lk.kmart_super.asset.category.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.kmart_super.asset.common_asset.model.enums.LiveDead;
import lk.kmart_super.asset.item.entity.Item;
import lk.kmart_super.asset.item.entity.enums.MainCategory;
import lk.kmart_super.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "Category" )
public class Category{

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Integer id;

    @Enumerated(EnumType.STRING)
    private MainCategory mainCategory;

    @Enumerated(EnumType.STRING)
    private LiveDead liveDead;

    @Size( min = 3, message = "Your name cannot be accepted" )
    private String name;

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Item> items;
}
