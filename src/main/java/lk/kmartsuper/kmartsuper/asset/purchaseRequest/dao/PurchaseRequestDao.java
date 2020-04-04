package lk.kmartsuper.kmartsuper.asset.purchaseRequest.dao;

import lk.kmartsuper.kmartsuper.asset.purchaseRequest.entity.PurchaseRequest;
import lk.kmartsuper.kmartsuper.asset.supplier.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRequestDao extends JpaRepository<PurchaseRequest, Integer> {
}
