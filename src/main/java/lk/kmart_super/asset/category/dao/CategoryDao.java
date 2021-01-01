package lk.kmart_super.asset.category.dao;

import lk.kmart_super.asset.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryDao extends JpaRepository<Category, Integer> {
}
