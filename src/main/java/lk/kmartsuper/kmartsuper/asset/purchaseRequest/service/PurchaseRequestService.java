package lk.kmartsuper.kmartsuper.asset.purchaseRequest.service;

import lk.kmartsuper.kmartsuper.asset.purchaseRequest.dao.PurchaseRequestDao;
import lk.kmartsuper.kmartsuper.asset.purchaseRequest.entity.PurchaseRequest;
import lk.kmartsuper.kmartsuper.util.interfaces.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig( cacheNames = "purchaseRequest" )
public class PurchaseRequestService implements AbstractService<PurchaseRequest, Integer>{
    private final PurchaseRequestDao purchaseRequestDao;

    @Autowired
    public PurchaseRequestService(PurchaseRequestDao purchaseRequestDao) {
        this.purchaseRequestDao = purchaseRequestDao;
    }

    public List<PurchaseRequest> findAll() {
        return purchaseRequestDao.findAll();
    }

    public PurchaseRequest findById(Integer id) {
        return purchaseRequestDao.getOne(id);
    }

    public PurchaseRequest persist(PurchaseRequest purchaseRequest) {
        return purchaseRequestDao.save(purchaseRequest);
    }

    public boolean delete(Integer id) {
        purchaseRequestDao.deleteById(id);
        return false;
    }

    public List<PurchaseRequest> search(PurchaseRequest purchaseRequest) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<PurchaseRequest> purchaseRequestExample = Example.of(purchaseRequest, matcher);
        return purchaseRequestDao.findAll(purchaseRequestExample);
    }
}
