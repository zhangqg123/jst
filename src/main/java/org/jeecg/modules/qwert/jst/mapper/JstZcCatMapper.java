package org.jeecg.modules.qwert.jst.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.qwert.jst.entity.JstZcAlarm;
import org.jeecg.modules.qwert.jst.entity.JstZcCat;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: jst_zc_cat
 * @Author: jeecg-boot
 * @Date:   2020-07-19
 * @Version: V1.0
 */
public interface JstZcCatMapper extends BaseMapper<JstZcCat> {
	public List<JstZcCat> queryJzcList();

	/**
	 * 编辑节点状态
	 * @param id
	 * @param status
	 */
	void updateTreeNodeStatus(@Param("id") String id,@Param("status") String status);

}
