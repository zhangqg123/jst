package org.jeecg.modules.qwert.jst.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.qwert.jst.entity.JstZcAlarm;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: jst_zc_alarm
 * @Author: jeecg-boot
 * @Date:   2020-09-05
 * @Version: V1.0
 */
public interface JstZcAlarmMapper extends BaseMapper<JstZcAlarm> {

	public List<JstZcAlarm> queryJzaList(@Param("send_type") String send_type);

}
