package cn.wis.account.model.dto;

import java.util.List;

import cn.wis.account.model.request.feign.ApiMap;
import lombok.Data;

@Data
public class ApiConfirmResult {

	private List<ApiMap> apis;

	private Integer createNumber;

	private Integer updateNumber;

	private Integer deleteNumber;

}
