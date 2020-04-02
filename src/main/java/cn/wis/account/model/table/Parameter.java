package cn.wis.account.model.table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class Parameter extends AbstractTable {

	private String appId;

	private String appellation;

	private String defaultValue;

	private String description;

}
