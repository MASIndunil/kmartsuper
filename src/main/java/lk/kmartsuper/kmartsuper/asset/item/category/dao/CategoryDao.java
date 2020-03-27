package lk.kmartsuper.kmartsuper.asset.item.category.dao;

import lk.kmartsuper.kmartsuper.asset.item.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryDao extends JpaRepository<Category, Integer> {
}
