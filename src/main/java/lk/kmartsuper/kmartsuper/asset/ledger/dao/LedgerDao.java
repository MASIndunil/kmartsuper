package lk.kmartsuper.kmartsuper.asset.ledger.dao;

import lk.kmartsuper.kmartsuper.asset.ledger.entity.Ledger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LedgerDao extends JpaRepository<Ledger, Integer> {
}
