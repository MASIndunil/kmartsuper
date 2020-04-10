package lk.kmartsuper.kmartsuper.asset.purchaseOrder.dao;

import lk.kmartsuper.kmartsuper.asset.purchaseOrder.entity.PurchaseOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderItemDao extends JpaRepository<PurchaseOrderItem, Integer> {
}
