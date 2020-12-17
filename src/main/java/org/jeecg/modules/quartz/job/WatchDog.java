package org.jeecg.modules.quartz.job;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.conn.modbus4j.ModbusUtil;
import org.jeecg.modules.conn.snmp.SnmpData;
import org.jeecg.modules.dbserver.mongo.common.model.Alarm;
import org.jeecg.modules.dbserver.mongo.common.model.Audit;
import org.jeecg.modules.qwert.jst.entity.JstZcAlarm;
import org.jeecg.modules.qwert.jst.entity.JstZcCat;
import org.jeecg.modules.qwert.jst.entity.JstZcDev;
import org.jeecg.modules.qwert.jst.entity.JstZcTarget;
import org.jeecg.modules.qwert.jst.service.IJstZcAlarmService;
import org.jeecg.modules.qwert.jst.service.IJstZcCatService;
import org.jeecg.modules.qwert.jst.service.IJstZcDevService;
import org.jeecg.modules.qwert.jst.service.IJstZcTargetService;
import org.jeecg.modules.qwert.jst.utils.JstConstant;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;

import lombok.extern.slf4j.Slf4j;

/**
 * 示例带参定时任务
 * 
 * @Author Scott
 */
@Slf4j
public class WatchDog implements Job {

	/**
	 * 若参数变量名修改 QuartzJobController中也需对应修改
	 */
	private String parameter;
	@Autowired
	private IJstZcCatService jstZcCatService;
	@Autowired
	private IJstZcDevService jstZcDevService;
	@Autowired
	private IJstZcTargetService jstZcTargetService;
	@Autowired
	private IJstZcAlarmService jstZcAlarmService;
	private List<JstZcCat> jzcList;
	private List<JstZcDev> jzdList;
	private List<JstZcTarget> jztList;

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		if(JstConstant.watchdog==true) {
			return;
		}
		JstConstant.watchdog=true;
		jzcList = jstZcCatService.queryJzcList();
		jzdList = jstZcDevService.queryJzdList();
		jztList = jstZcTargetService.queryJztList();			
		List<JstZcAlarm> jzaList = jstZcAlarmService.queryJzaList("0");
		log.info(String.format("jzc %d,jzd %d,jzt %d ,jza %d !   时间:" + DateUtils.now(), jzcList.size(),jzdList.size(),jztList.size(),jzaList.size()));
		
