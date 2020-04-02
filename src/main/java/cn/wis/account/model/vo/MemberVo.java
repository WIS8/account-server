package cn.wis.account.model.vo;

import lombok.Data;

@Data
public class MemberVo {

	private String id;

	private String roleId;

	private String nickname;

	private Long registerTime;

	private String depository;

}
