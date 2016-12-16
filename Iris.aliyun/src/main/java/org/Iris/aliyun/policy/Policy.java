package org.Iris.aliyun.policy;

import java.util.ArrayList;
import java.util.List;

public class Policy {

	private String Version = "1";
	private List<Statement> Statement;
	
	public String getVersion() {
		return Version;
	}
	
	public void setVersion(String version) {
		Version = version;
	}
	
	public List<Statement> getStatement() {
		return Statement;
	}
	
	public void addStatement(Statement statement) {
		if (null == this.Statement)
			this.Statement = new ArrayList<Statement>();
		this.Statement.add(statement);
	}
}
