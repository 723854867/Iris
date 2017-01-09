package com.busap.vcs.dao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.busap.vcs.data.entity.Evaluation;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.busap.vcs.data.entity.Video;

@Repository
public class VideoCheckDao extends JdbcDaoSupport {
	private static String INSERT_SQL = "insert into video_check (uid,videoid,publishdate) values (?,?,?)";
	private static String INSERT_PRAISE_SQL = "insert into praise(creator_id,video_id,data_from,create_at) values(?,?,?,?)";
	private static String UPDATE_VIDEO_PLAY_COUNT = "update video set praise_count=praise_count+?, play_count=play_count+?, play_count_today=play_count_today+? where id = ?";
	public int insert(long uid,long vid,long time){
		return this.getJdbcTemplate().update(INSERT_SQL, uid,vid,time);
	}
	public int insertPraise(long uid,long videoId,long time){
		return this.getJdbcTemplate().update(INSERT_PRAISE_SQL, uid,videoId,"myvideo_restwww",new Timestamp(time));
	}
	public int updateVideoPraiseCount(int count,long videoId){
		return this.getJdbcTemplate().update(UPDATE_VIDEO_PLAY_COUNT, count, count, count, videoId);
	}
	
	public List<String> findMajiaIds(){
		String sql="select id from ruser where type ='majia'";
		return (List<String>)this.getJdbcTemplate().queryForList(sql,String.class);
	}
	public boolean isAttentUser(long uid,long majiaId){
		String sql="select * from attention where creator_id =" + majiaId + " and attention_id = " + uid;
		return this.getJdbcTemplate().queryForList(sql).size() > 0;
	}

	public boolean hasComment(long vid) {
		String sql = "select e.id from evaluation e where e.data_from='auto_comment' and e.video_id=" + vid + ";";
		return this.getJdbcTemplate().queryForList(sql).size() > 0;
	}

	public int todayAttentionCount(long uid) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(c.getTime());
		String sql = "select id from attention where attention_id=" + uid + " and create_date>'" + date + "';";
		return this.getJdbcTemplate().queryForList(sql).size();
	}
	public int insertAttention(long uid,long mid){
		String sql = "insert into attention (creator_id, attention_id, create_date, data_from,majia_attention) values(?,?,?,?,?)";
		return this.getJdbcTemplate().update(sql, mid,uid,new Date(),"",1);
	}
	
	public int updateUserAttentionNumber(long uid,long addNum){
		String sql = "update ruser set attention_count = attention_count+? where id = ?";
		return this.getJdbcTemplate().update(sql, addNum,uid);
	}
	public int updateUserFunsNumber(long uid,long addNum){
		String sql = "update ruser set fans_count = fans_count+? where id = ?";
		return this.getJdbcTemplate().update(sql, addNum,uid);
	}
	
	public boolean isPraise(long mid,long vid){
		String sql="select * from praise where creator_id =" + mid+" and video_id = "+vid;
		if(this.getJdbcTemplate().queryForList(sql).size()>0)return true;
		else return false;
		
	}
	public boolean isDelete(long vid){
		String sql="select * from video where flow_stat='delete' and id =" + vid;
		if(this.getJdbcTemplate().queryForList(sql).size()>0)return true;
		else return false;
		
	}

	public boolean checkPraiseCommentCount(long vid) {
		String sql = "select if(praise_count>evaluation_count, 1, 0) as bln from video where id=" + vid;
		List<Integer> result = this.getJdbcTemplate().queryForList(sql, Integer.class);
		return result.size() == 1 && result.get(0) != null && result.get(0) > 0;
	}

	public boolean checkPlayedPraiseCount(long vid) {
		String sql = "select if(play_count>praise_count, 1, 0) as bln from video where id=" + vid;
		List<Integer> result = this.getJdbcTemplate().queryForList(sql, Integer.class);
		return result.size() == 1 && result.get(0) != null && result.get(0) > 0;
	}

	/**
	 * 加视频评论数
	 */
	public int incEvaluationCount(Long videoId) {
		String sql = "update video a set a.evaluation_count=a.evaluation_count+1 where a.id=" + videoId;
		return this.getJdbcTemplate().update(sql);
	}

	public boolean allowEvaluation(Long videoId) {
		String sql = "select u.allow_evaluation from ruser u where u.id=(select v.creator_id from video v where v.id=" + videoId + ")";
		List<Integer> result = this.getJdbcTemplate().queryForList(sql, Integer.class);
		return result.size() == 1 && result.get(0) != null && result.get(0) == 1;
	}

	/**
	 * 保存评论
	 * @param majiaId 马甲id
	 * @param content 评论内容
	 * @param videoId 视频id
	 */
	public int saveEvaluation(long majiaId, String content, long videoId) {
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String INSERT_SQL = "insert into evaluation (create_at, modify_at, creator_id, content, video_id, data_from, status) " +
				"values ('" + time + "','" + time + "'," + majiaId + ",'" + content + "'," + videoId + ",'auto_comment', 0);";

		return this.getJdbcTemplate().update(INSERT_SQL);
	}


	public String getRandomComment() {
		String sql="select `comment` from auto_comment order by RAND() limit 1;";
		List<String> result = this.getJdbcTemplate().queryForList(sql, String.class);
		if (result.size() == 1) {
			return result.get(0);
		} else {
			return "";
		}

	}
}
