package org.Iris.app.web.template.service.persistence.mapper;

import java.util.List;

import org.Iris.app.web.template.service.persistence.domain.User;
import org.Iris.app.web.template.service.persistence.provider.UserSqlProvider;
import org.apache.ibatis.annotations.SelectProvider;

public interface UserMapper {

	@SelectProvider(type = UserSqlProvider.class, method = "selectAll")
	List<User> selectAll();
}
