package com.busap.vcs.dao;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class VideoCheckDao extends JdbcDaoSupport {
	private static String INSERT_SQL = "insert into video_check (uid,videoid,publishdate) values (?,?,?)";
	
	public int insert(long uid,long vid,long time){
		return this.getJdbcTemplate().update(INSERT_SQL, uid,vid,time);
	}
}
