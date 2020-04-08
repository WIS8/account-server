package cn.wis.account.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.wis.account.model.table.ParamValue;

public interface ParamValueMapper extends BaseMapper<ParamValue> {

	@Select({"SELECT COUNT(id) FROM param_value "
			+ "WHERE provider_id = #{providerId}"})
	int countByProvider(@Param("providerId") String providerId);

	@Select({"SELECT COUNT(id) FROM param_value "
			+ "WHERE param_id = #{paramId}"})
	int countByParam(@Param("paramId") String paramId);

	@Select({"SELECT * FROM param_value "
			+ "WHERE provider_id = #{providerId}"})
	List<ParamValue> selectAllInProvider(@Param("providerId") String providerId);

}
