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
 * @Description: jst_zc_assert
 * @Author: jeecg-boot
 * @Date:   2020-09-06
 * @Version: V1.0
 */
@Data
@TableName("jst_zc_assert")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="jst_zc_assert对象", description="jst_zc_assert")
public class JstZcAssert implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**设备类型*/
	@Excel(name = "设备类型", width = 15)
    @ApiModelProperty(value = "设备类型")
	@Dict(dicCode = "origin_id",dictTable="jst_zc_cat",dicText="zc_catname")
    private java.lang.String assertCat;
	/**数字点编号*/
	@Excel(name = "数字点编号", width = 15)
    @ApiModelProperty(value = "数字点编号")
    private java.lang.String assertTarget;
	/**报警点名称*/
	@Excel(name = "报警点名称", width = 15)
    @ApiModelProperty(value = "报警点名称")
    private java.lang.String assertName;
	/**寄存器地址*/
	@Excel(name = "寄存器地址", width = 15)
    @ApiModelProperty(value = "寄存器地址")
    private java.lang.String address;
	/**指令*/
	@Excel(name = "指令", width = 15)
    @ApiModelProperty(value = "指令")
    private java.lang.String instruct;
	/**报警设置数值*/
	@Excel(name = "报警设置数值", width = 15)
    @ApiModelProperty(value = "报警设置数值")
    private java.lang.String alarmValue;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
	@Dict(dicCode = "assert_status")
    private java.lang.String assertStatus;
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
