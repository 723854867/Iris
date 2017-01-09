package com.busap.vcs.data.mapper;

import com.busap.vcs.data.entity.Exchange;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("exchangeDao")
public interface ExchangeDao {

    int deleteByPrimaryKey(Long id);

    int insert(Exchange record);

    Exchange selectByPrimaryKey(Long id);

    List<Exchange> selectAll(Exchange exchange);

    int updateByPrimaryKey(Exchange record);

}