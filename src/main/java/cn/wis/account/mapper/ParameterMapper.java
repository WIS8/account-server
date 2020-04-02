package cn.wis.account.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.wis.account.model.table.Parameter;

public interface ParameterMapper extends BaseMapper<Parameter> {

	@Select({"SELECT COUNT(id) FROM parameter "
			+ "WHERE app_id = #{appId}"})
	int countByApp(@Param("appId") String appId);

	@Select({"SELECT * FROM parameter "
			+ "WHERE app_id = #{appId}"})
	List<Parameter> selectAllInApp(@Param("appId") String appId);

}
