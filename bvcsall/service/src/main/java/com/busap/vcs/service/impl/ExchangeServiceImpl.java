package com.busap.vcs.service.impl;

import com.busap.vcs.data.entity.Exchange;
import com.busap.vcs.data.mapper.ExchangeDao;
import com.busap.vcs.service.ExchangeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by huoshanwei on 2016/4/6.
 */
@Service("exchangeService")
public class ExchangeServiceImpl implements ExchangeService{

    @Resource
    private ExchangeDao exchangeDao;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return exchangeDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Exchange record) {
        return exchangeDao.insert(record);
    }

    @Override
    public Exchange selectByPrimaryKey(Long id) {
        return exchangeDao.selectByPrimaryKey(id);
    }

    @Override
    public List<Exchange> selectAll(Exchange exchange) {
        return exchangeDao.selectAll(exchange);
    }

    @Override
    public int updateByPrimaryKey(Exchange record) {
        return exchangeDao.updateByPrimaryKey(record);
    }
}
