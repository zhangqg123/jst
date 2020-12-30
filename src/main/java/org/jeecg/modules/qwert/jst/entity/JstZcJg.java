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
 * @Description: jst_zc_jg
 * @Author: jeecg-boot
 * @Date:   2020-12-29
 * @Version: V1.0
 */
@Data
@TableName("jst_zc_jg")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="jst_zc_jg对象", description="jst_zc_jg")
public class JstZcJg implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**机柜模型编号*/
	@Excel(name = "机柜模型编号", width = 15)
    @ApiModelProperty(value = "机柜模型编号")
    private java.lang.String jgNo;
	/**列头柜出线编号*/
	@Excel(name = "列头柜出线编号", width = 15)
    @ApiModelProperty(value = "列头柜出线编号")
    private java.lang.String lietouNo;
	/**微环境温度编号*/
	@Excel(name = "微环境温度编号", width = 15)
    @ApiModelProperty(value = "微环境温度编号")
    private java.lang.String whNo;
	/**列头柜数据点编号*/
	@Excel(name = "列头柜数据点编号", width = 15)
    @ApiModelProperty(value = "列头柜数据点编号")
    private java.lang.String lietouTarget;
	/**微环境数据点*/
	@Excel(name = "微环境数据点", width = 15)
    @ApiModelProperty(value = "微环境数据点")
    private java.lang.String whTarget;
	/**有功电度*/
	@Excel(name = "有功电度", width = 15)
    @ApiModelProperty(value = "有功电度")
    private java.lang.String lietouYgdd;
	/**无功电度*/
	@Excel(name = "无功电度", width = 15)
    @ApiModelProperty(value = "无功电度")
    private java.lang.String lietouWgdd;
	/**有功功率*/
	@Excel(name = "有功功率", width = 15)
    @ApiModelProperty(value = "有功功率")
    private java.lang.String lietouYggl;
	/**无功功率*/
	@Excel(name = "无功功率", width = 15)
    @ApiModelProperty(value = "无功功率")
    private java.lang.String lietouWggl;
	/**功率因数*/
	@Excel(name = "功率因数", width = 15)
    @ApiModelProperty(value = "功率因数")
    private java.lang.String lietouGlys;
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
