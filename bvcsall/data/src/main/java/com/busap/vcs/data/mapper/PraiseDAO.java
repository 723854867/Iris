package com.busap.vcs.data.mapper;

import java.util.List;

/**
 * 赞DAO
 * Created by Yangxinyu on 15/9/18.
 */
public interface PraiseDAO {

    /**
     * 根据ID删除
     * @param idList praiseID
     */
    public void deletePraise(List<Long> idList);
}
