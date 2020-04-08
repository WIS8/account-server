package cn.wis.account.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.wis.account.model.entity.Page;
import cn.wis.account.model.table.Member;

public interface MemberMapper extends BaseMapper<Member> {

	@Results(id = "memberMap", value = {
			@Result(column = "id", property = "id", jdbcType = JdbcType.VARCHAR),
			@Result(column = "creator_id", property = "creatorId", jdbcType = JdbcType.VARCHAR),
			@Result(column = "create_time", property = "createTime", jdbcType = JdbcType.BIGINT),
			@Result(column = "updater_id", property = "updaterId", jdbcType = JdbcType.VARCHAR),
			@Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.BIGINT),
			@Result(column = "role_id", property = "roleId", jdbcType = JdbcType.VARCHAR),
			@Result(column = "nickname", property = "nickname", jdbcType = JdbcType.VARCHAR),
			@Result(column = "authentication", property = "authentication", jdbcType = JdbcType.VARCHAR),
			@Result(column = "depository", property = "depository", jdbcType = JdbcType.VARCHAR),
	})
	@Select({"SELECT * FROM member "
			+ "WHERE nickname = #{nickname}"})
	Member selectByNickname(@Param("nickname") String nickname);

	@Select({"SELECT COUNT(role_id) AS total FROM member "
			+ "WHERE role_id = #{roleId}"})
	@Results(id = "memberTotalMap", value = {
			@Result(column = "total", jdbcType = JdbcType.INTEGER)
	})
	Integer countRoleMemberNumber(@Param("roleId") String roleId);

	@Select({"<script>"
			+ "SELECT SQL_CALC_FOUND_ROWS * FROM member "
			+ "<if test='page.keyWord != null'>"
			+ "WHERE nickname LIKE CONCAT('%', #{page.keyWord}, '%') "
			+ "</if>"
			+ "LIMIT ${(page.index - 1) * page.count}, #{page.count};"
			+ "SELECT FOUND_ROWS() AS total;"
			+ "</script>"})
	@ResultMap({"memberMap", "memberTotalMap"})
	List<List<?>> selectByPage(@Param("page") Page page);

}
