package cn.wis.account.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.wis.account.model.table.Member;

public interface MemberMapper extends BaseMapper<Member> {

	@Select({"SELECT * FROM member "
			+ "WHERE nickname = #{nickname}"})
	Member selectByNickname(@Param("nickname") String nickname);

	@Select({"SELECT COUNT(role_id) FROM member "
			+ "WHERE role_id = #{roleId}"})
	Integer countRoleMemberNumber(@Param("roleId") String roleId);

}
