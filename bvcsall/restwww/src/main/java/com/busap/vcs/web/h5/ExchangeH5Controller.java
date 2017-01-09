package com.busap.vcs.web.h5;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.data.entity.Anchor;
import com.busap.vcs.data.entity.Exchange;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.service.AnchorService;
import com.busap.vcs.service.ExchangeService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.RespBodyBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 功能：兑换/提现
 * Created by huoshanwei on 2016/4/7.
 */
@Controller
@RequestMapping("exchange")
public class ExchangeH5Controller extends BaseH5Controller {

    @Autowired
    protected HttpServletRequest request;

    @Resource
    private ExchangeService exchangeService;

    @Resource(name = "respBodyBuilder")
    private RespBodyBuilder respBodyWriter = new RespBodyBuilder();

    @Resource
    private AnchorService anchorService;

    @Resource
    private JedisService jedisService;

    @Resource(name="ruserService")
    private RuserService ruserService;

    private Logger logger = LoggerFactory.getLogger(ExchangeH5Controller.class);
    /**
     * 功能：根据type获取兑换/提现列表信息
     *
     * @param type   1兑换 2提现
     * @param openId openId
     * @return Exchange
     */
    @ResponseBody
    @RequestMapping("getExchangeListByType")
    public RespBody getExchangeListByType(String openId, Integer type) {
        if (StringUtils.isBlank(openId)) {
            return respBodyWriter.toError(ResponseCode.CODE_312.toString(), ResponseCode.CODE_312.toCode());
        }
        Exchange exchangeParams = new Exchange();
        exchangeParams.setType(type);
        exchangeParams.setState(1);
        Set<String> exchangeIds = null;
        List<Map<String,String>> exchangeList = new ArrayList<Map<String,String>>();
        //获取兑换/提现列表信息
        if (type == 1) {
            //获取兑换列表
            exchangeIds = jedisService.getSortedSetFromShardByDesc(BicycleConstants.EXCHANGE_ID);
            for (String exchangeId:exchangeIds) {
                Map<String, String> exchange = jedisService.getMapByKey(BicycleConstants.EXCHANGE + exchangeId);
                if(exchange != null && exchange.get("state") != null && "1".equals(exchange.get("state"))) {
                    exchangeList.add(exchange);
                }
            }
        } else {
            //获取提现列表
            exchangeIds = jedisService.getSortedSetFromShardByDesc(BicycleConstants.WITHDRAW_CASH_ID);
            for (String exchangeId:exchangeIds) {
                Map<String, String> exchange = jedisService.getMapByKey(BicycleConstants.WITHDRAW_CASH + exchangeId);
                if(exchange != null && exchange.get("state") != null && "1".equals(exchange.get("state"))) {
                    exchangeList.add(exchange);
                }
            }
        }
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("openId", openId);
        List<Ruser> userList = ruserService.selectRusers(params);
        if (userList.size() == 1) {
            Ruser ruser = userList.get(0);
            Anchor anchor = anchorService.getAnchorByUserid(ruser.getId());
            String pointCount = "0";
            if(anchor != null){
                pointCount = String.valueOf(anchor.getPointCount());
            }
            return respBodyWriter.toSuccess(ResponseCode.CODE_200.toString(), exchangeList, pointCount);
        } else {
            logger.info("GetExchangeListByType=====> user not exist!");
            return respBodyWriter.toError(ResponseCode.CODE_451.toString(), ResponseCode.CODE_451.toCode());
        }
    }

    /**
     * 功能：根据type获取兑换/提现列表信息
     *
     * @param type   1兑换 2提现
     * @return Exchange
     */
    @ResponseBody
    @RequestMapping("getExchangeListByUidType")
    public RespBody getExchangeListByUidType(Integer type) {
        String uid = request.getHeader("uid");
        if (StringUtils.isBlank(uid)) {
            return respBodyWriter.toError(ResponseCode.CODE_312.toString(), ResponseCode.CODE_312.toCode());
        }
        Exchange exchangeParams = new Exchange();
        exchangeParams.setType(type);
        exchangeParams.setState(1);
        Set<String> exchangeIds = null;
        List<Map<String,String>> exchangeList = new ArrayList<Map<String,String>>();
        //获取兑换/提现列表信息
        if (type == 1) {
            //获取兑换列表
            exchangeIds = jedisService.getSortedSetFromShardByDesc(BicycleConstants.EXCHANGE_ID);
            for (String exchangeId:exchangeIds) {
                Map<String, String> exchange = jedisService.getMapByKey(BicycleConstants.EXCHANGE + exchangeId);
                if(exchange != null && exchange.get("state") != null && "1".equals(exchange.get("state"))) {
                    exchangeList.add(exchange);
                }
            }
        } else {
            //获取提现列表
            exchangeIds = jedisService.getSortedSetFromShardByDesc(BicycleConstants.WITHDRAW_CASH_ID);
            for (String exchangeId:exchangeIds) {
                Map<String, String> exchange = jedisService.getMapByKey(BicycleConstants.WITHDRAW_CASH + exchangeId);
                if(exchange != null && exchange.get("state") != null && "1".equals(exchange.get("state"))) {
                    exchangeList.add(exchange);
                }
            }
        }
        Ruser ruser = ruserService.find(Long.parseLong(uid));
        if (ruser == null) {
            return respBodyWriter.toError(ResponseCode.CODE_305.toString(), ResponseCode.CODE_305.toCode());
        }
        Anchor anchor = anchorService.getAnchorByUserid(ruser.getId());
        String pointCount = "0";
        if(anchor != null){
            pointCount = String.valueOf(anchor.getPointCount());
        }
        return respBodyWriter.toSuccess(ResponseCode.CODE_200.toString(), exchangeList, pointCount);
    }
}
