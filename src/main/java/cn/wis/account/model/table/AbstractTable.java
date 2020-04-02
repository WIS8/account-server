package cn.wis.account.model.table;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

@Data
public abstract class AbstractTable {

	@TableId(type = IdType.UUID)
	private String id;

	private String creatorId;

	private Long createTime;

	private String updaterId;

	private Long updateTime;

	public void setCreateFields(String operatorId) {
		creatorId = operatorId;
		createTime = System.currentTimeMillis();
	}

	public void setUpdateFields(String operatorId) {
		updaterId = operatorId;
		updateTime = System.currentTimeMillis();
	}

}
