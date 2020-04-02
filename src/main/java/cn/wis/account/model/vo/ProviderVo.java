package cn.wis.account.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ProviderVo extends AbstractVo {

	private String appName;

	private String identifier;

	private String entrance;

	private String description;

	private Integer uniqueParamNumber;

}
