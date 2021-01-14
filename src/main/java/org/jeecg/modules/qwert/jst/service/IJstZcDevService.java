package org.jeecg.modules.qwert.jst.service;

import java.util.List;

import org.jeecg.modules.qwert.jst.entity.JstZcDev;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: jst_zc_dev
 * @Author: jeecg-boot
 * @Date:   2020-07-24
 * @Version: V1.0
 */
public interface IJstZcDevService extends IService<JstZcDev> {
	public List<JstZcDev> queryJzdList();
	public List<JstZcDev> queryJmacList();
	public String handleRead(String catNo);
	List<JstZcDev> queryJzdList2(String catNo);
}
