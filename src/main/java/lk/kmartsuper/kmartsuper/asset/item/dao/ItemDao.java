package lk.kmartsuper.kmartsuper.asset.item.dao;

import lk.kmartsuper.kmartsuper.asset.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemDao extends JpaRepository<Item, Integer> {
}
