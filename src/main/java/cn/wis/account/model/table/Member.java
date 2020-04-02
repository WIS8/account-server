package cn.wis.account.model.table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class Member extends AbstractTable {

	private String nickname;

	private String roleId;

	private String authentication;

	private String depository;

}
