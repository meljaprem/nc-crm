package com.netcracker.crm.service.entity.impl;

import com.netcracker.crm.dao.DiscountDao;
import com.netcracker.crm.domain.model.Discount;
import com.netcracker.crm.domain.real.RealDiscount;
import com.netcracker.crm.domain.request.DiscountRowRequest;
import com.netcracker.crm.dto.AutocompleteDto;
import com.netcracker.crm.dto.DiscountDto;
import com.netcracker.crm.dto.bulk.DiscountBulkDto;
import com.netcracker.crm.dto.mapper.ModelMapper;
import com.netcracker.crm.dto.mapper.impl.DiscountMapper;
import com.netcracker.crm.dto.row.DiscountRowDto;
import com.netcracker.crm.service.entity.DiscountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by Pasha on 01.05.2017.
 */
@Service
public class DiscountServiceImpl implements DiscountService {
    private static final Logger log = LoggerFactory.getLogger(DiscountServiceImpl.class);

    private final DiscountDao discountDao;
    private final DiscountMapper discountMapper;

    @Autowired
    public DiscountServiceImpl(DiscountDao discountDao, DiscountMapper discountMapper) {
        this.discountDao = discountDao;
        this.discountMapper = discountMapper;
    }

    @Override
    @Transactional
    public Discount create(DiscountDto discountDto) {
        Discount discount = ModelMapper.map(discountMapper.dtoToModel(), discountDto, RealDiscount.class);
        discountDao.create(discount);
        return discount;
    }

    @Override
    @Transactional
    public boolean update(DiscountDto discountDto) {
        Discount discount = ModelMapper.map(discountMapper.dtoToModel(), discountDto, RealDiscount.class);
        Long updateId = discountDao.update(discount);
        return updateId > 0;
    }
    
    @Override
    @Transactional
    public boolean update(Discount discount) {        
        Long updateId = discountDao.update(discount);
        return updateId > 0;
    }

    @Override
    public Discount getDiscountById(Long id) {
        return discountDao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AutocompleteDto> getAutocompleteDto(String pattern) {
        List<Discount> discounts = discountDao.findByIdOrTitle(pattern);
        return ModelMapper.mapList(discountMapper.modelToAutocomplete(), discounts, AutocompleteDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDiscountRows(DiscountRowRequest rowRequest) {
        Map<String, Object> response = new HashMap<>();
        Long length = discountDao.getDiscountRowsCount(rowRequest);
        response.put("length", length);

        List<Discount> discounts = discountDao.findDiscounts(rowRequest);
        List<DiscountRowDto> dtoRows = ModelMapper.mapList(discountMapper.modelToRowDto(), discounts, DiscountRowDto.class);
        response.put("rows", dtoRows);
        return response;
    }

    @Override
    @Transactional
    public boolean bulkUpdate(DiscountBulkDto bulkDto) {
        RealDiscount discountTemplate = getBulkDiscount(bulkDto);
        Set<Long> discountIDs = new HashSet<>();
        if (bulkDto.getItemIds() != null) discountIDs.addAll(bulkDto.getItemIds());

        return discountDao.bulkUpdate(discountIDs, discountTemplate);
    }

    private RealDiscount getBulkDiscount(DiscountBulkDto bulkDto) {
        RealDiscount discountTemplate = new RealDiscount();
        if (bulkDto.isDescriptionChanged()) discountTemplate.setDescription(bulkDto.getDescription());
        if (bulkDto.isActiveChanged()) {
            boolean isActive = bulkDto.isActive() == null ? false : bulkDto.isActive();
            discountTemplate.setActive(isActive);
        }
        if (bulkDto.isPercentageChanged()) discountTemplate.setPercentage(bulkDto.getPercentage());

        return discountTemplate;
    }
}
