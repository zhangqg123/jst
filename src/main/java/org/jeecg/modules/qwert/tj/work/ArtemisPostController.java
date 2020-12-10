package org.jeecg.modules.qwert.tj.work;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.dbserver.mongo.common.enums.Gender;
import org.jeecg.modules.dbserver.mongo.common.model.User;
import org.jeecg.modules.dbserver.mongo.repository.impl.DemoRepository;
import org.jeecg.modules.qwert.tj.entity.TjDevice;
import org.jeecg.modules.qwert.tj.entity.TjSensor;
import org.jeecg.modules.qwert.tj.service.ITjDeviceService;
import org.jeecg.modules.qwert.tj.service.ITjSensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/work/thingjs/artemis")
@Slf4j
public class ArtemisPostController {
	@Autowired
	private ITjSensorService tjSensorService;
	@Autowired
	private ITjDeviceService tjDeviceService;

	/**
	 * 请根据自己的appKey和appSecret更换static静态块中的三个参数. [1 host]
	 * 如果你选择的是和现场环境对接,host要修改为现场环境的ip,https端口默认为443，http端口默认为80.例如10.33.25.22:443 或者10.33.25.22:80
	 * appKey和appSecret请按照或得到的appKey和appSecret更改.
	 * TODO 调用前先要清楚接口传入的是什么，是传入json就用doPostStringArtemis方法，下载图片doPostStringImgArtemis方法
	 */
	static {
		ArtemisConfig.host = "46.1.153.12";// 代理API网关nginx服务器ip端口
		ArtemisConfig.appKey = "28837806";// 秘钥appkey
		ArtemisConfig.appSecret = "eckdic0Y9kYzg9FexA7d";// 秘钥appSecret
	}
	/**
	 * 能力开放平台的网站路径
	 * TODO 路径不用修改，就是/artemis
	 */
	private static final String ARTEMIS_PATH = "/artemis";

    @Autowired
    DemoRepository repository;

    @GetMapping(value = "/hello")
    public void hello() {
        User user = new User();
        user.setId("1");
        user.setName("Make2");
        user.setAge(22);
        user.setGender(Gender.MALE);

        repository.save(user);    	
    }
	/**
	 * 调用POST请求类型(application/json)接口，这里以入侵报警事件日志为例
	 * https://open.hikvision.com/docs/918519baf9904844a2b608e558b21bb6#e6798840
	 *
	 * @return
	 */
	@GetMapping(value = "/cameraList")
	public Result<String> callPostStringApi(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		String vid=request.getParameter("vid");
		if(vid==null || vid.indexOf("VIDEO")==-1) {
			return null;
		}
		TjSensor tjSensor = new TjSensor();
		tjSensor.setSensordevice(vid);
		QueryWrapper<TjSensor> queryWrapper = QueryGenerator.initQueryWrapper(tjSensor, request.getParameterMap());
		Page<TjSensor> page = new Page<TjSensor>(1,2);
		IPage<TjSensor> pageList = tjSensorService.page(page, queryWrapper);
		List<TjSensor> ts = pageList.getRecords();
		String vn = ts.get(0).getSensorno();

		Result<String> result = new Result<String>();
		/**
		 * http://10.33.47.50/artemis/api/scpms/v1/eventLogs/searches
		 * 根据API文档可以看出来，这是一个POST请求的Rest接口，而且传入的参数值为一个json
		 * ArtemisHttpUtil工具类提供了doPostStringArtemis这个函数，一共六个参数在文档里写明其中的意思，因为接口是https，
		 * 所以第一个参数path是一个hashmap类型，请put一个key-value，query为传入的参数，body为传入的json数据
		 * 传入的contentType为application/json，accept不指定为null
		 * header没有额外参数可不传,指定为null
		 *
		 */
//		final String getCamsApi = ARTEMIS_PATH +"/api/scpms/v1/eventLogs/searches";
//		final String getCamsApi = ARTEMIS_PATH +"/api/acs/v1/door/events";
//		final String getCamsApi = ARTEMIS_PATH +"/api/resource/v1/acsDevice/acsDeviceList";
//		final String getCamsApi = ARTEMIS_PATH +"/api/resource/v1/encodeDevice/get";
		final String getCamsApi = ARTEMIS_PATH +"/api/video/v1/cameras/previewURLs";
		
		Map<String, String> path = new HashMap<String, String>(2) {
			{
				put("https://", getCamsApi);//根据现场环境部署确认是http还是https
			}
		};

		JSONObject jsonBody = new JSONObject();

/* 
 * 获取视频流
 */
//		jsonBody.put("cameraIndexCode","8c83212948a2449da9b21d4d769e7223");
		jsonBody.put("cameraIndexCode",vn);
		jsonBody.put("streamType", 0);
		jsonBody.put("protocol","rtmp");
		jsonBody.put("transmode", 1);	
		
		String body = jsonBody.toJSONString();

//		String res =ArtemisHttpUtil.doPostStringArtemis(path,body,null,null,"application/json",null);// post请求application/json类型参数
		String res = "{\"code\":\"0\",\"msg\":\"success\",\"data\":{\"url\":\"rtmp://58.200.131.2:1935/livetv/hunantv\"}}";
		JSONObject rowData = JSONObject.parseObject(res);
		JSONObject r1 = (JSONObject) rowData.get("data");
		String r2 = (String) r1.get("url");
		result.setResult(r2);
		result.setSuccess(true);
		return result;
	}
	
