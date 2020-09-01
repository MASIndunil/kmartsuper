package lk.kmartsuper.asset.goodReceivedNote.dao;

import lk.kmartsuper.asset.PurchaseOrder.entity.PurchaseOrder;
import lk.kmartsuper.asset.goodReceivedNote.entity.GoodReceivedNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodReceivedNoteDao extends JpaRepository< GoodReceivedNote, Integer> {
    GoodReceivedNote findByPurchaseOrder(PurchaseOrder purchaseOrder);
}
