package org.jeecg.modules.qwert.jst.service;

import java.util.List;

import org.jeecg.modules.qwert.jst.entity.JstZcAlarm;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: jst_zc_alarm
 * @Author: jeecg-boot
 * @Date:   2020-09-05
 * @Version: V1.0
 */
public interface IJstZcAlarmService extends IService<JstZcAlarm> {
	public void saveSys(JstZcAlarm jstZcAlarm);

	public List<JstZcAlarm> queryJzaList(String send_type);

	public void updateSys(JstZcAlarm jstZcAlarm);

	public void deleteSys(String id);
}
