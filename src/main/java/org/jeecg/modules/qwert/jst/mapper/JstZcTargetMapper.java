package org.jeecg.modules.qwert.jst.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.qwert.jst.entity.JstZcAlarm;
import org.jeecg.modules.qwert.jst.entity.JstZcTarget;
import org.jeecg.modules.qwert.jst.entity.JstZcTarget2;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: jst_zc_target
 * @Author: jeecg-boot
 * @Date:   2020-07-24
 * @Version: V1.0
 */
public interface JstZcTargetMapper extends BaseMapper<JstZcTarget> {

	public List<JstZcTarget> queryJztList();
	public List<JstZcTarget2> queryJztList2();
	public List<JstZcTarget> queryJztList3(@Param("dev_type") String dev_type);

}
