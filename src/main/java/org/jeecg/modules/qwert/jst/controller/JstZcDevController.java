package org.jeecg.modules.qwert.jst.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fusesource.stomp.jms.StompJmsConnectionFactory;
import org.fusesource.stomp.jms.StompJmsDestination;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.conn.modbus4j.ModbusUtil;
import org.jeecg.modules.conn.snmp.SnmpData;
import org.jeecg.modules.conn.snmp.SnmpTest;
import org.jeecg.modules.dbserver.mongo.common.model.Alarm;
import org.jeecg.modules.dbserver.mongo.common.model.Audit;
import org.jeecg.modules.dbserver.mongo.repository.impl.DemoRepository;
import org.jeecg.modules.qwert.jst.entity.JstZcAlarm;
import org.jeecg.modules.qwert.jst.entity.JstZcCat;
import org.jeecg.modules.qwert.jst.entity.JstZcConfig;
import org.jeecg.modules.qwert.jst.entity.JstZcDev;
import org.jeecg.modules.qwert.jst.entity.JstZcRev;
import org.jeecg.modules.qwert.jst.entity.JstZcTarget;
import org.jeecg.modules.qwert.jst.service.IJstZcAlarmService;
import org.jeecg.modules.qwert.jst.service.IJstZcCatService;
import org.jeecg.modules.qwert.jst.service.IJstZcConfigService;
import org.jeecg.modules.qwert.jst.service.IJstZcDevService;
import org.jeecg.modules.qwert.jst.service.IJstZcTargetService;
import org.jeecg.modules.qwert.jst.utils.JstConstant;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.serotonin.modbus4j.msg.ReadDiscreteInputsRequest;
import com.serotonin.modbus4j.msg.ReadDiscreteInputsResponse;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersResponse;
import com.serotonin.modbus4j.msg.ReadInputRegistersRequest;
import com.serotonin.modbus4j.msg.ReadInputRegistersResponse;

import lombok.experimental.var;
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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: jst_zc_dev
 * @Author: jeecg-boot
 * @Date: 2020-07-24
 * @Version: V1.0
 */
@Api(tags = "jst_zc_dev")
@RestController
@RequestMapping("/jst/jstZcDev")
@Slf4j
public class JstZcDevController extends JeecgController<JstZcDev, IJstZcDevService> {
	@Autowired
	private IJstZcCatService jstZcCatService;
	@Autowired
	private IJstZcDevService jstZcDevService;
	@Autowired
	private IJstZcTargetService jstZcTargetService;
	@Autowired
	private IJstZcAlarmService jstZcAlarmService;
	@Autowired
	private IJstZcConfigService jstZcConfigService;
    @Autowired
    DemoRepository repository;
    
    private List<JstZcCat> jzcList;
    private List<JstZcDev> jzdList;    
    private List<JstZcTarget> jztList;
//	private boolean runflag = true;

	/**
	 * 分页列表查询
	 *
	 * @param jstZcDev
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "jst_zc_dev-分页列表查询")
	@ApiOperation(value = "jst_zc_dev-分页列表查询", notes = "jst_zc_dev-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(JstZcDev jstZcDev, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
		Map<String, String[]> aaa = req.getParameterMap();
		QueryWrapper<JstZcDev> queryWrapper = QueryGenerator.initQueryWrapper(jstZcDev, req.getParameterMap());
		queryWrapper.orderByAsc("dev_cat");
		queryWrapper.orderByAsc("dev_no");
		Page<JstZcDev> page = new Page<JstZcDev>(pageNo, pageSize);
		IPage<JstZcDev> pageList = jstZcDevService.page(page, queryWrapper);
		return Result.ok(pageList,JstConstant.runflag);
	}

	/**
	 * 添加
	 *
	 * @param jstZcDev
	 * @return
	 */
	@AutoLog(value = "jst_zc_dev-添加")
	@ApiOperation(value = "jst_zc_dev-添加", notes = "jst_zc_dev-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody JstZcDev jstZcDev) {
		jstZcDevService.save(jstZcDev);
		return Result.ok("添加成功！");
	}

