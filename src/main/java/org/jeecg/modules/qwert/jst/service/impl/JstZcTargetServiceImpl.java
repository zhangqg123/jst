package org.jeecg.modules.qwert.jst.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.qwert.jst.entity.JstZcCat;
import org.jeecg.modules.qwert.jst.entity.JstZcTarget;
import org.jeecg.modules.qwert.jst.entity.JstZcTarget2;
import org.jeecg.modules.qwert.jst.mapper.JstZcDevMapper;
import org.jeecg.modules.qwert.jst.mapper.JstZcTargetMapper;
import org.jeecg.modules.qwert.jst.service.IJstZcTargetService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: jst_zc_target
 * @Author: jeecg-boot
 * @Date:   2020-07-24
 * @Version: V1.0
 */
@Service
public class JstZcTargetServiceImpl extends ServiceImpl<JstZcTargetMapper, JstZcTarget> implements IJstZcTargetService {
	@Resource
	private JstZcTargetMapper jstZcTargetMapper;

//	@Cacheable(value = CacheConstant.JST_TARGET_CACHE)
	@Override
	public List<JstZcTarget> queryJztList() {
//		QueryWrapper<JstZcTarget> zqw = QueryGenerator.initQueryWrapper(new JstZcTarget(), null);
//		zqw.orderByAsc("instruct");
//		zqw.orderByAsc("tmp");
		List<JstZcTarget> jztList = this.jstZcTargetMapper.queryJztList();			
		return jztList;
	}
	@Override
	public List<JstZcTarget2> queryJztList2() {
		List<JstZcTarget2> jztList = this.jstZcTargetMapper.queryJztList2();			
		return jztList;
	}
	@Override
	public List<JstZcTarget> queryJztList3(String dev_type) {
		List<JstZcTarget> jztList = this.jstZcTargetMapper.queryJztList3(dev_type);			
		return jztList;
	}
	
//	@CacheEvict(value = CacheConstant.JST_TARGET_CACHE,allEntries=true)
	@Override
	public boolean edit(JstZcTarget jstZcTarget) {
		return this.updateById(jstZcTarget);
	}
	
}
