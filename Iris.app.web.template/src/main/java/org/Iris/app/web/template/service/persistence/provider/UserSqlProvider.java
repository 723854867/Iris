package org.Iris.app.web.template.service.persistence.provider;

import org.apache.ibatis.jdbc.SQL;

public class UserSqlProvider {

	public String selectAll() { 
		return new SQL(){
			{
				SELECT("*");
				FROM("user");
			}
		}.toString();
	}
}
