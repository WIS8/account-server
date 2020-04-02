package cn.wis.account.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.wis.account.model.table.Role;

public interface RoleMapper extends BaseMapper<Role> {

	@Select({"SELECT * FROM role "
			+ "WHERE appellation = #{name}"})
	Role selectByName(@Param("name") String name);

	@Select({"SELECT * FROM role "
			+ "LIMIT ${(index - 1) * size}, #{size}"})
	List<Role> selectPage(@Param("index") int index, @Param("size") int size);

	@Select({"SELECT COUNT(id) FROM role"})
	int countByKeyWord(@Param("keyWord") String keyWord);

}
