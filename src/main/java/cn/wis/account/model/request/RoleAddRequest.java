package cn.wis.account.model.request;

import lombok.Data;

@Data
public class RoleAddRequest {

	private Integer maximum;

	private String appellation;

	private String description;

}
