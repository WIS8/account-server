package cn.wis.account.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.wis.account.model.table.Provider;

public interface ProviderMapper extends BaseMapper<Provider> {

	@Select({"SELECT COUNT(id) FROM provider "
			+ "WHERE app_id = #{appId}"})
	int countByApp(@Param("appId") String appId);

	@Select({"SELECT * FROM provider "
			+ "WHERE entrance = #{url}"})
	Provider selectByUrl(@Param("url") String url);

	@Select({"SELECT * FROM provider "
			+ "LIMIT ${(index - 1) * size}, #{size}"})
	List<Provider> selectPage(@Param("index") int index, @Param("size") int size);

	@Select({"SELECT COUNT(id) FROM provider"})
	int countByKeyWord(@Param("keyWord") String keyWord);

}
