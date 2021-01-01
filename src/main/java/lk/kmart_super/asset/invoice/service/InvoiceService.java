package lk.kmart_super.asset.invoice.service;

import lk.kmart_super.asset.common_asset.model.enums.LiveDead;
import lk.kmart_super.asset.invoice.dao.InvoiceDao;
import lk.kmart_super.asset.invoice.entity.Invoice;
import lk.kmart_super.util.interfaces.AbstractService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceService implements AbstractService< Invoice, Integer > {
    private final InvoiceDao invoiceDao;

    public InvoiceService(InvoiceDao invoiceDao) {
        this.invoiceDao = invoiceDao;
    }


    public List< Invoice > findAll() {
        return invoiceDao.findAll().stream()
            .filter(x -> LiveDead.ACTIVE.equals(x.getLiveDead()))
            .collect(Collectors.toList());
    }

    public Invoice findById(Integer id) {
        return invoiceDao.getOne(id);
    }

    public Invoice persist(Invoice invoice) {
        if(invoice.getId()==null){
            invoice.setLiveDead(LiveDead.ACTIVE);}
        return invoiceDao.save(invoice);
    }

    public boolean delete(Integer id) {
        Invoice invoice =  invoiceDao.getOne(id);
        invoice.setLiveDead(LiveDead.STOP);
        invoiceDao.save(invoice);
        return false;
    }
    public List< Invoice > search(Invoice invoice) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example< Invoice > invoiceExample = Example.of(invoice, matcher);
        return invoiceDao.findAll(invoiceExample);

    }

    public List< Invoice > findByCreatedAtIsBetween(LocalDateTime from, LocalDateTime to) {
        return invoiceDao.findByCreatedAtIsBetween(from, to);
    }

    public Invoice findByLastInvoice() {
        return invoiceDao.findFirstByOrderByIdDesc();
    }
}
