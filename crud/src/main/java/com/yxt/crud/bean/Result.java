package com.yxt.crud.bean;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/11/05 15:57
 */
public class Result {

	private int code;			// 状态码,200-成功,-1-失败
	private String msg;			// 提示信息
	private Object data;		// 数据
	private List<Object> ids;	// 业务ids

	public Result() {
		this.code = -1;
		this.ids = new ArrayList<>();
	}

	public Result(int code, String msg, Object data, List<Object> ids) {
		this.code = code;
		this.msg = msg;
		this.data = data;
		this.ids = ids;
	}

	public static Result success(){
		Result result = new Result();
		result.setCode(200);
		return result;
	}

	public static Result failure(){
		Result result = new Result();
		result.setCode(-1);
		return result;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public List<Object> getIds() {
		return ids;
	}

	public void setIds(List<Object> ids) {
		this.ids = ids;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Result)) return false;
		Result result = (Result) o;
		return getCode() == result.getCode() &&
				Objects.equals(getMsg(), result.getMsg()) &&
				Objects.equals(getData(), result.getData()) &&
				Objects.equals(getIds(), result.getIds());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getCode(), getMsg(), getData(), getIds());
	}

	@Override
	public String toString() {
		return "Result{" +
				"code=" + code +
				", msg='" + msg + '\'' +
				", data=" + data +
				", ids=" + ids +
				'}';
	}
}
