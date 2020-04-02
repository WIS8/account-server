package cn.wis.account.model.request.feign;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProviderConfirmRequest {

	private String url;

	private String appId;

}
