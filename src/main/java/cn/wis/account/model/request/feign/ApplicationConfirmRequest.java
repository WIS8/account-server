package cn.wis.account.model.request.feign;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationConfirmRequest {

	private String name;

	private String pluginName;

}
