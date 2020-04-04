package lk.kmartsuper.kmartsuper.asset.quotationRequest.service;

import lk.kmartsuper.kmartsuper.asset.quotationRequest.dao.QuotationRequestDao;
import lk.kmartsuper.kmartsuper.asset.quotationRequest.entity.QuotationRequest;
import lk.kmartsuper.kmartsuper.util.interfaces.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig( cacheNames = "quotationRequest" )
public class QuotationRequestService implements AbstractService<QuotationRequest, Integer>{
    private final QuotationRequestDao quotationRequestDao;

    @Autowired
    public QuotationRequestService(QuotationRequestDao quotationRequestDao) {
        this.quotationRequestDao = quotationRequestDao;
    }

    public List<QuotationRequest> findAll() {
        return quotationRequestDao.findAll();
    }

    public QuotationRequest findById(Integer id) {
        return quotationRequestDao.getOne(id);
    }

    public QuotationRequest persist(QuotationRequest quotationRequest) {
        return quotationRequestDao.save(quotationRequest);
    }

    public boolean delete(Integer id) {
        quotationRequestDao.deleteById(id);
        return false;
    }

    public List<QuotationRequest> search(QuotationRequest quotationRequest) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<QuotationRequest> quotationRequestExample = Example.of(quotationRequest, matcher);
        return quotationRequestDao.findAll(quotationRequestExample);
    }
}
