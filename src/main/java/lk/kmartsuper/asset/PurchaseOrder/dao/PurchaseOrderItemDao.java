package lk.kmartsuper.asset.PurchaseOrder.dao;

import lk.kmartsuper.asset.PurchaseOrder.entity.PurchaseOrder;
import lk.kmartsuper.asset.PurchaseOrder.entity.PurchaseOrderItem;
import lk.kmartsuper.asset.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderItemDao extends JpaRepository< PurchaseOrderItem, Integer> {
    PurchaseOrderItem findByPurchaseOrderAndItem(PurchaseOrder purchaseOrder, Item item);
    List<PurchaseOrderItem> findByPurchaseOrder(PurchaseOrder purchaseOrder);
}
