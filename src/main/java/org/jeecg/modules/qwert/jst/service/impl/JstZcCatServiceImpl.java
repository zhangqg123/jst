package org.jeecg.modules.qwert.jst.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.qwert.jst.entity.JstZcCat;
import org.jeecg.modules.qwert.jst.mapper.JstZcAlarmMapper;
import org.jeecg.modules.qwert.jst.mapper.JstZcCatMapper;
import org.jeecg.modules.qwert.jst.service.IJstZcCatService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: jst_zc_cat
 * @Author: jeecg-boot
 * @Date:   2020-07-19
 * @Version: V1.0
 */
@Service
public class JstZcCatServiceImpl extends ServiceImpl<JstZcCatMapper, JstZcCat> implements IJstZcCatService {
	@Resource
	private JstZcCatMapper jstZcCatMapper;

	@Cacheable(value = CacheConstant.JST_CAT_CACHE)
	@Override
	public List<JstZcCat> queryJzcList() {
//		QueryWrapper<JstZcCat> cqw = QueryGenerator.initQueryWrapper(new JstZcCat(), null);
//		cqw.eq("has_child", "0");
//		cqw.gt("id", "002");
		List<JstZcCat> jzcList = this.jstZcCatMapper.queryJzcList();
		return jzcList;
	}
	
	@Override
	public void addJstZcCat(JstZcCat jstZcCat) {
		if(oConvertUtils.isEmpty(jstZcCat.getPid())){
			jstZcCat.setPid(IJstZcCatService.ROOT_PID_VALUE);
		}else{
			//如果当前节点父ID不为空 则设置父节点的hasChildren 为1
			JstZcCat parent = baseMapper.selectById(jstZcCat.getPid());
			if(parent!=null && !"1".equals(parent.getHasChild())){
				parent.setHasChild("1");
				baseMapper.updateById(parent);
			}
		}
		baseMapper.insert(jstZcCat);
	}
	
	@Override
	public void updateJstZcCat(JstZcCat jstZcCat) {
		JstZcCat entity = this.getById(jstZcCat.getId());
		if(entity==null) {
			throw new JeecgBootException("未找到对应实体");
		}
		String old_pid = entity.getPid();
		String new_pid = jstZcCat.getPid();
		if(!old_pid.equals(new_pid)) {
			updateOldParentNode(old_pid);
			if(oConvertUtils.isEmpty(new_pid)){
				jstZcCat.setPid(IJstZcCatService.ROOT_PID_VALUE);
			}
			if(!IJstZcCatService.ROOT_PID_VALUE.equals(jstZcCat.getPid())) {
				baseMapper.updateTreeNodeStatus(jstZcCat.getPid(), IJstZcCatService.HASCHILD);
			}
		}
		baseMapper.updateById(jstZcCat);
	}
	
	@Override
	public void deleteJstZcCat(String id) throws JeecgBootException {
		JstZcCat jstZcCat = this.getById(id);
		if(jstZcCat==null) {
			throw new JeecgBootException("未找到对应实体");
		}
		updateOldParentNode(jstZcCat.getPid());
		baseMapper.deleteById(id);
	}
	
	
	
	/**
	 * 根据所传pid查询旧的父级节点的子节点并修改相应状态值
	 * @param pid
	 */
	private void updateOldParentNode(String pid) {
		if(!IJstZcCatService.ROOT_PID_VALUE.equals(pid)) {
			Integer count = baseMapper.selectCount(new QueryWrapper<JstZcCat>().eq("pid", pid));
			if(count==null || count<=1) {
				baseMapper.updateTreeNodeStatus(pid, IJstZcCatService.NOCHILD);
			}
		}
	}

}
