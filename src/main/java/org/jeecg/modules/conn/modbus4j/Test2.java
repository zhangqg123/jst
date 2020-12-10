/*
    Copyright (C) 2006-2007 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package org.jeecg.modules.conn.modbus4j;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;

/**
 * @author Matthew Lohbihler
 */
public class Test2 {
    public static void main(String[] args) throws Exception {
        IpParameters params = new IpParameters();
        params.setHost("46.1.154.203");
        params.setPort(10002);

        ModbusMaster master = new ModbusFactory().createTcpMaster(params, false);
        master.init();

        System.out.println(master.testSlaveNode(2));

        // Define the point locator.
//        BaseLocator<Number> loc = BaseLocator.holdingRegister(2, 0, DataType.TWO_BYTE_INT_UNSIGNED);

        // Set the point value
//        master.setValue(loc, 333);

        // Get the point value
 //       System.out.println(master.getValue(loc));
    }
}
