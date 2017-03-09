package org.Iris.app.jilu.service.action.merchant.serial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.storage.domain.MemOrderGoods;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;
import org.Iris.util.common.SerializeUtil;

/**
 * 分包
 * @author 樊水东
 * 2017年1月3日
 */
public class ORDER_PACKET extends SerialMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		/**
		 * packetGoodsList的结构[{“id”:1,”count”:3},{“id”:2,”count”:3}];[{“id”:1,”count”:3},{“id”:2,”count”:3}]
		 */
		String packetGoodsList = session.getKVParam(JiLuParams.PACKETGOODSLIST);
		String orderId = session.getKVParam(JiLuParams.ORDERID);
		Merchant merchant = session.getMerchant();
		
		/**对传递过来的打包参数 进行解析判断 start*/
		String[] strings = packetGoodsList.split(";");
		if(strings.length<1)
			throw IllegalConstException.errorException(JiLuParams.PACKETGOODSLIST);
		Map<Long, Integer> goodsCount = new HashMap<Long, Integer>();
		List<List<MemOrderGoods>> packets = new ArrayList<List<MemOrderGoods>>();
		for(String packet:strings){
			List<MemOrderGoods>	list = new ArrayList<MemOrderGoods>(Arrays.asList(SerializeUtil.JsonUtil.GSON.fromJson(packet, MemOrderGoods[].class)));
			if(null==list || list.size()==0)
				throw IllegalConstException.errorException(JiLuParams.PACKETGOODSLIST);
			for(MemOrderGoods mGoods : list){
				long goodId = mGoods.getId();
				if(!goodsCount.containsKey(goodId)){
					goodsCount.put(goodId, mGoods.getCount());
				}else{
					goodsCount.put(goodId, goodsCount.get(goodId)+mGoods.getCount());
				}
			}
			packets.add(list);
		}
		for(Entry<Long, Integer> entry:goodsCount.entrySet()){
			MemOrderGoods mog = memOrderGoodsMapper.getMerchantOrderGoodsById(entry.getKey());
			if(mog==null)
				throw IllegalConstException.errorException(JiLuParams.PACKETGOODSLIST);
			if(mog.getCount()!=entry.getValue())
				return Result.jsonError(JiLuCode.PACKET_COUNT_ERROR);
		}
		/**对传递过来的打包参数 进行解析判断 end*/
		
		return merchantService.orderPacket(orderId, packetGoodsList,merchant);
	}
	
}
