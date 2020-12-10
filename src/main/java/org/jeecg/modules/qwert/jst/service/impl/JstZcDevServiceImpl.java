package org.jeecg.modules.qwert.jst.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.fusesource.stomp.jms.StompJmsConnectionFactory;
import org.fusesource.stomp.jms.StompJmsDestination;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.conn.modbus4j.ModbusUtil;
import org.jeecg.modules.conn.snmp.SnmpData;
import org.jeecg.modules.dbserver.mongo.common.model.Alarm;
import org.jeecg.modules.dbserver.mongo.common.model.Audit;
import org.jeecg.modules.dbserver.mongo.repository.impl.DemoRepository;
import org.jeecg.modules.qwert.jst.entity.JstZcAlarm;
import org.jeecg.modules.qwert.jst.entity.JstZcCat;
import org.jeecg.modules.qwert.jst.entity.JstZcDev;
import org.jeecg.modules.qwert.jst.entity.JstZcTarget;
import org.jeecg.modules.qwert.jst.mapper.JstZcCatMapper;
import org.jeecg.modules.qwert.jst.mapper.JstZcDevMapper;
import org.jeecg.modules.qwert.jst.service.IJstZcAlarmService;
import org.jeecg.modules.qwert.jst.service.IJstZcCatService;
import org.jeecg.modules.qwert.jst.service.IJstZcDevService;
import org.jeecg.modules.qwert.jst.service.IJstZcTargetService;
import org.jeecg.modules.qwert.jst.utils.JstConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;

/**
 * @Description: jst_zc_dev
 * @Author: jeecg-boot
 * @Date:   2020-07-24
 * @Version: V1.0
 */
@Service
public class JstZcDevServiceImpl extends ServiceImpl<JstZcDevMapper, JstZcDev> implements IJstZcDevService {
	@Autowired
	private IJstZcCatService jstZcCatService;
	@Resource
	private JstZcDevMapper jstZcDevMapper;
	@Autowired
	private IJstZcTargetService jstZcTargetService;
	@Autowired
	private IJstZcAlarmService jstZcAlarmService;
    @Autowired
    DemoRepository repository;
    
    private List<JstZcCat> jzcList;
    private List<JstZcDev> jzdList;    
    private List<JstZcTarget> jztList;
    
//	@Cacheable(value = CacheConstant.JST_DEV_CACHE)
	@Override
	public List<JstZcDev> queryJzdList() {
//		QueryWrapper<JstZcDev> dqw = QueryGenerator.initQueryWrapper(new JstZcDev(), null);
//		dqw.eq("status", "0");
//		dqw.orderByAsc("dev_no");
		List<JstZcDev> jzdList = this.jstZcDevMapper.queryJzdList();
		return jzdList;
	}
	
	@Override
	public String handleRead(String catNo) {
		boolean allflag = true;
//		JstConstant.runflag=true;
		if(catNo.equals("all") && JstConstant.runflag==true) {
			JstConstant.runall=true;
		}
		jzcList = jstZcCatService.queryJzcList();
		jzdList = queryJzdList();
		jztList = jstZcTargetService.queryJztList();			
        MyThread mt=new MyThread(allflag,catNo);
        new Thread(mt).start();
	
		return null;
	}
	
	public void threadWork(boolean allflag, String catNo) throws JMSException {
		if(catNo==null||catNo=="") {
			return;
		}
        StompJmsConnectionFactory factory = new StompJmsConnectionFactory();
        factory.setBrokerURI("tcp://" + JstConstant.host + ":" + JstConstant.port);

        Connection connection;
		connection = factory.createConnection(JstConstant.user, JstConstant.password);
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//        Destination dest = new StompJmsDestination(JstConstant.destination);
//        MessageProducer producer = session.createProducer(dest);
//        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
	
		List<JstZcCat> jzcCollect = null;
		while(allflag&&JstConstant.runflag) {
			if(!catNo.equals("all")) {
				allflag=false;
		        jzcCollect = jzcList.stream().filter(u -> catNo.equals(u.getOriginId())).collect(Collectors.toList());
			}else {
				jzcCollect=jzcList;
			}
			for (int i = 0; i < jzcCollect.size(); i++) {
				if(!JstConstant.runflag) {
					break;
				}
//		        Destination dest = new StompJmsDestination(JstConstant.destination);
//		        MessageProducer producer = session.createProducer(dest);
//		        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
				JstZcCat jstZcCat = jzcCollect.get(i);
		        List<JstZcDev> jzdCollect = jzdList.stream().filter(u -> jstZcCat.getOriginId().equals(u.getDevCat())).collect(Collectors.toList());
				List<JstZcTarget> jztCollect = jztList.stream().filter(u -> jstZcCat.getOriginId().equals(u.getDevType())).collect(Collectors.toList());

				targetRead(jzdCollect,jztCollect,session);
			}
		}
        connection.close();
	}

