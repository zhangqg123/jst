package org.jeecg.modules.qwert.jd.controller;

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
import org.jeecg.modules.qwert.jd.entity.JdZutaiCat;
import org.jeecg.modules.qwert.jd.service.IJdZutaiCatService;

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
 * @Description: jd_zutai_cat
 * @Author: jeecg-boot
 * @Date:   2020-05-13
 * @Version: V1.0
 */
@Api(tags="jd_zutai_cat")
@RestController
@RequestMapping("/jd/jdZutaiCat")
@Slf4j
public class JdZutaiCatController extends JeecgController<JdZutaiCat, IJdZutaiCatService> {
	@Autowired
	private IJdZutaiCatService jdZutaiCatService;
	
	/**
	 * 分页列表查询
	 *
	 * @param jdZutaiCat
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "jd_zutai_cat-分页列表查询")
	@ApiOperation(value="jd_zutai_cat-分页列表查询", notes="jd_zutai_cat-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(JdZutaiCat jdZutaiCat,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<JdZutaiCat> queryWrapper = QueryGenerator.initQueryWrapper(jdZutaiCat, req.getParameterMap());
		Page<JdZutaiCat> page = new Page<JdZutaiCat>(pageNo, pageSize);
		IPage<JdZutaiCat> pageList = jdZutaiCatService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param jdZutaiCat
	 * @return
	 */
	@AutoLog(value = "jd_zutai_cat-添加")
	@ApiOperation(value="jd_zutai_cat-添加", notes="jd_zutai_cat-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody JdZutaiCat jdZutaiCat) {
		jdZutaiCatService.save(jdZutaiCat);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param jdZutaiCat
	 * @return
	 */
	@AutoLog(value = "jd_zutai_cat-编辑")
	@ApiOperation(value="jd_zutai_cat-编辑", notes="jd_zutai_cat-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody JdZutaiCat jdZutaiCat) {
		jdZutaiCatService.updateById(jdZutaiCat);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "jd_zutai_cat-通过id删除")
	@ApiOperation(value="jd_zutai_cat-通过id删除", notes="jd_zutai_cat-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		jdZutaiCatService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "jd_zutai_cat-批量删除")
	@ApiOperation(value="jd_zutai_cat-批量删除", notes="jd_zutai_cat-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.jdZutaiCatService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "jd_zutai_cat-通过id查询")
	@ApiOperation(value="jd_zutai_cat-通过id查询", notes="jd_zutai_cat-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		JdZutaiCat jdZutaiCat = jdZutaiCatService.getById(id);
		if(jdZutaiCat==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(jdZutaiCat);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param jdZutaiCat
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, JdZutaiCat jdZutaiCat) {
        return super.exportXls(request, jdZutaiCat, JdZutaiCat.class, "jd_zutai_cat");
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
        return super.importExcel(request, response, JdZutaiCat.class);
    }

}
