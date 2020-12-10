package org.jeecg.modules.qwert.jst.entity;

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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: jst_zc_target
 * @Author: jeecg-boot
 * @Date:   2020-07-24
 * @Version: V1.0
 */
@Data
@TableName("jst_zc_target")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="jst_zc_target对象", description="jst_zc_target")
public class JstZcTarget implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**设备类型*/
	@Excel(name = "设备类型", width = 15)
    @ApiModelProperty(value = "设备类型")
	@Dict(dicCode = "origin_id",dictTable="jst_zc_cat",dicText="zc_catname")
    private java.lang.String devType;
	/**指标编码*/
	@Excel(name = "指标编码", width = 15)
    @ApiModelProperty(value = "指标编码")
    private java.lang.String targetNo;
	/**指标名称*/
	@Excel(name = "指标名称", width = 15)
    @ApiModelProperty(value = "指标名称")
    private java.lang.String targetName;
	/**单位*/
	@Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位")
    private java.lang.String unit;
	/**是否采集*/
	@Excel(name = "是否采集", width = 15)
    @ApiModelProperty(value = "是否采集")
    private java.lang.String ifGet;
	/**数据类型*/
	@Excel(name = "数据类型", width = 15)
    @ApiModelProperty(value = "数据类型")
	@Dict(dicCode = "d_type")
    private java.lang.String dataType;
	/**协议*/
	@Excel(name = "协议", width = 15)
    @ApiModelProperty(value = "协议")
    private java.lang.String proType;
	/**指令*/
	@Excel(name = "寄存器地址", width = 15)
    @ApiModelProperty(value = "寄存器地址")
    private java.lang.String address;
	@Excel(name = "地址格式", width = 15)
    @ApiModelProperty(value = "地址格式")
	@Dict(dicCode = "address_type")
    private java.lang.String addressType;
    @Excel(name = "指令", width = 15)
    @ApiModelProperty(value = "指令")
    private java.lang.String instruct;
	/**采集时间*/
	@Excel(name = "采集时间", width = 15)
    @ApiModelProperty(value = "采集时间")
    private java.lang.String getTime;
	/**截取开始字节*/
	@Excel(name = "截取开始字节", width = 15)
    @ApiModelProperty(value = "截取开始字节")
    private java.lang.String offset;
	/**截取字节数*/
	@Excel(name = "截取字节数", width = 15)
    @ApiModelProperty(value = "截取字节数")
    private java.lang.String len;
	/**截取位*/
	@Excel(name = "截取位", width = 15)
    @ApiModelProperty(value = "截取位")
    private java.lang.String interceptBit;
	/**表达式配置*/
	@Excel(name = "表达式配置", width = 15)
    @ApiModelProperty(value = "表达式配置")
    private java.lang.String espConfig;
	/**监控类型*/
	@Excel(name = "监控类型", width = 15)
    @ApiModelProperty(value = "监控类型")
    private java.lang.String monitorType;
	/**信息点类型*/
	@Excel(name = "信息点类型", width = 15)
    @ApiModelProperty(value = "信息点类型")
    private java.lang.String infoType;
	/**信息点数值类型*/
	@Excel(name = "信息点数值类型", width = 15)
    @ApiModelProperty(value = "信息点数值类型")
    private java.lang.String infoDatatype;
	/**信息点数值精度*/
	@Excel(name = "信息点数值精度", width = 15)
    @ApiModelProperty(value = "信息点数值精度")
    private java.lang.String infoDataaccurate;
	/**报警点*/
	@Excel(name = "报警点", width = 15)
    @ApiModelProperty(value = "报警点")
	@Dict(dicCode = "alarm_point")
	private java.lang.String alarmPoint;
	/**因子*/
	@Excel(name = "因子", width = 15)
    @ApiModelProperty(value = "因子")
    private java.lang.String yinzi;

	/**控制上限*/
	@Excel(name = "控制上限", width = 15)
    @ApiModelProperty(value = "控制上限")
    private java.lang.String ctrlUp;
	/**控制下限*/
	@Excel(name = "控制下限", width = 15)
    @ApiModelProperty(value = "控制下限")
    private java.lang.String ctrlDown;
	/**指令顺序*/
	@Excel(name = "指令顺序", width = 15)
    @ApiModelProperty(value = "指令顺序")
    private java.lang.String targetOrder;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
}
