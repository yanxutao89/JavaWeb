package com.yxt.crud.bean;

import java.util.Date;
import java.util.Objects;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/11/05 15:31
 */
public class UserPojo {

	private Long userId; 		// 用户ID
	private String userName; 	// 用户名称
	private String password; 	// 密码
	private Integer status;		// 状态
	private Date createTime; 	// 创建时间
	private Date updateTime; 	// 更新时间

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof UserPojo)) return false;
		UserPojo userPojo = (UserPojo) o;
		return Objects.equals(getUserId(), userPojo.getUserId()) &&
				Objects.equals(getUserName(), userPojo.getUserName()) &&
				Objects.equals(getPassword(), userPojo.getPassword()) &&
				Objects.equals(getStatus(), userPojo.getStatus()) &&
				Objects.equals(getCreateTime(), userPojo.getCreateTime()) &&
				Objects.equals(getUpdateTime(), userPojo.getUpdateTime());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUserId(), getUserName(), getPassword(), getStatus(), getCreateTime(), getUpdateTime());
	}

	@Override
	public String toString() {
		return "UserPojo{" +
				"userId=" + userId +
				", userName='" + userName + '\'' +
				", password='" + password + '\'' +
				", status='" + status + '\'' +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				'}';
	}
}
