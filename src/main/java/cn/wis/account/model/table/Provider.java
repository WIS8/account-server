package cn.wis.account.model.table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class Provider extends AbstractTable {

	private String appId;

	private String identifier;

	private String entrance;

	private String description;

}
