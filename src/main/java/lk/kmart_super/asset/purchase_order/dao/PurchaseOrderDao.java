package lk.kmart_super.asset.purchase_order.dao;


import lk.kmart_super.asset.purchase_order.entity.PurchaseOrder;
import lk.kmart_super.asset.purchase_order.entity.enums.PurchaseOrderStatus;
import lk.kmart_super.asset.supplier.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PurchaseOrderDao extends JpaRepository< PurchaseOrder, Integer> {

    List<PurchaseOrder> findByPurchaseOrderStatus(PurchaseOrderStatus purchaseOrderStatus);

    List<PurchaseOrder> findByPurchaseOrderStatusAndSupplier(PurchaseOrderStatus purchaseOrderStatus, Supplier supplier);


    PurchaseOrder findFirstByOrderByIdDesc();

  List< PurchaseOrder> findByCreatedAtIsBetween(LocalDateTime form, LocalDateTime to);

  List< PurchaseOrder> findByUpdatedAtIsBetweenAndPurchaseOrderStatus(LocalDateTime form, LocalDateTime to, PurchaseOrderStatus purchaseOrderStatus);
}
