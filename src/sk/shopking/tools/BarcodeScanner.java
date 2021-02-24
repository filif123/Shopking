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

	private final SerialPort comPort;
	private InputStream iStream;
	private final List<BarcodeScannerListener> listeners = new ArrayList<>();


	public BarcodeScanner(String comPort) {
		this.comPort = SerialPort.getCommPort(comPort);
	}
	
	public static boolean isPortNotAvailable(String portString) {
		SerialPort[] allPorts = getCommPorts();
		for (SerialPort allPort : allPorts) {
			if (allPort.getSystemPortName().equals(portString)) {
				return false;
			}
		}
		return true;
	}

    public void addListener(BarcodeScannerListener toAdd) {
        listeners.add(toAdd);
    }

	public void removeListener(BarcodeScannerListener toRemove) {
		listeners.remove(toRemove);
	}

	public void removeListeners() {
		listeners.clear();
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
			return true;
		}
		else {
			return false;
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
	        	final String barcode = strRead.replaceAll("([\\r\\n])", "");
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
