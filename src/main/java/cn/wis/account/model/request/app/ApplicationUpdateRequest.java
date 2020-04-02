package cn.wis.account.model.request.app;

import lombok.Data;

@Data
public class ApplicationUpdateRequest {

	private String id;

	private String name;

	private String pluginName;

	private String description;

}
