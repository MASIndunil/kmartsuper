package lk.kmartsuper.asset.PurchaseOrder.dao;

import lk.kmartsuper.asset.PurchaseOrder.entity.Enum.PurchaseOrderStatus;
import lk.kmartsuper.asset.PurchaseOrder.entity.PurchaseOrder;
import lk.kmartsuper.asset.supplier.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderDao extends JpaRepository< PurchaseOrder, Integer> {

    List<PurchaseOrder> findByPurchaseOrderStatus(PurchaseOrderStatus purchaseOrderStatus);

    List<PurchaseOrder> findByPurchaseOrderStatusAndSupplier(PurchaseOrderStatus purchaseOrderStatus, Supplier supplier);


    PurchaseOrder findFirstByOrderByIdDesc();
}
