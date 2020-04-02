package cn.wis.account.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ApplicationVo extends AbstractVo {

	private String appellation;

	private String pluginName;

	private String description;

	private Integer apiNumber;

	private Integer providerNumber;

	private Integer parameterNumber;

}
