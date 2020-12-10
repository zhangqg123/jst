package org.jeecg.modules.conn.modbus4j;

import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;

public class BatchTest {
    public static void main(String[] args) throws Exception {
        IpParameters tcpParameters = new IpParameters();
        tcpParameters.setHost("46.1.154.204");
        tcpParameters.setPort(10001);
        tcpParameters.setEncapsulated(true);

        ModbusFactory modbusFactory = new ModbusFactory();
        ModbusMaster master = modbusFactory.createTcpMaster(tcpParameters, true);

        try {
            BatchRead<String> batchRead = new BatchRead<String>();
            int slaveId = 1;
/*
            batchRead.addLocator("00011 sb true", BaseLocator.coilStatus(slaveId, 10));
 
            batchRead.addLocator("00012 sb false", BaseLocator.coilStatus(slaveId, 11));
            batchRead.addLocator("00013 sb true", BaseLocator.coilStatus(slaveId, 12));
            batchRead.addLocator("00014 sb true", BaseLocator.coilStatus(slaveId, 13));

            batchRead.addLocator("10011 sb false", BaseLocator.inputStatus(slaveId, 10));
            batchRead.addLocator("10012 sb false", BaseLocator.inputStatus(slaveId, 11));
            batchRead.addLocator("10013 sb true", BaseLocator.inputStatus(slaveId, 12));
            batchRead.addLocator("10014 sb false", BaseLocator.inputStatus(slaveId, 13));
*/
  /*          
            batchRead.addLocator("d0", BaseLocator.holdingRegisterBit(slaveId, 0, 1));
            batchRead.addLocator("d1", BaseLocator.holdingRegisterBit(slaveId, 1, 1));
            batchRead.addLocator("d2", BaseLocator.holdingRegisterBit(slaveId, 2, 1));
            batchRead.addLocator("d3", BaseLocator.holdingRegisterBit(slaveId, 3, 1));
            batchRead.addLocator("d4", BaseLocator.holdingRegisterBit(slaveId, 4, 1));
            batchRead.addLocator("d5", BaseLocator.holdingRegisterBit(slaveId, 5, 1));
*/
            /*
            batchRead.addLocator("40016-8 sb true", BaseLocator.holdingRegisterBit(slaveId, 40016, 8));
            batchRead.addLocator("40016-9 sb false", BaseLocator.holdingRegisterBit(slaveId, 40016, 9));

             batchRead.addLocator("40016-a sb false", BaseLocator.holdingRegisterBit(slaveId, 40016, 10));
            batchRead.addLocator("40016-b sb false", BaseLocator.holdingRegisterBit(slaveId, 40016, 11));
            batchRead.addLocator("40016-c sb false", BaseLocator.holdingRegisterBit(slaveId, 40016, 12));
            batchRead.addLocator("40016-d sb false", BaseLocator.holdingRegisterBit(slaveId, 40016, 13));
            batchRead.addLocator("40016-e sb true", BaseLocator.holdingRegisterBit(slaveId, 40016, 14));
            batchRead.addLocator("40016-f sb false", BaseLocator.holdingRegisterBit(slaveId, 40016, 15));

            batchRead.addLocator("30016-0 sb true", BaseLocator.inputRegisterBit(slaveId, 30016, 0));
            batchRead.addLocator("30016-1 sb false", BaseLocator.inputRegisterBit(slaveId, 30016, 1));
            batchRead.addLocator("30016-2 sb false", BaseLocator.inputRegisterBit(slaveId, 30016, 2));
            batchRead.addLocator("30016-3 sb false", BaseLocator.inputRegisterBit(slaveId, 30016, 3));
            batchRead.addLocator("30016-4 sb false", BaseLocator.inputRegisterBit(slaveId, 30016, 4));
            batchRead.addLocator("30016-5 sb false", BaseLocator.inputRegisterBit(slaveId, 30016, 5));
            batchRead.addLocator("30016-6 sb false", BaseLocator.inputRegisterBit(slaveId, 30016, 6));
            batchRead.addLocator("30016-7 sb true", BaseLocator.inputRegisterBit(slaveId, 30016, 7));
            batchRead.addLocator("30016-8 sb true", BaseLocator.inputRegisterBit(slaveId, 30016, 8));
            batchRead.addLocator("30016-9 sb false", BaseLocator.inputRegisterBit(slaveId, 30016, 9));
            batchRead.addLocator("30016-a sb false", BaseLocator.inputRegisterBit(slaveId, 30016, 10));
            batchRead.addLocator("30016-b sb false", BaseLocator.inputRegisterBit(slaveId, 30016, 11));
            batchRead.addLocator("30016-c sb false", BaseLocator.inputRegisterBit(slaveId, 30016, 12));
            batchRead.addLocator("30016-d sb false", BaseLocator.inputRegisterBit(slaveId, 30016, 13));
            batchRead.addLocator("30016-e sb false", BaseLocator.inputRegisterBit(slaveId, 30016, 14));
            batchRead.addLocator("30016-f sb true", BaseLocator.inputRegisterBit(slaveId, 30016, 15));
*/
            
            batchRead.addLocator("1",
                    BaseLocator.holdingRegister(slaveId, 1, DataType.TWO_BYTE_INT_UNSIGNED));
            batchRead.addLocator("2",
                    BaseLocator.holdingRegister(slaveId, 2, DataType.TWO_BYTE_INT_UNSIGNED));
            batchRead.addLocator("3",
                    BaseLocator.holdingRegister(slaveId, 3, DataType.TWO_BYTE_INT_UNSIGNED));
            batchRead.addLocator("4",
                    BaseLocator.holdingRegister(slaveId, 4, DataType.TWO_BYTE_INT_UNSIGNED));
            batchRead.addLocator("5",
                    BaseLocator.holdingRegister(slaveId, 5, DataType.TWO_BYTE_INT_UNSIGNED));
            batchRead.addLocator("6",
                    BaseLocator.holdingRegister(slaveId, 6, DataType.TWO_BYTE_INT_UNSIGNED));
            batchRead.addLocator("0",
                    BaseLocator.holdingRegister(slaveId, 0, DataType.TWO_BYTE_INT_UNSIGNED));

/*
            batchRead.addLocator("30017 sb -1968 tc",
                    BaseLocator.inputRegister(slaveId, 30017, DataType.TWO_BYTE_INT_UNSIGNED));
            batchRead.addLocator("30018 sb -123456789 tc",
                    BaseLocator.inputRegister(slaveId, 30018, DataType.FOUR_BYTE_INT_UNSIGNED));
            batchRead.addLocator("30020 sb -123456789 tc",
                    BaseLocator.inputRegister(slaveId, 30020, DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED));
            batchRead.addLocator("30022 sb 1968.1968",
                    BaseLocator.inputRegister(slaveId, 30022, DataType.FOUR_BYTE_FLOAT_SWAPPED));
            batchRead.addLocator("30024 sb -123456789 tc",
                    BaseLocator.inputRegister(slaveId, 30024, DataType.EIGHT_BYTE_INT_UNSIGNED));
            batchRead.addLocator("30028 sb -123456789 tc",
                    BaseLocator.inputRegister(slaveId, 30028, DataType.EIGHT_BYTE_INT_UNSIGNED_SWAPPED));
            batchRead.addLocator("30032 sb 1968.1968",
                    BaseLocator.inputRegister(slaveId, 30032, DataType.EIGHT_BYTE_FLOAT_SWAPPED));
*/
            master.init();

            BatchResults<String> results = master.send(batchRead);

            System.out.println(results);
            System.out.println(results.getValue("d0"));
            System.out.println(results.getValue("d1"));
            System.out.println(results.getValue("d2"));
            System.out.println(results.getValue("d3"));
            System.out.println(results.getValue("d4"));
            System.out.println(results.getValue("d5"));
        }
        finally {
            master.destroy();
        }
    }
}
