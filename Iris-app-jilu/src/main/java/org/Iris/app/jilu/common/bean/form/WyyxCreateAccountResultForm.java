package org.Iris.app.jilu.common.bean.form;

public class WyyxCreateAccountResultForm {

	private int code;
	private Result info;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Result getInfo() {
		return info;
	}

	public void setInfo(Result info) {
		this.info = info;
	}

	 public class Result {
		private String token;
		private String accid;
		private String name;

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public String getAccid() {
			return accid;
		}

		public void setAccid(String accid) {
			this.accid = accid;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}
}