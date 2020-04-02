package cn.wis.account.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ApiVo extends AbstractVo {

	private String providerId;

	private String appellation;

	private String description;

}
