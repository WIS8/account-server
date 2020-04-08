package cn.wis.account.model.request.param;

import lombok.Data;

@Data
public class ParamUpdateRequest {

	private String id;

	private String value;

	private String description;

}
