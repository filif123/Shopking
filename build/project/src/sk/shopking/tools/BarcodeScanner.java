/**
 * 
 */
package sk.shopking.tools;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.fazecast.jSerialComm.*;


/**
 * @author Filip
 *
 */
public class BarcodeScanner {

	private SerialPort comPort;
	private InputStream iStream;
	private List<BarcodeScannerListener> listeners = new ArrayList<BarcodeScannerListener>();


	public BarcodeScanner(String comPort) {
		this.comPort = SerialPort.getCommPort(comPort);
	}
	
	public static boolean isPortAvailable(String portString) {
		SerialPort[] allPorts = getCommPorts();
		for (int i = 0; i < allPorts.length; i++) {
			if (allPorts[i].getSystemPortName().equals(portString)) {
				return true;
			}
		}
		return false;
	}

    public void addListener(BarcodeScannerListener toAdd) {
        listeners.add(toAdd);
    }
	
	public static SerialPort[] getCommPorts() {
		return SerialPort.getCommPorts();
	}
	
	public void closePort() {
		comPort.removeDataListener();
		comPort.closePort();
	}
	
	public boolean initPort() {
		boolean open = comPort.openPort();
		if (open) {
			comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
			iStream = comPort.getInputStream();
			comPort.addDataListener(new SerialPortListener());
			return open;
		}
		else {
			return open;
		}
	}
	
	private class SerialPortListener implements SerialPortDataListener{
		
		@Override
		public int getListeningEvents() {
			   return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; 
		}
		@Override
		public void serialEvent(SerialPortEvent event){
			int avail = event.getSerialPort().bytesAvailable();
	        if (avail == 0) {
	            return;
	        }
	        InputStreamReader inputStreamReader = new InputStreamReader(iStream);
	        BufferedReader br = new BufferedReader(inputStreamReader);
	        try {
	        	String strRead = br.readLine();
	        	final String barcode = strRead.replaceAll("(\\r|\\n)", "");
	        	System.out.println(barcode);
	        	if(barcode.isEmpty() || barcode.contains("\\n") ) {
	        		return;
	        	}
	        	for (BarcodeScannerListener bsl : listeners) {
	        		bsl.barcodeEvent(barcode);
	        	}
	                 
				
	        }catch (Exception e) {
				e.printStackTrace();
			}    
		}
	}

	
}
