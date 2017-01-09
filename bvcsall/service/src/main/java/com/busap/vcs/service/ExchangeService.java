package com.busap.vcs.service;

import com.busap.vcs.data.entity.Exchange;

import java.util.List;

public interface ExchangeService {

    int deleteByPrimaryKey(Long id);

    int insert(Exchange record);

    Exchange selectByPrimaryKey(Long id);

    List<Exchange> selectAll(Exchange exchange);

    int updateByPrimaryKey(Exchange record);

}