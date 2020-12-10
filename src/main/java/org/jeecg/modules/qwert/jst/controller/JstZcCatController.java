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
import org.jeecg.modules.qwert.jst.entity.JstZcCat;
import org.jeecg.modules.qwert.jst.service.IJstZcCatService;

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

 /**
 * @Description: jst_zc_cat
 * @Author: jeecg-boot
 * @Date:   2020-07-19
 * @Version: V1.0
 */
@RestController
@RequestMapping("/jst/jstZcCat")
@Slf4j
public class JstZcCatController extends JeecgController<JstZcCat, IJstZcCatService>{
	@Autowired
	private IJstZcCatService jstZcCatService;
	
	/**
	 * 分页列表查询
	 *
	 * @param jstZcCat
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/rootList")
	public Result<?> queryPageList(JstZcCat jstZcCat,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String parentId = jstZcCat.getPid();
		if (oConvertUtils.isEmpty(parentId)) {
			parentId = "0";
		}
		jstZcCat.setPid(null);
		QueryWrapper<JstZcCat> queryWrapper = QueryGenerator.initQueryWrapper(jstZcCat, req.getParameterMap());
		// 使用 eq 防止模糊查询
		queryWrapper.eq("pid", parentId);
		Page<JstZcCat> page = new Page<JstZcCat>(pageNo, pageSize);
		IPage<JstZcCat> pageList = jstZcCatService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	 /**
      * 获取子数据
      * @param testTree
      * @param req
      * @return
      */
	@GetMapping(value = "/childList")
	public Result<?> queryPageList(JstZcCat jstZcCat,HttpServletRequest req) {
		QueryWrapper<JstZcCat> queryWrapper = QueryGenerator.initQueryWrapper(jstZcCat, req.getParameterMap());
		List<JstZcCat> list = jstZcCatService.list(queryWrapper);
		return Result.ok(list);
	}
	
	
	/**
	 *   添加
	 *
	 * @param jstZcCat
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody JstZcCat jstZcCat) {
		jstZcCatService.addJstZcCat(jstZcCat);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param jstZcCat
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody JstZcCat jstZcCat) {
		jstZcCatService.updateJstZcCat(jstZcCat);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		jstZcCatService.deleteJstZcCat(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.jstZcCatService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		JstZcCat jstZcCat = jstZcCatService.getById(id);
		if(jstZcCat==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(jstZcCat);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param jstZcCat
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, JstZcCat jstZcCat) {
		return super.exportXls(request, jstZcCat, JstZcCat.class, "jst_zc_cat");
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
		return super.importExcel(request, response, JstZcCat.class);
    }

}
