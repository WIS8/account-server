package cn.wis.account.model.request.feign;

import java.util.List;

import cn.wis.account.model.dto.ApiDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiConfirmRequest {

	private List<ApiDto> apis;

	private String appId;

}
