package lk.kmartsuper.kmartsuper.asset.purchaseOrder.dao;

import lk.kmartsuper.kmartsuper.asset.purchaseOrder.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderDao extends JpaRepository<PurchaseOrder, Integer> {
}
