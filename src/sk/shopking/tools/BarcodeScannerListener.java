package sk.shopking.tools;

/**
 * Toto rozhranie musí byť implementované pre vytvorenie udalosti (event) pri čítaní čiarových kódov zo sériového portu.
 * @author Filip
 * 
 */
public interface BarcodeScannerListener {
	
	void barcodeEvent(String barcode);
}
