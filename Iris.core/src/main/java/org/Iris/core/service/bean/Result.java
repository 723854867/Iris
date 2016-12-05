package org.Iris.core.service.bean;

import java.io.Serializable;

import org.Iris.core.consts.IrisConst;
import org.Iris.core.service.locale.ICode;
import org.Iris.util.common.SerializeUtil;

/**
 * 可以表示任何调用过程的返回结果(rpc调用、web调用、甚至是方法调用都可以)
 * 
 * @author Ahab
 *
 * @param <T>
 */
public class Result<T> implements Serializable {

	private static final long serialVersionUID = 4231387477200661362L;
	
	private int code;
	private String desc;
	private T data;
	
	public Result(int code, String desc) {
		this(code, desc, null);
	}
	
	public Result(int code, String desc, T data) {
		this.code = code;
		this.desc = desc;
		this.data = data;
	}
	
	public Result(ICode iCode) {
		this(iCode, null);
	}
	
	public Result(ICode iCode, T data) {
		this.code = iCode.constId();
		this.desc = iCode.defaultValue();
		this.data = data;
	}
	
	public Result(IrisConst<String> constant) {
		this.code = constant.constId();
		this.desc = constant.defaultValue();
	}
	
	public int getCode() {
		return code;
	}
	
	public Result<T> setCode(int code) {
		this.code = code;
		return this;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public Result<T> setDesc(String desc) {
		this.desc = desc;
		return this;
	}
	
	public T getData() {	
		return data;
	}
	
	public Result<T> setData(T data) {
		this.data = data;
		return this;
	}
	
	public static String jsonError(int code, String desc) { 
		Result<Void> result = new Result<Void>(code, desc);
		return SerializeUtil.JsonUtil.GSON.toJson(result);
	}
	
	public static String jsonError(IrisConst<String> constant) {
		return SerializeUtil.JsonUtil.GSON.toJson(new Result<Void>(constant));
	}
	
	public static String jsonSuccess() {
		return SerializeUtil.JsonUtil.GSON.toJson(new Result<Void>(ICode.Code.OK));
	}
	
	public static <T> String jsonSuccess(T data) { 
		return SerializeUtil.JsonUtil.GSON.toJson(new Result<T>(ICode.Code.OK, data));
	}
}
