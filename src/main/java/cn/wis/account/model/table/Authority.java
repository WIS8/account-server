package cn.wis.account.model.table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class Authority extends AbstractTable {

	private String roleId;

	private String apiId;

}
