package cn.wis.account.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.wis.account.model.table.Application;

public interface ApplicationMapper extends BaseMapper<Application> {

	@Select({"SELECT * FROM application "
			+ "WHERE appellation = #{name}"})
	Application selectByName(@Param("name") String name);

	@Select({"SELECT * FROM application "
			+ "LIMIT ${(index - 1) * size}, #{size}"})
	List<Application> selectPage(@Param("index") int index, @Param("size") int size);

	@Select({"SELECT COUNT(id) FROM application"})
	int countByKeyWord(@Param("keyWord") String keyWord);

	@Select({"<script>"
			+ "SELECT id, plugin_name, description FROM application "
			+ "WHERE id IN "
			+ "<foreach collection='set' item='item' open='(' separator=',' close=')'>"
			+ "#{item}"
			+ "</foreach>"
			+ "</script>"})
	List<Application> selectAllByIds(@Param("set") Set<String> ids);

}
