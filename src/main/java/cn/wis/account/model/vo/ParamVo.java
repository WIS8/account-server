package cn.wis.account.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ParamVo extends AbstractVo {

	private String key;

	private String value;

	private String appName;

	private String description;

	private Integer uniqueParamNumber;

}
