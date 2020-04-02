package cn.wis.account.model.table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class Application extends AbstractTable {

	private String appellation;

	private String pluginName;

	private String description;

}
