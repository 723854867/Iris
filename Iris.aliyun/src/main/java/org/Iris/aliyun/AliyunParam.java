package org.Iris.aliyun;

import org.Iris.aliyun.bean.Region;
import org.Iris.core.consts.IrisConst;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.util.common.uuid.AlternativeJdkIdGenerator;
import org.Iris.util.lang.DateUtils;

public enum AliyunParam implements IrisConst<String> {
	
	RegionId(Region.CN_HANGZHOU.mark()),
	
	Format(org.Iris.aliyun.bean.Format.XML.name()),
	
	Version("2015-04-01"),
	
	AccessKeyId,
	
	Signature,
	
	SignatureMethod(org.Iris.aliyun.bean.SignatureMethod.HMAC_SHA1.mark()),
	
	SignatureVersion("1.0"),
	
	SignatureNonce {
		@Override
		public String defaultValue() {
			return AlternativeJdkIdGenerator.INSTANCE.generateId().toString();
		}
	},
	
	Timestamp {
		@Override
		public String defaultValue() {
			return DateUtils.getUTCDate();
		}
	},
	
	Action,
	
	RoleArn,
	
	RoleSessionName, 
	
	Policy,
	
	DurationSeconds("3600") {
		@Override
		public String parse(String value) {
			int val = Integer.valueOf(value);
			if (val >= 900 && val <= 3600)
				return value;
			throw IllegalConstException.errorException(this);
		}
	},
	
	Authorization,
	
	Content_Length {
		@Override
		public String key() {
			return "Content-Length";
		}
	},
	
	Content_Type {
		@Override
		public String key() {
			return "Content-Type";
		}
	},
	
	Date,
	
	Host;
	
	private String defaultValue;
	
	private AliyunParam() {
		this(null);
	}
	
	private AliyunParam(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public int constId() {
		throw new UnsupportedOperationException("AliyunConst param has no const id!");
	}

	@Override
	public String key() {
		return this.name();
	}

	@Override
	public String parse(String value) {
		return value;
	}

	@Override
	public String defaultValue() {
		return this.defaultValue;
	}
}