	/**
	 * 测试
	 *
	 * @param jstZcDev
	 * @return
	 * @throws InterruptedException 
	 */
//	@AutoLog(value = "jst_zc_dev-测试")
	@ApiOperation(value = "jst_zc_dev-测试", notes = "jst_zc_dev-测试")
	@PostMapping(value = "/conntest")
	public Result<?> conntest(@RequestBody JstZcRev jstZcRev) throws InterruptedException {
		List<JstZcConfig> jzConList = jstZcConfigService.list();
		
		for (int i=0;i<jzConList.size();i++) {
			JstZcConfig jc = jzConList.get(i);
			if(jc.getConfigNo().equals("debugflag")) {
				JstConstant.debugflag=Integer.parseInt(jc.getConfigValue());
			}
			if(jc.getConfigNo().equals("sleeptime")) {
				JstConstant.sleeptime=Integer.parseInt(jc.getConfigValue());
			}
		}
		long start, end;
		start = System.currentTimeMillis();

		String devNo=jstZcRev.getDevNo();
		String conInfo = jstZcRev.getConnInfo();
		String revList = jstZcRev.getRevList();
		List<JstZcTarget> jztList = null;
		List resList = new ArrayList();
		if (jstZcRev.getDevType() != null) {
			JstZcTarget jstZcTarget = new JstZcTarget();
			jstZcTarget.setDevType(jstZcRev.getDevType());
//			jstZcTarget.setCtrlDown("");
			QueryWrapper<JstZcTarget> queryWrapper = QueryGenerator.initQueryWrapper(jstZcTarget, null);
			queryWrapper.orderByAsc("instruct");
			queryWrapper.orderByAsc("tmp");
			jztList = jstZcTargetService.list(queryWrapper);
		} else {
			jztList = JSONArray.parseArray(revList, JstZcTarget.class);
		}
		JSONObject jsonConInfo = JSON.parseObject(conInfo);
		String ipAddress = jsonConInfo.getString("ipAddress");
		String port = jsonConInfo.getString("port");
		String type = jsonConInfo.getString("type");
		String retry = jsonConInfo.getString("retry");

		String slave = null;

		String version = null;
		String timeOut = null;
		String community = null;
		BatchResults<String> results = null;
		JstZcTarget jztl = jztList.get(jztList.size() - 1);
		boolean extype = (jztl.getDevType().equals("kangmingsipcc3300") || jstZcRev.getDevType() == null);
	//	boolean extype = (jztl.getDevType().equals("kangmingsipcc") || jstZcRev.getDevType() == null);
		if (type.equals("SOCKET")) {
			int ts = Integer.parseInt(jztl.getAddress());
			slave = jsonConInfo.getString("slave");
			timeOut = jsonConInfo.getString("sotimeout");

			IpParameters ipParameters = new IpParameters();
			ipParameters.setHost(ipAddress);
			ipParameters.setPort(Integer.parseInt(port));
			ipParameters.setEncapsulated(true);

			ModbusFactory modbusFactory = new ModbusFactory();
			ModbusMaster master = modbusFactory.createTcpMaster(ipParameters, false);

//			jztList.sort((x, y) -> Integer.compare(Integer.parseInt(x.getAddress()), Integer.parseInt(y.getAddress())));
			boolean flag = false;
			try {
//				if (ts > 200) {
//					master.setTimeout(1000);
//				}
				master.init();
				int slaveId = 0;
//				if (isNumeric(slave)) {
//					slaveId = Integer.parseInt(slave);
//				} else {
					BigInteger slavebigint = new BigInteger(slave, 16);
					slaveId = slavebigint.intValue();
//				}
				BatchRead<String> batch = new BatchRead<String>();
				String tmpInstruct = null;
				int tmpOffset = 0;
				int tmp2Offset = 0;
				boolean batchSend = false;
				int revnull=0;
				for (int i = 0; i < jztList.size(); i++) {
					JstZcTarget jzt = jztList.get(i);
					String targetNo = jzt.getTargetNo();
					String di = jzt.getInstruct().substring(0, 2);
					String oshexs = jzt.getInstruct().substring(2, 6);
					String lenhexs = jzt.getInstruct().substring(6, 10);
					String instruct = jzt.getInstruct();
					
					int offset = 0;
					int len = 0;

					if (jzt.getAddress() != null) {
						offset = offset + Integer.parseInt(jzt.getAddress());
					}
					if (instruct.equals(tmpInstruct) && offset==tmp2Offset) {
						continue;
					}
					if(extype) {
						if (tmpInstruct!=null && !instruct.equals(tmpInstruct)) {
							flag = true;
						}
					}else {
						if (tmpInstruct!=null && ts > 200 && offset - tmpOffset >= 80) {
							flag = true;
						}
					}
					tmpInstruct = instruct;
					tmp2Offset=offset;
					if (flag == true) {
				//		System.out.println(i + "::" + offset);
						results = master.send(batch);
						Thread.sleep(JstConstant.sleeptime);
						if(results.toString().equals("{}")) {
						}else {
							resList.add(results.toString());
						}
						if(JstConstant.debugflag==1) {
							System.out.println(results);
						}
						batch = new BatchRead<String>();
						flag = false;
						tmpOffset = offset;
					}
					
					String res = null;
					Map<String, String> resMap = new HashMap<String, String>();

					if (di.equals("04")) {
							batch.addLocator(jzt.getId(),
									BaseLocator.inputRegister(slaveId, offset, Integer.parseInt(jzt.getDataType())));
							batchSend = true;
					}
					if (di.equals("03")) {
							batch.addLocator(jzt.getId(),
									BaseLocator.holdingRegister(slaveId, offset, Integer.parseInt(jzt.getDataType())));
							batchSend = true;
					}
					if (di.equals("02")) {
						if (extype) {
							ModbusUtil.readDiscreteInputTest(master, slaveId, offset, len);
						} else {
							batch.addLocator(jzt.getId(), BaseLocator.inputStatus(slaveId, offset));
							batchSend = true;
						}
					}
					Thread.sleep(JstConstant.sleeptime/2);
				}
				if (batchSend == true) {
					results = master.send(batch);
					Thread.sleep(JstConstant.sleeptime);
					resList.add(results.toString());
//					System.out.println(devNo+"::"+results);
				}
				if(JstConstant.debugflag==1) {
					System.out.println(devNo+"::"+results);
				}				
			} catch (ModbusInitException e) {
				e.printStackTrace();
			} catch (ModbusTransportException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ErrorResponseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				master.destroy();
			}
		}
		if (type.equals("SNMP")) {
			version = jsonConInfo.getString("version");
			timeOut = jsonConInfo.getString("timeOut");
			community = jsonConInfo.getString("community");
			jztList.stream().sorted(Comparator.comparing(JstZcTarget::getInstruct));
			List<String> oidList = new ArrayList<String>();
			for (int i = 0; i < jztList.size(); i++) {
				JstZcTarget jzt = jztList.get(i);
				String oidval = jzt.getInstruct();
	//			System.out.println(devNo+"::");
				List snmpList = SnmpData.snmpGet(ipAddress, community, oidval,null);
				if(snmpList.size()>0) {
					for(int j=0;j<snmpList.size();j++) {
						resList.add(snmpList.get(j));
					}
				}else {
					break;
				}
			}
		}
		end = System.currentTimeMillis();
		System.out.println("开始时间:" + start + "; 结束时间:" + end + "; 用时:" + (end - start) + "(ms)");
		return Result.ok(resList);
	}
	
//	@AutoLog(value = "jst_zc_dev-读取")
	@ApiOperation(value = "jst_zc_dev-读取", notes = "jst_zc_dev-读取")
	@GetMapping(value = "/readClose")
	public Result<?> readClose(HttpServletRequest req) {
		JstConstant.runflag=false;
		JstConstant.runall=false;
		return Result.ok("ok",false);
	}
	
//	@AutoLog(value = "jst_zc_dev-读取")
	@ApiOperation(value = "jst_zc_dev-读取", notes = "jst_zc_dev-读取")
	@GetMapping(value = "/handleRead")
	public Result<?> handleRead(HttpServletRequest req) {
		String catNo = req.getParameter("devCat");
		if(catNo==null) {
			return Result.ok("choose category");
		}
		JstConstant.runflag=true;
		if(catNo.equals("all") && JstConstant.runall==true) {
			return Result.ok("reading all",true);
		}else {
			String hr = jstZcDevService.handleRead(catNo);
		}
		return Result.ok("ok",true);
	}
	
