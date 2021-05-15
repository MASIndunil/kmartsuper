package lk.kmart_super.asset.discount_ratio.service;


import lk.kmart_super.asset.common_asset.model.enums.LiveDead;
import lk.kmart_super.asset.discount_ratio.dao.DiscountRatioDao;
import lk.kmart_super.asset.discount_ratio.entity.DiscountRatio;
import lk.kmart_super.asset.discount_ratio.entity.enums.DiscountRatioStatus;
import lk.kmart_super.util.interfaces.AbstractService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiscountRatioService implements AbstractService< DiscountRatio, Integer > {
private final DiscountRatioDao discountRatioDao;

    public DiscountRatioService(DiscountRatioDao discountRatioDao) {
        this.discountRatioDao = discountRatioDao;
    }

    public List< DiscountRatio > findAll() {
        return discountRatioDao.findAll().stream()
            .filter(x -> LiveDead.ACTIVE.equals(x.getLiveDead()))
            .collect(Collectors.toList());
    }

    public List< DiscountRatio > findAllActive() {
        return discountRatioDao.findAll().stream()
                .filter(x -> LiveDead.ACTIVE.equals(x.getLiveDead()))
                .filter(x -> DiscountRatioStatus.ACTIVE.equals(x.getDiscountRatioStatus()))
                .collect(Collectors.toList());
    }

    public DiscountRatio findById(Integer id) {
        return discountRatioDao.getOne(id);
    }

    public DiscountRatio persist(DiscountRatio discountRatio) {
        if ( discountRatio.getId() == null ){
            discountRatio.setLiveDead(LiveDead.ACTIVE);
            discountRatio.setDiscountRatioStatus(DiscountRatioStatus.ACTIVE);
        }
        return discountRatioDao.save(discountRatio);
    }

    public boolean delete(Integer id) {
        DiscountRatio discountRatio =  discountRatioDao.getOne(id);
        discountRatio.setLiveDead(LiveDead.STOP);
        discountRatioDao.save(discountRatio);
        return false;
    }

    public List< DiscountRatio > search(DiscountRatio discountRatio) {
        return null;
    }
}