		for(int i=0;i<jzaList.size();i++) {
			JstZcAlarm jza = jzaList.get(i);
			if(jza.getSendType().contentEquals("2")) {
				continue;
			}
			String devNo = jza.getDevNo();
//			String targetNos = jza.getTargetNo();
			String catNo = jza.getCatNo();
			JstZcDev jstZcDev = jzdList.stream().filter(o -> o.getDevNo().equals(devNo)).findAny().orElse(null);
			List<JstZcTarget> jztCollect = jztList.stream().filter(u -> catNo.equals(u.getDevType())).collect(Collectors.toList());
			targetAudit(jza,jstZcDev,jztCollect);
		}
		JstConstant.watchdog=false;
	}
	
	public Result<?> targetAudit(JstZcAlarm jza,JstZcDev jzd,List<JstZcTarget> jztCollect) {
		List resList = new ArrayList();
		String devNo=jzd.getDevNo();
		String devName=jzd.getDevName();
		String catNo = jzd.getDevCat();
		String conInfo = jzd.getConInfo();
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
		JstZcTarget jztl = jztCollect.get(jztCollect.size() - 1);
	//	boolean extype = (jzd.getDevNo().equals("kangminbgsiPC330"));
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

			boolean flag = false;
			try {
				if (ts > 200) {
					master.setTimeout(3000);
				}
				master.init();
				int slaveId = 0;
				BigInteger slavebigint = new BigInteger(slave, 16);
				slaveId = slavebigint.intValue();
				BatchRead<String> batch = new BatchRead<String>();
				
				if(jza.getAlarmValue().equals("connection-fail")&&jztCollect.size()>0) {

					for (int j = 0; j < jztCollect.size(); j++) {
						JstZcTarget jzt = jztCollect.get(j);

						if(jza.getTargetNo().indexOf(jzt.getId())==-1) {
							continue;
						}

						String di = jzt.getInstruct().substring(0, 2);
						int offset = 0;

						if (jzt.getAddress() != null) {
							offset = offset + Integer.parseInt(jzt.getAddress());
						}
						Map<String, String> resMap = new HashMap<String, String>();
						if (di.equals("04")) {
							batch.addLocator(jzt.getId(), BaseLocator.inputRegister(slaveId, offset,
									Integer.parseInt(jzt.getDataType())));
						}
						if (di.equals("03")) {
							batch.addLocator(jzt.getId(), BaseLocator.holdingRegister(slaveId, offset,
									Integer.parseInt(jzt.getDataType())));
						}
						if (di.equals("02")) {
							batch.addLocator(jzt.getId(), BaseLocator.inputStatus(slaveId, offset));
						}

					}
					
					results = master.send(batch);
					Thread.sleep(100);
					JstZcAlarm jstZcAlarm = new JstZcAlarm();
					jstZcAlarm.setId(jza.getId());
					if(results.toString().equals("{}")) {
						jstZcAlarm.setAlarmValue("connection-fail");
						jstZcAlarm.setTargetNo(jza.getTargetNo());
						jstZcAlarm.setSendTime(new Date());
						if(jza.getSendType().equals("0")) {
							jstZcAlarm.setSendType("1");
							jstZcAlarmService.updateSys(jstZcAlarm);
						}else {
							if(jza.getSendType().equals("1")) {
								jstZcAlarm.setSendType("2");
								jstZcAlarmService.updateSys(jstZcAlarm);
							}
						}
						jstZcAlarmService.updateSys(jstZcAlarm);
	//					System.out.println(devNo+"::通讯中断,发出报警:::watchdog");
					}else {
						jstZcAlarm.setDevNo(devNo);
						jstZcAlarm.setCatNo(catNo);
						jstZcAlarm.setTargetNo(jza.getTargetNo());
						jstZcAlarm.setAlarmValue("recovery");
						jstZcAlarm.setSendTime(new Date());
						jstZcAlarm.setSendType("-2");
						jstZcAlarmService.updateSys(jstZcAlarm);									

	//					jstZcAlarmService.deleteSys(jza.getId());									
						resList.add(results.toString());
					}
/*					String alarm=null;
					if(resList.size()>0) {
						alarm=trackAlarm(jztCollect, resList);
					}
					if(alarm!=null) {
						String[] tmpAlarm = alarm.split("::");
						String alarmNo=tmpAlarm[0];
						String alarmValue=tmpAlarm[1];
						jstZcAlarm = new JstZcAlarm();
						if(alarmValue.length()>0) {
							jstZcAlarm.setDevNo(devNo);
							jstZcAlarm.setDevName(devName);
							jstZcAlarm.setCatNo(catNo);
							jstZcAlarm.setTargetNo(alarmNo);
							jstZcAlarm.setAlarmValue(alarmValue);
							jstZcAlarm.setSendTime(new Date());
							jstZcAlarm.setSendType("2");
							jstZcAlarmService.saveSys(jstZcAlarm);
						}
					}
*/
					Thread.sleep(500);
			    }
			} catch (ModbusInitException e) {
				e.printStackTrace();
			} catch (ModbusTransportException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ErrorResponseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (Exception e)	            {
				e.printStackTrace();
            } finally {
				master.destroy();
			}
		}
		
		if (type.equals("SNMP")) {
			version = jsonConInfo.getString("version");
			timeOut = jsonConInfo.getString("timeOut");
			community = jsonConInfo.getString("community");
			jztCollect.stream().sorted(Comparator.comparing(JstZcTarget::getInstruct));
			List<String> oidList = new ArrayList<String>();
//			System.out.println(devNo+"::");
			for (int j = 0; j < jztCollect.size(); j++) {
				JstZcTarget jzt = jztCollect.get(j);
				String oidval = jzt.getInstruct();
				List snmpList = SnmpData.snmpGet(ipAddress, community, oidval,null);
				
				if(snmpList.size()>0) {
					for(int n=0;n<snmpList.size();n++) {
						resList.add(snmpList.get(n));
					}
				}else {
					break;
				}
				
			}
		}
	
		return Result.ok("验证结束");
	}
	public String trackAlarm(List<JstZcTarget> jztCollect, List resList) {
		String alarmValue="";
		String alarmNo="";
		for(int ri=0;ri<resList.size();ri++) {
			String r1=(String) resList.get(ri);
			r1=r1.replaceAll(" ", "");
			r1=r1.substring(1, r1.length()-1);
			String[] r2 = r1.split(",");
			for(int rj=0;rj<r2.length;rj++) {
				String[] r3 = r2[rj].split("=");
				for(int rk=0;rk<jztCollect.size();rk++) {
					JstZcTarget jzt = jztCollect.get(rk);
					if(jzt.getAlarmPoint().equals("1")&&r3[0].equals(jzt.getId())) {
						if(jzt.getInfoType().equals("状态量")){
							if(jzt.getInterceptBit()!=null&&jzt.getInterceptBit().indexOf("bitIndex")!=-1) {
								String tmpinstruct=jzt.getInstruct();
		                        String tmpaddress=jzt.getAddress();
		                        for(int n=0;n<100;n++){
		                            JstZcTarget item = jztCollect.get(rk+n);
		                            if(item.getInstruct()!=tmpinstruct||item.getAddress()!=tmpaddress ){
		                                break;
		                            };

		                       //     String aa = Integer.toBinaryString(Integer.parseInt(r3[1]));
		                       //     String a0=String.format("%16d", Integer.parseInt(aa)).replace(" ", "0");
		                            String binaryStr = Integer.toBinaryString(Integer.parseInt(r3[1]));
		                            while(binaryStr.length() < 16){
		                                binaryStr = "0"+binaryStr;
		                            }
		                            
		                            String a1=item.getInterceptBit();
		                            String[] a2=a1.split(",");
		                            String[] a3=a2[0].split(":");
		                            int a4=Integer.parseInt(a3[1]);
		                            String a5 = binaryStr.substring(a4,a4+1);
		                            
									String bjz = item.getCtrlUp();
									if(bjz.indexOf("==")!=-1) {
										String[] bj = bjz.split("==");
										if(a5.equals(bj[1])) {
											alarmNo+=jzt.getId()+",";
											alarmValue+=jzt.getTargetName()+",";
										}
									}
									if(bjz.indexOf("!=")!=-1) {
										String[] bj = bjz.split("!=");
										if(!a5.equals(bj[1])) {
											alarmNo+=jzt.getId()+",";
											alarmValue+=jzt.getTargetName()+",";
										}
									}
		                        }
							}else {
								String bjz = jzt.getCtrlUp();
								if(bjz.indexOf("==")!=-1) {
									String[] bj = bjz.split("==");
									if(r3[1].equals(bj[1])) {
										alarmNo+=jzt.getId()+",";
										alarmValue+=jzt.getTargetName()+",";
									}
								}
								if(bjz.indexOf("!=")!=-1) {
									String[] bj = bjz.split("!=");
									if(!r3[1].equals(bj[1])) {
										alarmNo+=jzt.getId()+",";
										alarmValue+=jzt.getTargetName()+",";
									}
								}
							}
						}else {
							String[] mn = jzt.getCtrlDown().split(";");
							
							
							for(int rm=0;rm<mn.length;rm++) {
								String yinzi=jzt.getYinzi();
								if(mn[rm].indexOf("<")!=-1) {
									String a1 = mn[rm].replace("<", "").replace("=", "");
									float r4=0f;
									if(yinzi!=null) {
										r4=Integer.parseInt(r3[1])/Integer.parseInt(yinzi);
									}else {
										r4=Integer.parseInt(r3[1]);
									}
									if(r4<=Integer.parseInt(a1)) {
										alarmNo+=jzt.getId()+",";
										alarmValue+=jzt.getTargetName()+",";
									}
								}
								if(mn[rm].indexOf(">")!=-1) {
									String a1 = mn[rm].replace(">", "").replace("=", "");
							//		String yinzi=jzt.getYinzi();
									float r4=0f;
									if(yinzi!=null) {
										r4=Integer.parseInt(r3[1])/Integer.parseInt(yinzi);
									}else {
										r4=Integer.parseInt(r3[1]);
									}
									if(r4>=Integer.parseInt(a1)) {
										alarmNo+=jzt.getId()+",";
										alarmValue+=jzt.getTargetName()+",";
									}
								}
							}
							
						}
					}
				}
			}
		}
		String alarm=null;
		if(alarmValue.length()>0) {
			alarm=alarmNo+"::"+alarmValue;
		}
		return alarm;
	}
	
