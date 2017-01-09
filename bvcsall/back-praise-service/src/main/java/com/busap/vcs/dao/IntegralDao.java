package com.busap.vcs.dao;

import com.busap.vcs.data.entity.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Repository
public class IntegralDao extends JdbcDaoSupport {


	public List<Task> selectSpecialVideoTask() {
		String sql = "select t.id, t.description ,t.integral,t.jump_target_id as jumpTargetId, t.jump_type as jumpType, " +
				"t.num,t.previous_task_id as previousTaskId,t.task_value as taskValue,t.type_one as typeOne, " +
				"t.type_two as typeTwo,t.`status`,t.dead_line as deadLine, t.title " +
				"from task t where t.`status`=0 and t.type_two=1001;";
		return this.getJdbcTemplate().query(sql, new RowMapper<Task>() {
			public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
				Task vo = new Task();
				vo.setId(rs.getLong("id"));
				vo.setDescription(rs.getString("description"));
				vo.setIntegral(rs.getLong("integral"));
				vo.setJumpTargetId(rs.getString("jumpTargetId"));
				vo.setJumpType(rs.getString("jumpType"));
				vo.setPreviousTaskId(rs.getLong("previousTaskId"));
				vo.setTaskValue(rs.getString("taskValue"));
				vo.setTypeOne(rs.getString("typeOne"));
				vo.setTypeTwo(rs.getString("typeTwo"));
				vo.setStatus(Integer.parseInt(rs.getString("status")));
				vo.setDeadLine(rs.getTimestamp("deadLine"));
				vo.setTitle(rs.getString("title"));
				vo.setNum(rs.getString("num"));
				return vo;
			}
		});
	}

	public List<Task> selectFansTask() {
		String sql = "select t.id, t.description ,t.integral,t.jump_target_id as jumpTargetId, t.jump_type as jumpType, " +
				"t.num,t.previous_task_id as previousTaskId,t.task_value as taskValue,t.type_one as typeOne, " +
				"t.type_two as typeTwo,t.`status`,t.dead_line as deadLine, t.title " +
				"from task t where t.`status`=0 and t.type_two=2021;";
		return this.getJdbcTemplate().query(sql, new RowMapper<Task>() {
			public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
				Task vo = new Task();
				vo.setId(rs.getLong("id"));
				vo.setDescription(rs.getString("description"));
				vo.setIntegral(rs.getLong("integral"));
				vo.setJumpTargetId(rs.getString("jumpTargetId"));
				vo.setJumpType(rs.getString("jumpType"));
				vo.setPreviousTaskId(rs.getLong("previousTaskId"));
				vo.setTaskValue(rs.getString("taskValue"));
				vo.setTypeOne(rs.getString("typeOne"));
				vo.setTypeTwo(rs.getString("typeTwo"));
				vo.setStatus(Integer.parseInt(rs.getString("status")));
				vo.setDeadLine(rs.getTimestamp("deadLine"));
				vo.setTitle(rs.getString("title"));
				vo.setNum(rs.getString("num"));
				return vo;
			}
		});
	}

	public int getUserFansCount(Long userId) {
		String sql = "select fans_count from ruser where id=" + userId + ";";
		List<String> list = this.getJdbcTemplate().queryForList(sql, String.class);
		if (list != null && list.size() == 1 && StringUtils.isNumeric(list.get(0))) {
			return Integer.parseInt(list.get(0));
		}
		return 0;
	}

	public Integer selectIntegral(Long userId, Long taskId, boolean isToday) {
		String sql;
		if (isToday) {
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			sql = "select i.id from integral i where i.user_id=" + userId + " and i.source_id=" + taskId + " and i.create_date=" + date + ";";
		} else {
			sql = "select i.id from integral i where i.user_id=" + userId + " and i.source_id=" + taskId + ";";
		}

		List<String> list = this.getJdbcTemplate().queryForList(sql, String.class);
		if (list == null) {
			return 0;
		} else {
			return list.size();
		}
	}

	public int countVideoByActivityId(Long userId, Long activityId, boolean isDaily) {

		String sql;
		if (isDaily) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
			sql = "select a.id from activity_video a, video v where a.videoid=v.id and v.creator_id=" + userId + " and a.activityid=" + activityId + " and a.create_at >'" + time + "' ;";
		} else {
			sql = "select a.id from activity_video a, video v where a.videoid=v.id and v.creator_id=" + userId + " and a.activityid=" + activityId + ";";
		}
		List<String> list = this.getJdbcTemplate().queryForList(sql, String.class);
		if (list == null) {
			return 0;
		} else {
			return list.size();
		}
	}

	public List<String> findActivityIdsByVideoId(Long userId, Long videoId) {
		String sql = "select DISTINCT a.activityid from activity_video a, video v  where v.creator_id=" + userId
				+ " and a.videoid=" + videoId + " and v.id=a.videoid and v.flow_stat='check_ok';";
		return this.getJdbcTemplate().queryForList(sql, String.class);
	}

	public int insert(long userId, int type, int integral, long taskId, String title) {
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String INSERT_SQL = "insert into integral (create_time, modify_time,user_id,`type`,status,description, source_id,integral_num,create_date) " +
				"values ('" + time + "','" + time + "'," + userId + "," + type + ",0,'" + title + "', " + taskId + ", " + integral + ",'" + date + "');";

		return this.getJdbcTemplate().update(INSERT_SQL);
	}

	public List<Task> selectDailyVideoTask() {
		String sql = "select t.id, t.description ,t.integral,t.jump_target_id as jumpTargetId, t.jump_type as jumpType, " +
				"t.num,t.previous_task_id as previousTaskId,t.task_value as taskValue,t.type_one as typeOne, " +
				"t.type_two as typeTwo,t.`status`,t.dead_line as deadLine, t.title " +
				"from task t where t.`status`=0 and t.type_two=1 or t.type_two=41;";

		return this.getJdbcTemplate().query(sql, new RowMapper<Task>() {
			public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
				Task vo = new Task();
				vo.setId(rs.getLong("id"));
				vo.setDescription(rs.getString("description"));
				vo.setIntegral(rs.getLong("integral"));
				vo.setJumpTargetId(rs.getString("jumpTargetId"));
				vo.setJumpType(rs.getString("jumpType"));
				vo.setPreviousTaskId(rs.getLong("previousTaskId"));
				vo.setTaskValue(rs.getString("taskValue"));
				vo.setTypeOne(rs.getString("typeOne"));
				vo.setTypeTwo(rs.getString("typeTwo"));
				vo.setStatus(Integer.parseInt(rs.getString("status")));
				vo.setDeadLine(rs.getTimestamp("deadLine"));
				vo.setTitle(rs.getString("title"));
				vo.setNum(rs.getString("num"));
				return vo;
			}
		});
	}


	public Integer countUserVideoToday(Long userId) {
		String sql = "select id from video where creator_id=" + userId + " and  create_at > '" + getTodayStart() + "' and  create_at < '" + getTodayEnd() + "';";
		List<String> list = this.getJdbcTemplate().queryForList(sql, String.class);
		if (list == null) {
			return 0;
		} else {
			return list.size();
		}
	}

	public boolean checkTaskIsFinished(Long userId, Long taskId) {
		String sql = "select id from integral where user_id=" + userId + " and source_id=" + taskId + " and  create_time > '" + getTodayStart() + "' and  create_time < '" + getTodayEnd() + "';";
		List<String> list = this.getJdbcTemplate().queryForList(sql, String.class);
		if (list != null && list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	private String getTodayStart() {
		Calendar startDate = Calendar.getInstance();
		startDate.set(Calendar.HOUR_OF_DAY, 0);
		startDate.set(Calendar.MINUTE, 0);
		startDate.set(Calendar.SECOND, 0);
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startDate.getTime());
	}

	private String getTodayEnd() {
		Calendar endDate = Calendar.getInstance();
		endDate.set(Calendar.HOUR_OF_DAY, 23);
		endDate.set(Calendar.MINUTE, 59);
		endDate.set(Calendar.SECOND, 59);
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endDate.getTime());
	}

	public static void main(String[] args) {

		Long userId = 184l;
		Integer type = 1;
		Long taskId = 19l;
		String desc = "发布一个视频,赠送5积分";
		Integer integral = 5;
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String INSERT_SQL = "insert into integral (create_time, modify_time,user_id,`type`,status,description, source_id,integral_num,create_date) " +
				"values ('" + time + "','" + time + "'," + userId + "," + type + ",0,'" + desc + "', " + taskId + ", " + integral + ",'" + date + "')";
		System.out.println(INSERT_SQL);
	}

}
