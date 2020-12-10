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
 * @Description: jst_zc_dev
 * @Author: jeecg-boot
 * @Date:   2020-07-24
 * @Version: V1.0
 */
@Data
@TableName("jst_zc_dev")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="jst_zc_dev对象", description="jst_zc_dev")
public class JstZcDev implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**设备名称*/
	@Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
    private java.lang.String devName;
	/**设备分类*/
	@Excel(name = "设备分类", width = 15)
    @ApiModelProperty(value = "设备分类")
	@Dict(dicCode = "origin_id",dictTable="jst_zc_cat",dicText="zc_catname")
    private java.lang.String devCat;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private java.lang.String devNo;
	/**设备位置*/
	@Excel(name = "设备位置", width = 15)
    @ApiModelProperty(value = "设备位置")
	@Dict(dicCode = "pos_id",dictTable="jst_zc_position",dicText="pos_name")
    private java.lang.String devPos;
	/**位置*/
	@Excel(name = "位置", width = 15)
    @ApiModelProperty(value = "位置")
    private java.lang.String position;
	@Excel(name = "模型编号", width = 15)
    @ApiModelProperty(value = "模型编号")
    private java.lang.String modNo;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
	@Dict(dicCode = "ac_status")
    private java.lang.Integer status;
	/**连接类型*/
	@Excel(name = "连接类型", width = 15)
    @ApiModelProperty(value = "连接类型")
    private java.lang.String type;
	/**扩展信息*/
	@Excel(name = "扩展信息", width = 15)
    @ApiModelProperty(value = "扩展信息")
    private java.lang.String extInfo;
	/**连接信息*/
	@Excel(name = "连接信息", width = 15)
    @ApiModelProperty(value = "连接信息")
    private java.lang.String conInfo;
	/**属性信息*/
	@Excel(name = "属性信息", width = 15)
    @ApiModelProperty(value = "属性信息")
    private java.lang.String proInfo;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
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
