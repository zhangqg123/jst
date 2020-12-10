package org.jeecg.modules.conn.modbus4j;

import java.util.Arrays;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.serotonin.modbus4j.msg.ReadCoilsRequest;
import com.serotonin.modbus4j.msg.ReadCoilsResponse;
import com.serotonin.modbus4j.msg.ReadDiscreteInputsRequest;
import com.serotonin.modbus4j.msg.ReadDiscreteInputsResponse;
import com.serotonin.modbus4j.msg.ReadExceptionStatusRequest;
import com.serotonin.modbus4j.msg.ReadExceptionStatusResponse;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersResponse;
import com.serotonin.modbus4j.msg.ReadInputRegistersRequest;
import com.serotonin.modbus4j.msg.ReadInputRegistersResponse;
import com.serotonin.modbus4j.msg.ReportSlaveIdRequest;
import com.serotonin.modbus4j.msg.ReportSlaveIdResponse;
import com.serotonin.modbus4j.msg.WriteCoilRequest;
import com.serotonin.modbus4j.msg.WriteCoilResponse;
import com.serotonin.modbus4j.msg.WriteCoilsRequest;
import com.serotonin.modbus4j.msg.WriteCoilsResponse;
import com.serotonin.modbus4j.msg.WriteMaskRegisterRequest;
import com.serotonin.modbus4j.msg.WriteMaskRegisterResponse;
import com.serotonin.modbus4j.msg.WriteRegisterRequest;
import com.serotonin.modbus4j.msg.WriteRegisterResponse;
import com.serotonin.modbus4j.msg.WriteRegistersRequest;
import com.serotonin.modbus4j.msg.WriteRegistersResponse;

public class ModbusUtil {

	public static void readDiscreteInputTest(ModbusMaster master, int slaveId, int start, int len) {
		try {
			ReadDiscreteInputsRequest request = new ReadDiscreteInputsRequest(slaveId, start, len);
			ReadDiscreteInputsResponse response = (ReadDiscreteInputsResponse) master.send(request);

			if (response.isException())
				System.out.println("Exception response: message=" + response.getExceptionMessage());
			else
				System.out.println(Arrays.toString(response.getBooleanData()));
		} catch (ModbusTransportException e) {
			e.printStackTrace();
		}
	}

	public static String readHoldingRegistersTest(ModbusMaster master, int slaveId, int start, int len) {
		String result = null;
		try {
			ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest(slaveId, start, len);
			ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) master.send(request);
			if(response==null) {
				result ="devicefail";
				System.out.println("设备连接失败");
				return result;
			}
			if (response.isException()) {
				result = "fail";
				System.out.println("Exception response: message=" + response.getExceptionMessage());
			} else {
				result = Arrays.toString(response.getShortData());
//				System.out.println(Arrays.toString(response.getShortData()));
			}
		} catch (ModbusTransportException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String readInputRegistersTest(ModbusMaster master, int slaveId, int start, int len) {
		String result = null;
		try {
			ReadInputRegistersRequest request = new ReadInputRegistersRequest(slaveId, start, len);
			ReadInputRegistersResponse response = (ReadInputRegistersResponse) master.send(request);
			if(response==null) {
				result ="devicefail";
				System.out.println("设备连接失败");
				return result;
			}
			if (response.isException()) {
				result = "fail";
				System.out.println("Exception response: message=" + response.getExceptionMessage());
			} else {
				result = Arrays.toString(response.getShortData());
				System.out.println(Arrays.toString(response.getShortData()));
			}
		} catch (ModbusTransportException e) {
			e.printStackTrace();
		}
		return result;
	}
}
