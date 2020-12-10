/**
 * Copyright (C) 2015 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package org.jeecg.modules.conn.modbus4j;
 
import com.serotonin.modbus4j.serial.SerialPortWrapper;
import jssc.SerialPort;
 
import java.io.InputStream;
import java.io.OutputStream;
 
import jssc.SerialPortException;
//The project cannot be built until build path errors are resolved
/**
 * 
 * This class is not finished
 * 
 * @author Terry Packer
 *
 */
public class TestSerialPortWrapper implements SerialPortWrapper{
	
	private SerialPort port;
	private String commPortId;
    private int baudRate;
    private int flowControlIn;
    private int flowControlOut;
    private int dataBits;
    private int stopBits;
	private int parity;
	
	public TestSerialPortWrapper(String commPortId, int baudRate, int flowControlIn,
			int flowControlOut, int dataBits, int stopBits, int parity){
		this.commPortId = commPortId;
        this.baudRate = baudRate;
        this.flowControlIn = flowControlIn;
        this.flowControlOut = flowControlOut;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
		this.parity = parity;
 
		port = new SerialPort(this.commPortId);
		
	}
 
	/* (non-Javadoc)
	 * @see com.serotonin.modbus4j.serial.SerialPortWrapper#close()
	 */
	@Override
	public void close() throws Exception {
		port.closePort();
		// TODO Auto-generated method stub
		
	}
 
	/* (non-Javadoc)
	 * @see com.serotonin.modbus4j.serial.SerialPortWrapper#open()
	 */
	@Override
	public void open() throws Exception {
		try {
            port.openPort();
            port.setParams(this.getBaudRate(), this.getDataBits(), this.getStopBits(), this.getParity());
            port.setFlowControlMode(this.getFlowControlIn() | this.getFlowControlOut());
 
            //listeners.forEach(PortConnectionListener::opened);
            //LOG.debug("Serial port {} opened", port.getPortName());
        } catch (SerialPortException ex) {
            //LOG.error("Error opening port : {} for {} ", port.getPortName(), ex);
        }
		// TODO Auto-generated method stub
		
	}
 
 
 
	/* (non-Javadoc)
	 * @see com.serotonin.modbus4j.serial.SerialPortWrapper#getInputStream()
	 */
	@Override
	public InputStream getInputStream() {
		// TODO Auto-generated method stub
		return new SerialInputStream(port);
	}
 
	/* (non-Javadoc)
	 * @see com.serotonin.modbus4j.serial.SerialPortWrapper#getOutputStream()
	 */
	@Override
	public OutputStream getOutputStream() {
		// TODO Auto-generated method stub
		return new SerialOutputStream(port);
	}
 
	/* (non-Javadoc)
	 * @see com.serotonin.modbus4j.serial.SerialPortWrapper#getBaudRate()
	 */
	@Override
	public int getBaudRate() {
		// TODO Auto-generated method stub
		return baudRate;
	}
 
 
    public int getFlowControlIn() {
        return flowControlIn;
        //return SerialPort.FLOWCONTROL_NONE;
    }
 
    public int getFlowControlOut() {
        return flowControlOut;
        //return SerialPort.FLOWCONTROL_NONE;
    }
 
 
	/* (non-Javadoc)
	 * @see com.serotonin.modbus4j.serial.SerialPortWrapper#getStopBits()
	 */
	@Override
	public int getStopBits() {
		// TODO Auto-generated method stub
		return stopBits;
	}
 
	/* (non-Javadoc)
	 * @see com.serotonin.modbus4j.serial.SerialPortWrapper#getParity()
	 */
	@Override
	public int getParity() {
		// TODO Auto-generated method stub
		return parity;
	}
 
	/* (non-Javadoc)
	 * @see com.serotonin.modbus4j.serial.SerialPortWrapper#getDataBits()
	 */
	@Override
	public int getDataBits() {
		// TODO Auto-generated method stub
		return dataBits;
	}
 
}