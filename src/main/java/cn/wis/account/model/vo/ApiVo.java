package cn.wis.account.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ApiVo extends AbstractVo {

	private String appId;

	private String router;

	private Integer accessRule;

	private String appellation;

	private String description;

}
