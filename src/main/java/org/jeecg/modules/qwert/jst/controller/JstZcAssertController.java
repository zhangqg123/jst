package org.jeecg.modules.qwert.jst.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.qwert.jst.entity.JstZcAssert;
import org.jeecg.modules.qwert.jst.service.IJstZcAssertService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: jst_zc_assert
 * @Author: jeecg-boot
 * @Date:   2020-09-06
 * @Version: V1.0
 */
@Api(tags="jst_zc_assert")
@RestController
@RequestMapping("/jst/jstZcAssert")
@Slf4j
public class JstZcAssertController extends JeecgController<JstZcAssert, IJstZcAssertService> {
	@Autowired
	private IJstZcAssertService jstZcAssertService;
	
	/**
	 * 分页列表查询
	 *
	 * @param jstZcAssert
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "jst_zc_assert-分页列表查询")
	@ApiOperation(value="jst_zc_assert-分页列表查询", notes="jst_zc_assert-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(JstZcAssert jstZcAssert,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<JstZcAssert> queryWrapper = QueryGenerator.initQueryWrapper(jstZcAssert, req.getParameterMap());
		queryWrapper.eq("assert_status","1");
		queryWrapper.orderByAsc("assert_cat");
		queryWrapper.orderByAsc("instruct");
		queryWrapper.orderByAsc("address");
		Page<JstZcAssert> page = new Page<JstZcAssert>(pageNo, pageSize);
		IPage<JstZcAssert> pageList = jstZcAssertService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param jstZcAssert
	 * @return
	 */
	@AutoLog(value = "jst_zc_assert-添加")
	@ApiOperation(value="jst_zc_assert-添加", notes="jst_zc_assert-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody JstZcAssert jstZcAssert) {
		jstZcAssertService.save(jstZcAssert);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param jstZcAssert
	 * @return
	 */
	@AutoLog(value = "jst_zc_assert-编辑")
	@ApiOperation(value="jst_zc_assert-编辑", notes="jst_zc_assert-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody JstZcAssert jstZcAssert) {
		jstZcAssertService.updateById(jstZcAssert);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "jst_zc_assert-通过id删除")
	@ApiOperation(value="jst_zc_assert-通过id删除", notes="jst_zc_assert-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		jstZcAssertService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "jst_zc_assert-批量删除")
	@ApiOperation(value="jst_zc_assert-批量删除", notes="jst_zc_assert-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.jstZcAssertService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "jst_zc_assert-通过id查询")
	@ApiOperation(value="jst_zc_assert-通过id查询", notes="jst_zc_assert-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		JstZcAssert jstZcAssert = jstZcAssertService.getById(id);
		if(jstZcAssert==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(jstZcAssert);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param jstZcAssert
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, JstZcAssert jstZcAssert) {
        return super.exportXls(request, jstZcAssert, JstZcAssert.class, "jst_zc_assert");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, JstZcAssert.class);
    }

}
