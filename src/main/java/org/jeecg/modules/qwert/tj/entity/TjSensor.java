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
 * @Description: tj_sensor
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Data
@TableName("tj_sensor")
public class TjSensor implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**ID*/
	@TableId(type = IdType.ID_WORKER_STR)
    private java.lang.String id;
	/**传感器名称*/
	@Excel(name = "传感器名称", width = 15)
    private java.lang.String sensorname;
	/**传感器编号*/
	@Excel(name = "传感器编号", width = 15)
    private java.lang.String sensorno;
	/**顺序*/
	@Excel(name = "顺序", width = 15)
    private java.lang.String sensororder;
	/**模型编号*/
	@Excel(name = "模型编号", width = 15)
	@Dict(dicCode = "modelno",dictTable = "tj_model",dicText = "modelname")
    private java.lang.String sensormodel;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    private java.lang.String sensordevice;
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
