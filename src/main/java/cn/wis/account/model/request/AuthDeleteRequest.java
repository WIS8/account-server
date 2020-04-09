package cn.wis.account.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthDeleteRequest {

	private String roleId;

	private String apiId;

}