	@AutoLog(value = "jst_zc_dev-队列")
	@ApiOperation(value = "jst_zc_dev-队列", notes = "jst_zc_dev-队列")
	@GetMapping(value = "/readAmq")
	public Result<?> readAmq(HttpServletRequest req) {
        StompJmsConnectionFactory factory = new StompJmsConnectionFactory();
        factory.setBrokerURI("tcp://" + JstConstant.host + ":" + JstConstant.port);

        Connection connection;
		try {
			connection = factory.createConnection(JstConstant.user, JstConstant.password);
	        connection.start();
	        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	        Destination dest = new StompJmsDestination(JstConstant.destination);
	        MessageProducer producer = session.createProducer(dest);
	        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			log.info(String.format(" Jeecg-Boot 普通定时任务 SampleJob !  时间:" + DateUtils.getTimestamp()));
			List<Audit> auditList = repository.findAllAudit();
			for(int i=0;i<auditList.size();i++) {
				Audit audit = auditList.get(i);
	            TextMessage msg = session.createTextMessage(audit.getAuditValue());
	            msg.setIntProperty("id", i);
	            producer.send(msg);
  //              System.out.println(String.format("Sent %d messages", i));
				
			}
	        connection.close();

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Result.ok("ok");
	}
    private static String env(String key, String defaultValue) {
        String rc = System.getenv(key);
        if( rc== null )
            return defaultValue;
        return rc;
    }


	public boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
			System.out.println(str.charAt(i));
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}


	/**
	 * 编辑
	 *
	 * @param jstZcDev
	 * @return
	 */
	@AutoLog(value = "jst_zc_dev-编辑")
	@ApiOperation(value = "jst_zc_dev-编辑", notes = "jst_zc_dev-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody JstZcDev jstZcDev) {
		jstZcDevService.updateById(jstZcDev);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "jst_zc_dev-通过id删除")
	@ApiOperation(value = "jst_zc_dev-通过id删除", notes = "jst_zc_dev-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
		jstZcDevService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "jst_zc_dev-批量删除")
	@ApiOperation(value = "jst_zc_dev-批量删除", notes = "jst_zc_dev-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		this.jstZcDevService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "jst_zc_dev-通过id查询")
	@ApiOperation(value = "jst_zc_dev-通过id查询", notes = "jst_zc_dev-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
		JstZcDev jstZcDev = jstZcDevService.getById(id);
		if (jstZcDev == null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(jstZcDev);
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param jstZcDev
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, JstZcDev jstZcDev) {
		return super.exportXls(request, jstZcDev, JstZcDev.class, "jst_zc_dev");
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
		return super.importExcel(request, response, JstZcDev.class);
	}

}
