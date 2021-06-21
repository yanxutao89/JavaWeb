package com.user.model;

import java.util.Date;
import java.util.Objects;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/11/06 15:53
 */
public class LoggerPojo {

	private Long loggerId;				// 日志ID
	private Long requestNo;				// 请求编号
	private String requestMd5;			// 请求Md5
	private Date requestTime;			// 请求时间
	private String requestParams;		// 请求参数
	private Date responseTime;			// 响应时间
	private String responseParams;		// 响应参数
	private Long rtt;					// Round-trip time

	public Long getLoggerId() {
		return loggerId;
	}

	public void setLoggerId(Long loggerId) {
		this.loggerId = loggerId;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public Long getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(Long requestNo) {
		this.requestNo = requestNo;
	}

	public String getRequestMd5() {
		return requestMd5;
	}

	public void setRequestMd5(String requestMd5) {
		this.requestMd5 = requestMd5;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	public String getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(String requestParams) {
		this.requestParams = requestParams;
	}

	public Date getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}

	public String getResponseParams() {
		return responseParams;
	}

	public void setResponseParams(String responseParams) {
		this.responseParams = responseParams;
	}

	public Long getRtt() {
		return rtt;
	}

	public void setRtt(Long rtt) {
		this.rtt = rtt;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof LoggerPojo)) return false;
		LoggerPojo that = (LoggerPojo) o;
		return Objects.equals(getLoggerId(), that.getLoggerId()) &&
				Objects.equals(getRequestNo(), that.getRequestNo()) &&
				Objects.equals(getRequestMd5(), that.getRequestMd5()) &&
				Objects.equals(getRequestTime(), that.getRequestTime()) &&
				Objects.equals(getRequestParams(), that.getRequestParams()) &&
				Objects.equals(getResponseTime(), that.getResponseTime()) &&
				Objects.equals(getResponseParams(), that.getResponseParams()) &&
				Objects.equals(getRtt(), that.getRtt());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getLoggerId(), getRequestNo(), getRequestMd5(), getRequestTime(), getRequestParams(), getResponseTime(), getResponseParams(), getRtt());
	}

	@Override
	public String toString() {
		return "LoggerPojo{" +
				"loggerId=" + loggerId +
				", requestNo=" + requestNo +
				", requestMd5='" + requestMd5 + '\'' +
				", requestTime=" + requestTime +
				", requestParams='" + requestParams + '\'' +
				", responseTime=" + responseTime +
				", responseParams='" + responseParams + '\'' +
				", rtt=" + rtt +
				'}';
	}
}
