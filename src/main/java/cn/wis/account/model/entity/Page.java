package cn.wis.account.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Page {

	private Integer index;

	private Integer count;

	private String keyWord;

}