/*
	public Result<?> targetAudit(JstZcAlarm jza,JstZcDev jzd,String targetNos,List<JstZcTarget> jztCollect) {

		List resList = new ArrayList();
		String devNo=jzd.getDevNo();
		String catNo = jzd.getDevCat();
		String conInfo = jzd.getConInfo();
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
		JstZcTarget jztl = jztCollect.get(jztCollect.size() - 1);
		boolean extype = (jzd.getDevNo().equals("kangminbgsiPC330"));
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

			boolean flag = false;
			try {
				if (ts > 200) {
					master.setTimeout(3000);
				}
				master.init();
				int slaveId = 0;
				BigInteger slavebigint = new BigInteger(slave, 16);
				slaveId = slavebigint.intValue();
				BatchRead<String> batch = new BatchRead<String>();
				String tmpInstruct = null;
				int tmpOffset = 0;
				int tmp2Offset=0;
				boolean batchSend = false;
				int revnull=0;
				if(jztCollect.size()>0) {
					boolean alarmFlag = false;
					String alarmValue="";
					String alarmNo="";

					for (int j = 0; j < jztCollect.size(); j++) {
						JstZcTarget jzt = jztCollect.get(j);
						if(!targetNos.equals("connection-fail")) {
							if(targetNos.indexOf(jzt.getId())==-1) {
								continue;
							}
						}

						String di = jzt.getInstruct().substring(0, 2);
						String oshexs = jzt.getInstruct().substring(2, 6);
						String lenhexs = jzt.getInstruct().substring(6, 10);
						String instruct = jzt.getInstruct();
						int offset = 0;
						int len = 0;
						if (extype) {
							if (instruct.equals(tmpInstruct)) {
								continue;
							}
							tmpInstruct = instruct;
							BigInteger bigint = new BigInteger(oshexs, 16);
							offset = bigint.intValue();

							tmpOffset = offset;
							bigint = new BigInteger(lenhexs, 16);
							len = bigint.intValue();
						} else {

							if (jzt.getAddress() != null) {
								offset = offset + Integer.parseInt(jzt.getAddress());
							}

							if (instruct.equals(tmpInstruct) && offset==tmp2Offset) {
								continue;
							}
							if (ts > 200 && offset - tmpOffset >= 80) {
								flag = true;
							}
							tmpInstruct = instruct;
							tmp2Offset=offset;

							if (flag == true) {
								if(tmpOffset>0) {
									results = master.send(batch);
									Thread.sleep(100);
									resList.add(results.toString());
									JstZcAlarm jstZcAlarm = new JstZcAlarm();
									if(results.toString().equals("{}")) {
										revnull=revnull+1;
										if(offset<100&&revnull>0) {
											jstZcAlarm.setId(jza.getId());
							//				jstZcAlarm.setDevNo(devNo);
							//				jstZcAlarm.setCatNo(catNo);
											jstZcAlarm.setTargetNo("connection-fail");
											jstZcAlarm.setSendTime(new Date());
									//		jstZcAlarm.setSendType("1");
									//		jstZcAlarmService.updateSys(jstZcAlarm);
											if(jza.getSendType().equals("0")) {
												jstZcAlarm.setSendType("1");
												jstZcAlarmService.updateSys(jstZcAlarm);
											}else {
												if(jza.getSendType().equals("1")) {
													jstZcAlarm.setSendType("2");
													jstZcAlarmService.updateSys(jstZcAlarm);
												}
											}
											alarmFlag=true;
		//									System.out.println(devNo+"::通讯中断,发出报警::watchdog");
											break;
										}
									}
								}
		//						System.out.println(devNo+"::"+results);
								batch = new BatchRead<String>();
								flag = false;
								tmpOffset = offset;
							}
						}
						String res = null;
						Map<String, String> resMap = new HashMap<String, String>();

						if (di.equals("04")) {
			//				System.out.println(jzd.getDevNo());

							if (extype) {
								res = ModbusUtil.readInputRegistersTest(master, slaveId, offset, len);
			//					System.out.println(devNo+"::"+tmpInstruct+"::"+res);
								if(res.equals("devicefail")) {
									break;
								}
								resMap.put("instruct", tmpInstruct);
								resMap.put("resData", res);
								resList.add(resMap);
							} else {
								batch.addLocator(jzt.getId(), BaseLocator.inputRegister(slaveId, offset,
										Integer.parseInt(jzt.getDataType())));
								batchSend = true;
							}
						}
						if (di.equals("03")) {
			//				System.out.println(jzd.getDevNo());
							if (extype) {
								res = ModbusUtil.readHoldingRegistersTest(master, slaveId, offset, len);
			//					System.out.println(devNo+"::"+tmpInstruct+"::"+res);
								if(res.equals("devicefail")) {
									break;
								}
								resMap.put("instruct", tmpInstruct);
								resMap.put("resData", res);
								resList.add(resMap);
							} else {
								batch.addLocator(jzt.getId(), BaseLocator.holdingRegister(slaveId, offset,
										Integer.parseInt(jzt.getDataType())));
								batchSend = true;
							}
						}
						if (di.equals("02")) {
			//				System.out.println(jzd.getDevNo());
							if (extype) {
								ModbusUtil.readDiscreteInputTest(master, slaveId, offset, len);
							} else {
								batch.addLocator(jzt.getId(), BaseLocator.inputStatus(slaveId, offset));
								batchSend = true;
							}
						}

					}
					if (batchSend == true && alarmFlag==false) {
						Thread.sleep(100);
						results = master.send(batch);
						JstZcAlarm jstZcAlarm = new JstZcAlarm();
						jstZcAlarm.setId(jza.getId());
						if(results.toString().equals("{}")&&jztCollect.size()<80) {
							jstZcAlarm.setTargetNo("connection-fail");
							jstZcAlarm.setSendTime(new Date());
							if(jza.getSendType().equals("0")) {
								jstZcAlarm.setSendType("1");
								jstZcAlarmService.updateSys(jstZcAlarm);
							}else {
								if(jza.getSendType().equals("1")) {
									jstZcAlarm.setSendType("2");
									jstZcAlarmService.updateSys(jstZcAlarm);
								}
							}
							jstZcAlarmService.updateSys(jstZcAlarm);
		//					System.out.println(devNo+"::通讯中断,发出报警:::watchdog");
							alarmFlag=true;
						}else {
							jstZcAlarm.setDevNo(devNo);
							jstZcAlarm.setCatNo(catNo);
							jstZcAlarm.setTargetNo("recovery");
							jstZcAlarm.setSendTime(new Date());
							jstZcAlarm.setSendType("-2");
							jstZcAlarmService.updateSys(jstZcAlarm);									

		//					jstZcAlarmService.deleteSys(jza.getId());									
						}
						resList.add(results.toString());
		//				System.out.println(devNo+"::"+results);
					}
//					System.out.println(results);
	//				System.out.println(devNo+"::resList.size::"+resList.size());
					for(int ri=0;ri<resList.size();ri++) {
						String r1=(String) resList.get(ri);
						r1=r1.replaceAll(" ", "");
						r1=r1.substring(1, r1.length()-1);
						String[] r2 = r1.split(",");
						for(int rj=0;rj<r2.length;rj++) {
							String[] r3 = r2[rj].split("=");
							for(int rk=0;rk<jztCollect.size();rk++) {
								JstZcTarget jzt = jztCollect.get(rk);
								if(jzt.getAlarmPoint().equals("1")&&r3[0].equals(jzt.getId())) {
									if(jzt.getInfoType().equals("状态量")){
										if(jzt.getInterceptBit()!=null&&jzt.getInterceptBit().indexOf("bitIndex")!=-1) {
											String tmpinstruct=jzt.getInstruct();
				                            String tmpaddress=jzt.getAddress();
			                                for(int n=0;n<100;n++){
				                                JstZcTarget item = jztCollect.get(rk+n);
				                                if(item.getInstruct()!=tmpinstruct||item.getAddress()!=tmpaddress ){
				                                    break;
				                                };
	
				                           //     String aa = Integer.toBinaryString(Integer.parseInt(r3[1]));
				                           //     String a0=String.format("%16d", Integer.parseInt(aa)).replace(" ", "0");
				                                String binaryStr = Integer.toBinaryString(Integer.parseInt(r3[1]));
				                                while(binaryStr.length() < 16){
				                                    binaryStr = "0"+binaryStr;
				                                }
				                                
				                                String a1=item.getInterceptBit();
				                                String[] a2=a1.split(",");
				                                String[] a3=a2[0].split(":");
				                                int a4=Integer.parseInt(a3[1]);
				                                String a5 = binaryStr.substring(a4,a4+1);
				                                
												String bjz = item.getCtrlUp();
												if(bjz.indexOf("==")!=-1) {
													String[] bj = bjz.split("==");
													if(a5.equals(bj[1])) {
														alarmNo+=jzt.getId()+",";
														alarmValue+=jzt.getTargetName()+",";
													}
												}
												if(bjz.indexOf("!=")!=-1) {
													String[] bj = bjz.split("!=");
													if(!a5.equals(bj[1])) {
														alarmNo+=jzt.getId()+",";
														alarmValue+=jzt.getTargetName()+",";
													}
												}
			                                }
										}else {
											String bjz = jzt.getCtrlUp();
											if(bjz.indexOf("==")!=-1) {
												String[] bj = bjz.split("==");
												if(r3[1].equals(bj[1])) {
													alarmNo+=jzt.getId()+",";
													alarmValue+=jzt.getTargetName()+",";
												}
											}
											if(bjz.indexOf("!=")!=-1) {
												String[] bj = bjz.split("!=");
												if(!r3[1].equals(bj[1])) {
													alarmNo+=jzt.getId()+",";
													alarmValue+=jzt.getTargetName()+",";
												}
											}
										}
									}else {
										String[] mn = jzt.getCtrlDown().split(";");
										
										
										for(int rm=0;rm<mn.length;rm++) {
											String yinzi=jzt.getYinzi();
											if(mn[rm].indexOf("<")!=-1) {
												String a1 = mn[rm].replace("<", "").replace("=", "");
												float r4=0f;
												if(yinzi!=null) {
													r4=Integer.parseInt(r3[1])/Integer.parseInt(yinzi);
												}else {
													r4=Integer.parseInt(r3[1]);
												}
												if(r4<=Integer.parseInt(a1)) {
													alarmNo+=jzt.getId()+",";
													alarmValue+=jzt.getTargetName()+",";
												}
											}
											if(mn[rm].indexOf(">")!=-1) {
												String a1 = mn[rm].replace(">", "").replace("=", "");
										//		String yinzi=jzt.getYinzi();
												float r4=0f;
												if(yinzi!=null) {
													r4=Integer.parseInt(r3[1])/Integer.parseInt(yinzi);
												}else {
													r4=Integer.parseInt(r3[1]);
												}
												if(r4>=Integer.parseInt(a1)) {
													alarmNo+=jzt.getId()+",";
													alarmValue+=jzt.getTargetName()+",";
												}
											}
										}
										
									}
								}
							}
						}
					}
					JstZcAlarm jstZcAlarm = new JstZcAlarm();
					if(alarmValue.length()>0) {
						if(jza.getSendType().equals("0")){
							jstZcAlarm.setId(jza.getId());
							//	jstZcAlarm.setDevNo(devNo);
							//	jstZcAlarm.setCatNo(catNo);
							jstZcAlarm.setTargetNo(alarmNo);
							jstZcAlarm.setAlarmValue(alarmValue);
							jstZcAlarm.setSendTime(new Date());
							jstZcAlarm.setSendType("1");
							jstZcAlarmService.updateSys(jstZcAlarm);
						}
					}
					Thread.sleep(500);
			    }
			} catch (ModbusInitException e) {
				e.printStackTrace();
			} catch (ModbusTransportException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ErrorResponseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (Exception e)	            {
				e.printStackTrace();
            } finally {
				master.destroy();
			}
		}
		
		if (type.equals("SNMP")) {
			version = jsonConInfo.getString("version");
			timeOut = jsonConInfo.getString("timeOut");
			community = jsonConInfo.getString("community");
			jztCollect.stream().sorted(Comparator.comparing(JstZcTarget::getInstruct));
			List<String> oidList = new ArrayList<String>();
//			System.out.println(devNo+"::");
			for (int j = 0; j < jztCollect.size(); j++) {
				JstZcTarget jzt = jztCollect.get(j);
				String oidval = jzt.getInstruct();
				List snmpList = SnmpData.snmpGet(ipAddress, community, oidval,null);
				
				if(snmpList.size()>0) {
					for(int n=0;n<snmpList.size();n++) {
						resList.add(snmpList.get(n));
					}
				}else {
					break;
				}
				
			}
		}

	
		return Result.ok("验证结束");
	} */

}
