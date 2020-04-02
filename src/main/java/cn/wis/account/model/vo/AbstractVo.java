package cn.wis.account.model.vo;

import lombok.Data;

@Data
public abstract class AbstractVo {

	private String id;

	private String creatorId;

	private Long createTime;

	private String updaterId;

	private Long updateTime;

}