	/**
	 * 测试
	 * 
	 * @param devCat
	 *
	 * @param jstZcDev
	 * @return
	 * @throws JMSException 
	 */

	public Result<?> targetRead(List<JstZcDev> jzdCollect,List<JstZcTarget> jztCollect,Session session) throws JMSException {
        Destination dest = new StompJmsDestination(JstConstant.destination);
        Destination dest2 = new StompJmsDestination(JstConstant.destination2);
        MessageProducer producer = session.createProducer(dest);
        MessageProducer producer2 = session.createProducer(dest2);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        producer2.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

		for (int i = 0; i < jzdCollect.size(); i++) {
			if(!JstConstant.runflag) {
				break;
			}
			List resList = new ArrayList();
			JstZcDev jzd = jzdCollect.get(i);
			String devNo=jzd.getDevNo();
			String devName=jzd.getDevName();
			String catNo = jzd.getDevCat();
			String modNo=jzd.getModNo();
			if(modNo==null||modNo.equals("")) {
				modNo="blank";
			}
			String conInfo = jzd.getConInfo();
//			System.out.println(conInfo);
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
	//		boolean extype = (jzd.getDevNo().equals("kangminbgsiPC330"));
			boolean extype = (jzd.getDevNo().equals("kangminbgsiPC"));
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

//				jztList.sort((x, y) -> Integer.compare(Integer.parseInt(x.getAddress()), Integer.parseInt(y.getAddress())));
				boolean flag = false;
				try {
//					if(i%5==4) {
//						Thread.sleep(100);
//					}

					if (ts > 200) {
						master.setTimeout(1500);
					}
					master.init();
					int slaveId = 0;
//					if (isNumeric(slave)) {
//						slaveId = Integer.parseInt(slave);
//					} else {
						BigInteger slavebigint = new BigInteger(slave, 16);
						slaveId = slavebigint.intValue();
//					}
					BatchRead<String> batch = new BatchRead<String>();
					String tmpInstruct = null;
					int tmpOffset = 0;
					int tmp2Offset=0;
					int offset=0;
					boolean batchSend = false;
					int revnull=0;
					if(jztCollect.size()>0) {
						boolean alarmFlag = false;
						for (int j = 0; j < jztCollect.size(); j++) {
	
							JstZcTarget jzt = jztCollect.get(j);
	
							String di = jzt.getInstruct().substring(0, 2);
							String oshexs = jzt.getInstruct().substring(2, 6);
							String lenhexs = jzt.getInstruct().substring(6, 10);
							String instruct = jzt.getInstruct();
							offset = 0;
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
					//			if (ts > 200 && offset - tmpOffset >= 80) {
					//				flag = true;
					//			}
								if (tmpInstruct!=null && !instruct.equals(tmpInstruct)) {
									flag = true;
								}
								
								tmpInstruct = instruct;
								tmp2Offset=offset;
	
								if (flag == true) {
		//							System.out.println(jzd.getDevNo()+"::"+j + "::" + offset);
									if(tmpOffset>0) {
										results = master.send(batch);
										Thread.sleep(200);
										if(results.toString().equals("{}")) {
								//			revnull=revnull+1;
											if(offset<100 && offset>0) {
												JstZcAlarm jstZcAlarm = new JstZcAlarm();
												jstZcAlarm.setDevNo(devNo);
												jstZcAlarm.setDevName(devName);
												jstZcAlarm.setCatNo(catNo);
												jstZcAlarm.setTargetNo("connection-fail");
												jstZcAlarm.setSendTime(new Date());
												jstZcAlarm.setSendType("0");
												jstZcAlarmService.saveSys(jstZcAlarm);
												alarmFlag=true;
							//					System.out.println(devNo+"::connection-fail");
							//					break;
											}
										}else {
											resList.add(results.toString());
										}
									}
							//		System.out.println(devNo+"::"+results);
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
						//			System.out.println(devNo+"::"+tmpInstruct+"::"+res);
									if(res.equals("devicefail")) {
										break;
									}
									resMap.put("instruct", tmpInstruct);
									resMap.put("resData", res);
						
						/*			batch = new BatchRead<String>();
									batch.addLocator(jzt.getId(),
											BaseLocator.holdingRegister(slaveId, offset, Integer.parseInt(jzt.getDataType())));
									results = master.send(batch);
									Thread.sleep(100);
									res=results.toString();
									System.out.println(res);
									if(!res.equals("{}")) {
										resList.add(res);
									}*/
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
									if(res.equals("devicefail")) {
										break;
									}
									resMap.put("instruct", tmpInstruct);
									resMap.put("resData", res);

						/*			batch = new BatchRead<String>();
									batch.addLocator(jzt.getId(),
											BaseLocator.holdingRegister(slaveId, offset, Integer.parseInt(jzt.getDataType())));
									results = master.send(batch);
									Thread.sleep(100);
									res=results.toString(); */
						//			System.out.println(res);
						//			if(!res.equals("{}")) {
						//				resList.add(res);
						//			}
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
							Thread.sleep(100);
						}
						if (batchSend == true && alarmFlag==false) {
						//	for(int n=0;n<3;n++) {
						//	Thread.sleep(100);
							results = master.send(batch);
							Thread.sleep(100);

							if(results.toString().equals("{}") && offset<80 && offset>0) {
									JstZcAlarm jstZcAlarm = new JstZcAlarm();
									jstZcAlarm.setDevNo(devNo);
									jstZcAlarm.setDevName(devName);
									jstZcAlarm.setCatNo(catNo);
									jstZcAlarm.setTargetNo("connection-fail");
									jstZcAlarm.setSendTime(new Date());
									jstZcAlarm.setSendType("0");
									jstZcAlarmService.saveSys(jstZcAlarm);
			//						System.out.println(devNo+"::connection-fail");
							}
							if(!results.toString().equals("{}")) {
								resList.add(results.toString());
							}	
				//			System.out.println(devNo+"::"+results);
						}
	//					System.out.println(results);
		//				System.out.println(devNo+"::resList.size::"+resList.size());
						String alarmValue="";
						String alarmNo="";
	//					if(extype) {
	//						System.out.println(resList.size());
	//					}else {
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
					                                	if((rk+n+1)>jztCollect.size()) {
					                                		break;
					                                	}
						                                JstZcTarget item = jztCollect.get(rk+n);
						                                if(item.getInstruct()!=tmpinstruct||item.getAddress()!=tmpaddress ){
						                                    break;
						                                };
			
						                           //     String aa = Integer.toBinaryString(Integer.parseInt(r3[1]));
						                           //     String a0=String.format("%16d", Integer.parseInt(aa)).replace(" ", "0");
						                                String str1=r3[1];
						                                if(str1.equals("true")) {
						                                	str1="1";
						                                }
						                                if(str1.equals("false")) {
						                                	str1="0";
						                                }
						                                
						                                String binaryStr = Integer.toBinaryString(Integer.parseInt(str1));
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
															alarmValue+=jzt.getTargetName()+"-报警值-"+r4+",";
														}
													}
													if(mn[rm].indexOf(">")!=-1) {
														String a1 = mn[rm].replace(">", "").replace("=", "");
												//		String yinzi=jzt.getYinzi();
														float r4=0f;
														String str=r3[1];
														if(str.contains(".")) {
															 int indexOf = str.indexOf(".");
															 str = str.substring(0, indexOf);
														}
														if(yinzi!=null) {
															r4=Integer.parseInt(str)/Integer.parseInt(yinzi);
														}else {
															r4=Integer.parseInt(str);
														}
														if(r4>=Integer.parseInt(a1)) {
															alarmNo+=jzt.getId()+",";
															alarmValue+=jzt.getTargetName()+"-报警值-"+r4+",";
														}
													}
												}
												
											}
										}
									}
								}
							}
		//				}
						if(alarmValue.length()>0) {
							List<JstZcAlarm> jzaList = jstZcAlarmService.queryJzaList("2");
							int dealflag=0; //初始状态
							for(int ai=0;ai<jzaList.size();ai++) {
								JstZcAlarm jza = jzaList.get(ai);
								if(jza.getDevNo().equals(devNo)&&jza.getTargetNo().equals(alarmNo)) {
									if(jza.getDealType()=="1") {  //已处理
										JstZcAlarm jstZcAlarm = new JstZcAlarm();
										jstZcAlarm.setDevNo(devNo);
										jstZcAlarm.setDevName(devName);
										jstZcAlarm.setCatNo(catNo);
										jstZcAlarm.setTargetNo(alarmNo);
										jstZcAlarm.setAlarmValue(alarmValue);
										jstZcAlarm.setSendTime(new Date());
										jstZcAlarm.setSendType("2");
										jstZcAlarmService.saveSys(jstZcAlarm);
										dealflag=2; //已处理
										break;
									}else {
										dealflag=1; //未处理
										jza.setSendTime(new Date());
										jstZcAlarmService.updateSys(jza);
									}
								}
							}
							if(dealflag==0 || dealflag==2) {
								JstZcAlarm jstZcAlarm = new JstZcAlarm();
								jstZcAlarm.setDevNo(devNo);
								jstZcAlarm.setDevName(devName);
								jstZcAlarm.setCatNo(catNo);
								jstZcAlarm.setTargetNo(alarmNo);
								jstZcAlarm.setAlarmValue(alarmValue);
								jstZcAlarm.setSendTime(new Date());
								jstZcAlarm.setSendType("2");
								jstZcAlarmService.saveSys(jstZcAlarm);
							}
						}else {
							List<JstZcAlarm> jzaList = jstZcAlarmService.queryJzaList("1");
						//	List<JstZcAlarm> jzaList = jstZcAlarmService.queryJzaList("0");
							for(int ai=0;ai<jzaList.size();ai++) {
								JstZcAlarm jza = jzaList.get(ai);
								if(jza.getDevNo().equals(devNo)) {
									jza.setSendType("-2");
									jstZcAlarmService.updateSys(jza);
						//			jstZcAlarmService.deleteSys(jza.getId());
								}
							}
						}
						Thread.sleep(200);
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
			        Alarm alarm = new Alarm();
