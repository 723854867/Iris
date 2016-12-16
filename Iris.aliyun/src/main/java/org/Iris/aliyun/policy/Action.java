package org.Iris.aliyun.policy;

import java.util.ArrayList;
import java.util.List;

public enum Action {
	
	OSS_FULL_ACCESS {
		@Override
		public Object mark() {
			return OFA;
		}
	},
	OSS_READ_ONLY_ACCESS {
		@Override
		public Object mark() {
			return OROA;
		}
	};
	
	public abstract Object mark();
	
	private static final String OFA			= "oss:*";
	private static final List<String> OROA	= new ArrayList<String>();
	
	static {
		OROA.add("oss:Get*");
		OROA.add("oss:List*");
	}
}
