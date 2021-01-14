package org.jeecg.modules.qwert.jst.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.qwert.jst.entity.JstZcCat;
import org.jeecg.modules.qwert.jst.entity.JstZcDev;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: jst_zc_dev
 * @Author: jeecg-boot
 * @Date:   2020-07-24
 * @Version: V1.0
 */
public interface JstZcDevMapper extends BaseMapper<JstZcDev> {
	public List<JstZcDev> queryJzdList();
	public List<JstZcDev> queryJzdList2(@Param("dev_cat") String catNo);

	public List<JstZcDev> queryJmacList();

}
