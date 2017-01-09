package com.busap.vcs.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.busap.vcs.data.entity.Label;
import com.busap.vcs.data.entity.LabelVideo;

@Repository
public class LabelDao extends JdbcDaoSupport {
	public static final String UPDATE_LABEL_NUM = "UPDATE label set num = num + 1 where name = ?";
	public static final String INSERT_LABEL = "INSERT INTO label (name,create_at,num) values (?,?,0)";
	public static final String GET_LABEL_LIST = "SELECT id,name,num from label where name = ? limit 1";
	public static final String GET_LABEL_VIDEO_LIST = "SELECT id,label_name,video_id from label_video where video_id = ?";

	public void updateLabelNumByName(String name) {
		this.getJdbcTemplate().update(UPDATE_LABEL_NUM, name);
	}

	public void batchUpdateLabelNumByName(final String[] tags) {
		this.getJdbcTemplate().batchUpdate(UPDATE_LABEL_NUM, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, tags[i]);
			}

			public int getBatchSize() {
				return tags.length;
			}
		});
	}

	public void insert(String tag) {
		this.getJdbcTemplate().update(INSERT_LABEL, tag, new Date());
	}

	public Label getLabel(final String label) {
		return this.getJdbcTemplate().query(GET_LABEL_LIST, new ResultSetExtractor<Label>() {
			public Label extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Label labelVo = new Label();
					labelVo.setId(rs.getLong("id"));
					labelVo.setName(rs.getString("name"));
					labelVo.setNum(rs.getLong("num"));
					return labelVo;
				} else {
					return null;
				}
			}
		}, label);
	}

	public List<LabelVideo> getLabelVideoList(long videoId) {
		return this.getJdbcTemplate().query(GET_LABEL_VIDEO_LIST, new RowMapper<LabelVideo>() {
			public LabelVideo mapRow(ResultSet rs, int rowNum) throws SQLException {
				LabelVideo vo = new LabelVideo();
				vo.setId(rs.getLong("id"));
				vo.setLabelName(rs.getString("label_name"));
				vo.setVideoId(rs.getLong("video_id"));
				return vo;
			}
		}, videoId);
	}
}
