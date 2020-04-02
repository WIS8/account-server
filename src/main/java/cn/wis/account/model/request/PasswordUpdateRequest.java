package cn.wis.account.model.request;

import lombok.Data;

@Data
public class PasswordUpdateRequest {

	private String oldPassword;

	private String newPassword;

}
