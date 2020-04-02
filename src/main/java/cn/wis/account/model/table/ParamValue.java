package cn.wis.account.model.table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ParamValue extends AbstractTable {

	private String paramId;

	private String providerId;

	private String content;

	private String description;

}
