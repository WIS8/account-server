package cn.wis.account.model.table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class Role extends AbstractTable {

	private Integer maximum;

	private String appellation;

	private String description;

}
