package cn.wis.account.model.request;

import lombok.Data;

@Data
public class RoleUpdateRequest {

	private String id;

	private Integer maximum;

	private String appellation;

	private String description;

}
