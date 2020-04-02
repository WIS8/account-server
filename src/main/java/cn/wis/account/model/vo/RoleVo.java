package cn.wis.account.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class RoleVo extends AbstractVo {

	private Integer maximum;

	private String appellation;

	private String description;

	private Integer memberNumber;

}
