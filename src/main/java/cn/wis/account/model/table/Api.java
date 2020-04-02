package cn.wis.account.model.table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class Api extends AbstractTable {

	private String appId;

	private String router;

	private Integer accessRule;

	private String appellation;

	private String description;

}
