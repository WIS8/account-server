package cn.wis.account.model.request.provider;

import lombok.Data;

@Data
public class ProviderUpdateRequest {

	private String id;

	private String identifier;

	private String description;

}
