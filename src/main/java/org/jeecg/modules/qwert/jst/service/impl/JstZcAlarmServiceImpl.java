package org.jeecg.modules.qwert.jst.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.qwert.jst.entity.JstZcAlarm;
import org.jeecg.modules.qwert.jst.entity.JstZcCat;
import org.jeecg.modules.qwert.jst.mapper.JstZcAlarmMapper;
import org.jeecg.modules.qwert.jst.service.IJstZcAlarmService;
import org.jeecg.modules.system.entity.SysPermission;
import org.jeecg.modules.system.entity.SysPermissionDataRule;
import org.jeecg.modules.system.mapper.SysPermissionMapper;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: jst_zc_alarm
 * @Author: jeecg-boot
 * @Date:   2020-09-05
 * @Version: V1.0
 */
@Service
public class JstZcAlarmServiceImpl extends ServiceImpl<JstZcAlarmMapper, JstZcAlarm> implements IJstZcAlarmService {
	@Resource
	private JstZcAlarmMapper jstZcAlarmMapper;

	synchronized public void saveSys(JstZcAlarm jstZcAlarm) {
		this.save(jstZcAlarm);
	}
	synchronized public void updateSys(JstZcAlarm jstZcAlarm) {
		this.updateById(jstZcAlarm);
	}
	public List<JstZcAlarm> queryJzaList(String send_type) {
		return this.jstZcAlarmMapper.queryJzaList(send_type);
	}
	@Override
	synchronized public void deleteSys(String id) {
		// TODO Auto-generated method stub
		this.removeById(id);
		
	}

}