//			        alarm.setId("2");
			        alarm.setDevNo(devNo);
			        alarm.setTargetNo("mysql");
			        alarm.setAlarmValue("数据库保存失败");
			        alarm.setSendTime(new Date());
			        repository.insertAlarm(alarm);
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
		//		System.out.println(devNo+"::");
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
			try {
				String resValue = org.apache.commons.lang.StringUtils.join(resList.toArray(),";");
		        Audit audit = new Audit();
		        audit.setDevNo(devNo);
		        audit.setAuditValue(resValue);
		        audit.setAuditTime(new Date());
		        repository.insertAudit(audit);
		        
		        String messageBody = "\""+audit.getAuditValue()+"\"";
		        String sendMessage = "[{"+"devNo:\""+devNo+"\","+"modNo:\""+modNo+"\","+"message:"+messageBody+"}]";
				TextMessage msg = session.createTextMessage(sendMessage);
				if (type.equals("SOCKET")) {
					producer.send(msg);
	            }
				if (type.equals("SNMP")) {
					producer2.send(msg);
	            }
			} catch (Exception e) {
			      e.printStackTrace();
			}			
		}
		
		return Result.ok("巡检结束");
	}
	
	class MyThread implements Runnable {
		private boolean allflag;
		private String catNo;
		
		public MyThread(boolean allflag, String catNo) {
			this.allflag = allflag;
			this.catNo = catNo;
		}

		@Override
		public void run() {
			try {
				long start, end;
				start = System.currentTimeMillis();
				threadWork(this.allflag,this.catNo);
				end = System.currentTimeMillis();
				System.out.println("开始时间:" + start + "; 结束时间:" + end + "; 用时:" + (end - start) + "(ms)");
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
