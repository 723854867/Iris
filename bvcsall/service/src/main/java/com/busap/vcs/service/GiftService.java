package com.busap.vcs.service;

import java.util.List;
import java.util.Map;

import com.busap.vcs.data.entity.Gift;
import com.busap.vcs.data.model.GiftDisplay;

public interface GiftService extends BaseService<Gift, Long>{
    int deleteByPrimaryKey(Long id);

    int insert(Gift record);

    Gift selectByPrimaryKey(Long id);

    List<Gift> selectAll(GiftDisplay giftDisplay);

    int updateByPrimaryKey(Gift record);
    /**
     * 用户给主播送礼物
     * @param id
     * @param number
     * @param senderId
     * @param recieverId
     * @param roomId
     * @return 返回，状态码：0 赠送成功、-1 余额不足、-2 物品已下架、-9 其他错误，余额，免费礼物余额
     */
    Map<String,Object> sendGift(Long id,Integer number,Long senderId,Long recieverId,Long roomId,String appVersion,String platform,String channel);
    /**
     * 计算主播获取金豆总数
     * @param receiverId
     * @return
     */
    Long excuteAnchorTotalPoints(Long receiverId);

    List<Gift> selectByIds(List<Long> ids);

    public List<Gift> findExclusiveGifts();

    List<Gift> findExclusiveGiftsByState(Integer state);
    public List<Map<String,Object>> reCount(Long liveActivityId,List<Long> idList);
}
