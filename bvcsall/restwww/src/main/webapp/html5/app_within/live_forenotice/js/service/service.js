define(['../libs/jquery-2.1.4', '../common/ajax'], function ($, Ajax) {
    return {
        getActive : function(count,actId,page){
            return Ajax.once().postJSON('/restwww/video/findActVideos',{
                count: count,
                actId: actId,
                page: page
            });
        },
        getZan:function(p){
        	return Ajax.once().postJSON('/restwww/praise/savePraise',{
        		p:p
        	})
        },
        getAtten:function(attentionId,dataFrom,isAttention){
            return Ajax.once().postJSON('/restwww/attention/addAttention',{
                attentionId: attentionId,
                dataFrom: dataFrom,
                isAttention: isAttention
            });
        },
        //获取视频评论列表
        getEvaluation:function(page,size,videoId){
            return Ajax.once().postJSON('/restwww/evaluation/evaluationList',{
                page: page,
                size: size,
                videoId: videoId
            });
        },
        //获取相关最热视频
        getHotVideo:function(videoId,page,size){
            return Ajax.once().postJSON('/restwww/video/findRelatedVideos',{
                videoId: videoId,
                page: page,
                size: size
            });
        },

        //点赞
        savePraise:function(videoId){
            return Ajax.once().postJSONWithBeforeSendPraise('/restwww/praise/savePraise',{
            	videoId: videoId
            });
        },

        //取消赞
        deletePraise:function(videoId){
            return Ajax.once().postJSONWithBeforeSendPraise('/restwww/praise/deletePraise',{
            	videoId: videoId
            });
        },
        addMore:function(timestamp,count,userid){
        	return Ajax.once().postJSONWithBeforeSend('/restwww/video/findUserVideos',{
        		timestamp: timestamp,
        		count:count,
        		userid:userid
            });
        },
        getActList: function(groupType){ //活动列表
            return Ajax.once().postJSONWithBeforeSend('/restwww/activity/findAllByGroupType',{
                groupType: groupType
            });
        },
        addAttention: function(attentionId,dataFrom,isAttention){ //关注
            return Ajax.once().postJSONWithBeforeSendNew('/restwww/attention/payAttention',{
                attentionId: attentionId,
                dataFrom: dataFrom,
                isAttention: isAttention

            });
        },
        allAttention: function(attentionIds,dataFrom){ //关注
            return Ajax.once().postJSONWithBeforeSend('/restwww/attention/multiPayAttention',{
            	attentionIds: attentionIds,
                dataFrom: dataFrom
            });
        },
        getVidEoeva: function(flag,startId,videoId,count){ //评论列表
            return Ajax.once().postJSONWithBeforeSend('/restwww/evaluation/findEvaluationList',{
                flag: flag,
                startId: startId,
                videoId: videoId,
                count: count

            });
        },
        saveEvaluation: function(videoId,creator,content){ //发表评论
            return Ajax.once().postJSONWithBeforeSend('/restwww/evaluation/saveEvaluation',{
                videoId: videoId,
                creator: creator,
                content: content
            });
        },
        getActVideoList: function(page,count,actId){
            return Ajax.once().postJSONWithBeforeSend('/restwww/video/findActVideos',{
                page: page,
                count: count,
                actId: actId
            });
        },
        getVideoPraiseList:function(vid,minPraiseId,count){//视频点赞列表
        	return Ajax.once().postJSONWithBeforeSend('/restwww/video/praiseUserList',{
        		vid:vid,
        		minPraiseId:minPraiseId,
        		count:count
        	})
        },
        shareWopaiap: function(videoId,authorId,evaluation,operate,dataFrom){ //转发到我拍
            return Ajax.once().postJSONWithBeforeSend('/restwww/forward/addForward',{
                videoId: videoId,
                authorId: authorId,
                evaluation: evaluation,
                operate: operate,
                dataFrom: dataFrom
            });
        },
        logOut:function(data_from){
        	return Ajax.once().postJSONWithBeforeSend('/restwww/logout',{
        		data_from:data_from
        	})
        },
        playNumber:function(videoId,playNumber){
            return Ajax.once().postJSONWithBeforeSend('/restwww/video/incCount',{
                id: videoId,
                c: playNumber
            })
        },
        getAttentionVideoList:function(timestamp,count){
            return Ajax.once().postJSONWithBeforeSendAtten('/restwww/attention/getAttentionVideoList',{
            	timestamp: timestamp,
            	count: count
            })
        },
        getPraiseList:function(vid,minPraiseId,count){ //点赞用户列表
            return Ajax.once().postJSONWithBeforeSend('/restwww/video/praiseUserList',{
                vid: vid,
                minPraiseId: minPraiseId,
                count: count
            })
        },
        getAttentionList:function(lastId,count){//获取关注列表
        	return Ajax.once().postJSONWithBeforeSend('/restwww/attention/getAttentionListNew',{
        		lastId:lastId,
        		count:count
        	})
        },
        getFansList:function(timestamp,count){//粉丝列表
        	return Ajax.once().postJSONWithBeforeSend('/restwww/attention/getFansList',{
        		timestamp:timestamp,
        		count:count
        	})
        },
        getActiveTagList:function(tag,row,lastid){//活动标签
            return Ajax.once().postJSONWithBeforeSend('/restwww/video/findKeywordVideoV2',{
                tag: tag,
                row: row,
                lastid: lastid
            })
        },
        getActiveNewList:function(count,actId,page){//最新视频
            return Ajax.once().postJSONWithBeforeSend('/restwww/video/findActVideos',{
                count: count,
                actId: actId,
                page: page
            })
        },
        getActiveHotList:function(startIndex,count,activityId){//最热视频
            return Ajax.once().postJSONWithBeforeSend('/restwww/video/findHotIndiceVideos',{
                startIndex: startIndex,
                count: count,
                activityId: activityId
            })
        },
        getRankVideo:function(type,start,count){//视频排行榜
            return Ajax.once().postJSONWithBeforeSendHit('/restwww/rank/findHotVideoRankingList',{
            	type:type,
                start: start,
                count: count
            })
        },
        getRankPopul:function(start,count){//人气排行榜
            return Ajax.once().postJSONWithBeforeSendHit('/restwww/rank/findDayUserPopularityRank',{
                start: start,
                count: count
            })
        },
        queryRankPopul:function(){//查询视频排行榜
            return Ajax.once().postJSONWithBeforeSend('/restwww/rank/findDayUserPopularityRank',{

            })
        },
        queryRankPopul:function(){//查询人气排行榜
            return Ajax.once().postJSONWithBeforeSend('/restwww/rank/findDayUserPopularityRank',{

            })
        },
        getLvList:function(prizeId,prizeLevel,page,size){//获取获奖用户列表
            return Ajax.quiet().postJSON('/restwww/activity/getPrizeDetail',{
                prizeId: prizeId,
                prizeLevel: prizeLevel,
                page: page,
                size: size
            })
        },
        getFeedback:function(content,contact,dataFrom){//用户反馈
            return Ajax.quiet().postJSONWithBeforeSend('/restwww/feedback/addFeedback',{
            	content: content,
            	contact: contact,
            	dataFrom: dataFrom
            })
        },
        getVideoTag:function(tag){//用户反馈
            return Ajax.quiet().postJSON('/restwww/feedback/addFeedback',{
                tag:tag
            })
        },
        CustomShare:function(shareType){//自定义分享接口
            return Ajax.quiet().postJSON('/restwww/shareManage/findByType',{
                shareType:shareType
            })
        },
        getRoomList:function(page,size,isLive,userId){
        	return Ajax.quiet().postJSON('/restwww/live/getRoomList',{
        		page:page,
        		size:size,
        		isLive:isLive,
        		userId:userId
        	})
        },
        isAttention:function(otherUid){
        	return Ajax.quiet().postJSONWithBeforeSend('/restwww/attention/isAttention',{
        		otherUid:otherUid
        	})
        }

    };
});


