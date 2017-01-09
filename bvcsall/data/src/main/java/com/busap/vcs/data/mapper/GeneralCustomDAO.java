package com.busap.vcs.data.mapper;


import org.apache.ibatis.annotations.Select;


/**
 *  查询最后一个插入的ID
 *  Created by yangxinyu on 15/10/16.
 */
public interface GeneralCustomDAO {

    @Select("SELECT LAST_INSERT_ID()")
    public Long findLastID();
}
