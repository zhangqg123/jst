package org.jeecg.modules.conn.snmp;

import java.util.ArrayList;
import java.util.List;
import org.snmp4j.log.ConsoleLogFactory;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogFactory;


public class SnmpTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// TODO Auto-generated method stub
		SnmpTest test = new SnmpTest();

	//	test.testGet();
		test.testGetList();
	//	test.testGetAsyList();
	//	test.testWalk();
	//	test.testAsyWalk();
	//	test.testVersion();

	}

	public void testGet() {
		String ip = "192.168.1.31";
		String community = "public";
		String oidval = "1.3.6.1.4.1.34672.20.1.3.1.80.2.1.3.1";
		SnmpData.snmpGet(ip, community, oidval);
	}

	public void testGetList() {
		String ip = "192.168.1.31";
		String community = "public";
		List<String> oidList = new ArrayList<String>();
		oidList.add("1.3.6.1.4.1.34672.20.1.3.1.80.2.1.3.1");
		oidList.add("1.3.6.1.4.1.34672.20.1.3.1.80.2.1.3.2");
		oidList.add("1.3.6.1.4.1.34672.20.1.3.1.80.2.1.5.1");
		oidList.add("1.3.6.1.4.1.34672.20.1.3.1.80.2.1.5.2");
		SnmpData.snmpGetList(ip, community, oidList);
	}

	public void testGetAsyList() {
		String ip = "127.0.0.1";
		String community = "public";
		List<String> oidList = new ArrayList<String>();
		oidList.add("1.3.6.1.2.1");
		oidList.add("1.3.6.1.2.12");
		SnmpData.snmpAsynGetList(ip, community, oidList);
		System.out.println("i am first!");
	}

	public void testWalk() {
		String ip = "127.0.0.1";
		String community = "public";
//		String targetOid = "1.3.6.1.2.1.1.5.0";
		String targetOid = "1.3.6.1.2.1.25.4.2.1.2";
		SnmpData.snmpWalk(ip, community, targetOid);
	}

	public void testAsyWalk() {
		String ip = "127.0.0.1";
		String community = "public";
		// 异步采集数据
		SnmpData.snmpAsynWalk(ip, community, "1.3.6.1.2.1.25.4.2.1.2");
	}

	public void testSetPDU() throws Exception {
		String ip = "127.0.0.1";
		String community = "public";
		SnmpData.setPDU(ip, community, "1.3.6.1.2.1.1.6.0", "jianghuiwen");
	}

	public void testVersion() {
		System.out.println(org.snmp4j.version.VersionInfo.getVersion());
	}
}