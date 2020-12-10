package org.jeecg.modules.qwert.jst.service;

import org.jeecg.modules.qwert.jst.entity.JstZcCat;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

import org.jeecg.common.exception.JeecgBootException;

/**
 * @Description: jst_zc_cat
 * @Author: jeecg-boot
 * @Date:   2020-07-19
 * @Version: V1.0
 */
public interface IJstZcCatService extends IService<JstZcCat> {

	/**根节点父ID的值*/
	public static final String ROOT_PID_VALUE = "0";
	
	/**树节点有子节点状态值*/
	public static final String HASCHILD = "1";
	
	/**树节点无子节点状态值*/
	public static final String NOCHILD = "0";
	
	public List<JstZcCat> queryJzcList();
	/**新增节点*/
	void addJstZcCat(JstZcCat jstZcCat);
	
	/**修改节点*/
	void updateJstZcCat(JstZcCat jstZcCat) throws JeecgBootException;
	
	/**删除节点*/
	void deleteJstZcCat(String id) throws JeecgBootException;

}