	@GetMapping(value = "/gateList")
	public Result<String> callPostGateApi(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");

		TjDevice tjDevice = new TjDevice();
		tjDevice.setDevicemodel("DOOR");
		QueryWrapper<TjDevice> queryWrapper = QueryGenerator.initQueryWrapper(tjDevice, request.getParameterMap());
		Page<TjDevice> page = new Page<TjDevice>(1, 50);
		IPage<TjDevice> pageList = tjDeviceService.page(page, queryWrapper);
		List<TjDevice> td = pageList.getRecords();

		Result<String> result = new Result<String>();
		final String getCamsApi = ARTEMIS_PATH +"/api/resource/v1/acsDoor/advance/acsDoorList";
		final String getCamsApi2 = ARTEMIS_PATH +"/api/acs/v1/door/states";
		
		Map<String, String> path = new HashMap<String, String>(2) {
			{
				put("https://", getCamsApi);//根据现场环境部署确认是http还是https
			}
		};
		Map<String, String> path2 = new HashMap<String, String>(2) {
			{
				put("https://", getCamsApi2);//根据现场环境部署确认是http还是https
			}
		};

		JSONObject jsonBody = new JSONObject();
		JSONObject jsonBody2 = new JSONObject();
		jsonBody.put("pageNo", 1);
		jsonBody.put("pageSize", 100);
		
		String body = jsonBody.toJSONString();

//		String result2 =ArtemisHttpUtil.doPostStringArtemis(path,body,null,null,"application/json",null);// post请求application/json类型参数
		String res =ArtemisHttpUtil.doPostStringArtemis(path,body,null,null,"application/json",null);// post请求application/json类型参数
//		String res = "{\"code\":\"0\",\"msg\":\"success\",\"data\":{\"url\":\"rtmp://58.200.131.2:1935/livetv/hunantv\"}}";
		JSONObject rowData = JSONObject.parseObject(res);
		JSONObject r1 = (JSONObject) rowData.get("data");
		JSONArray r2 = (JSONArray) r1.get("list");
		String[] str = new String[r2.size()];
        for(int i=0;i<r2.size();i++) {
            String tr=(String) r2.getJSONObject(i).get("doorIndexCode");
    		str[i]=tr;
//    		System.out.println("doorName:"+(String) r2.getJSONObject(i).get("doorName")+"    doorIndexCode:"+(String) r2.getJSONObject(i).get("doorIndexCode"));
        }
	    jsonBody2.put("doorIndexCodes", str);
        
		String body2 = jsonBody2.toJSONString();
		String res2 =ArtemisHttpUtil.doPostStringArtemis(path2,body2,null,null,"application/json",null);// post请求application/json类型参数
		JSONObject rowData2 = JSONObject.parseObject(res2);
		JSONObject r3 = (JSONObject) rowData2.get("data");
		JSONArray r4 = (JSONArray) r3.get("authDoorList");
		for (int i = 0; i < td.size(); i++) {
            TjDevice tjd = td.get(i);
            for(int j=0;j<r4.size();j++) {
                String dic=(String) r4.getJSONObject(j).get("doorIndexCode");
                if(tjd.getDevicename().equals(dic)) {
                	int state=(Integer) r4.getJSONObject(j).get("doorState");
                	tjd.setDevicex(String.valueOf(state));
                	break;
                }
            }
        }
		JSONArray r5= JSONArray.parseArray(JSON.toJSONString(td));
		result.setResult(r5.toString());
		result.setSuccess(true);
		return result;
	}
	
	@GetMapping(value = "/gateId")
	public Result<String> callPostGateIdApi(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		String gid=request.getParameter("gid");
		if(gid==null || gid.indexOf("GATE")==-1) {
			return null;
		}
		TjDevice tjDevice = new TjDevice();
		tjDevice.setDeviceno(gid);
		QueryWrapper<TjDevice> queryWrapper = QueryGenerator.initQueryWrapper(tjDevice, request.getParameterMap());
		Page<TjDevice> page = new Page<TjDevice>(1, 1);
		IPage<TjDevice> pageList = tjDeviceService.page(page, queryWrapper);
		List<TjDevice> td = pageList.getRecords();
		
		Result<String> result = new Result<String>();
		final String getCamsApi = ARTEMIS_PATH +"/api/acs/v1/door/events";
		
		Map<String, String> path = new HashMap<String, String>(2) {
			{
				put("https://", getCamsApi);//根据现场环境部署确认是http还是https
			}
		};

		JSONObject jsonBody = new JSONObject();
		String[] str = new String[]{td.get(0).getDevicename()};
	    jsonBody.put("doorIndexCodes", str);
	    
//		"doorIndexCode":"8d59cbe1ddcb4fc5a970d30526c30239"
		jsonBody.put("startTime", "2020-03-22T12:00:00+08:00");
		jsonBody.put("endTime", "2020-04-22T17:00:00+08:00");
		jsonBody.put("pageNo", 1);
		jsonBody.put("pageSize", 100);
		
		String body = jsonBody.toJSONString();

		String res =ArtemisHttpUtil.doPostStringArtemis(path,body,null,null,"application/json",null);// post请求application/json类型参数

		JSONObject rowData = JSONObject.parseObject(res);
		JSONObject r1 = (JSONObject) rowData.get("data");
		JSONArray r2 = (JSONArray) r1.get("list");
//		JSONArray rr=new JSONArray();		
/*		for(int i=0;i<r2.size();i++) {
			JSONObject r3 = (JSONObject) r2.get(i);
			JSONObject r4=new JSONObject();
			r4.put("personName",r3.getString("personName"));
			r4.put("cardNo",r3.getString("cardNo"));
			rr.add(r4);
		}
*/
		result.setResult(r2.toString());
		result.setSuccess(true);
		return result;
	}
	
}
