package cn.wis.account.model.entity;

import cn.wis.account.model.vo.MemberVo;
import lombok.Data;

@Data
public class Token {

	public static Token getInstance(MemberVo memberVo) {
		Token token = new Token();
		token.setCreateTime(System.currentTimeMillis());
		token.setUpdateTime(token.getCreateTime());
		token.setMemberVo(memberVo);
		return token;
	}

	private MemberVo memberVo;

	private String cookie;

	private Long createTime;

	private Long updateTime;

	public void updateCookie(String cookie) {
		this.cookie = cookie;
		updateTime = System.currentTimeMillis();
	}

}
