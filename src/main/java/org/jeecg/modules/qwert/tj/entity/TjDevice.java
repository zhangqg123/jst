package org.jeecg.modules.qwert.tj.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
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
 * @Description: tj_device
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Data
@TableName("tj_device")
public class TjDevice implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**ID*/
	@TableId(type = IdType.ID_WORKER_STR)
    private java.lang.String id;
	/**设备名称*/
	@Excel(name = "设备名称", width = 15)
    private java.lang.String devicename;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    private java.lang.String deviceno;
	/**顺序*/
	@Excel(name = "顺序", width = 15)
    private java.lang.String deviceorder;
	/**模型编号*/
	@Excel(name = "模型编号", width = 15)
	@Dict(dicCode = "modelno",dictTable = "tj_model",dicText = "modelname")
    private java.lang.String devicemodel;
	/**X轴*/
	@Excel(name = "X轴", width = 15)
    private java.lang.String devicex;
	/**Y轴*/
	@Excel(name = "Y轴", width = 15)
    private java.lang.String devicey;
	/**Z轴*/
	@Excel(name = "Z轴", width = 15)
    private java.lang.String devicez;
//	private java.lang.Integer deviceState;
	
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date createTime;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    private java.lang.String createBy;
	/**更新时间*/
	@Excel(name = "更新时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date updateTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    private java.lang.String updateBy;
}
