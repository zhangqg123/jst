package org.jeecg.modules.qwert.jst.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * @Description: jst_zc_cat
 * @Author: jeecg-boot
 * @Date:   2020-07-19
 * @Version: V1.0
 */
@Data
@TableName("jst_zc_cat")
public class JstZcCat implements Serializable {
    private static final long serialVersionUID = 1L;

	/**资产分类编号*/
	@TableId(type = IdType.ID_WORKER_STR)
	private java.lang.String id;
	/**资产分类名称*/
	@Excel(name = "资产分类名称", width = 15)
	private java.lang.String zcCatname;
	/**原有编号*/
	@Excel(name = "原有编号", width = 15)
	private java.lang.String originId;
	/**采集协议*/
	@Excel(name = "采集协议", width = 15)
	private java.lang.String proType;
	/**上级id*/
	@Excel(name = "上级id", width = 15)
	private java.lang.String pid;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	private java.lang.String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
	private java.lang.String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date updateTime;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
	private java.lang.String sysOrgCode;
	/**是否有子节点*/
	@Excel(name = "是否有子节点", width = 15)
	private java.lang.String hasChild;
}
