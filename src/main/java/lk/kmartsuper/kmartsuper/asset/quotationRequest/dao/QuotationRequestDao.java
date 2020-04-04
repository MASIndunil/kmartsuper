package lk.kmartsuper.kmartsuper.asset.quotationRequest.dao;

import lk.kmartsuper.kmartsuper.asset.purchaseRequest.entity.PurchaseRequest;
import lk.kmartsuper.kmartsuper.asset.quotationRequest.entity.QuotationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotationRequestDao extends JpaRepository<QuotationRequest, Integer> {
}
