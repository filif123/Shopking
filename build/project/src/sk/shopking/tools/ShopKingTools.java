package sk.shopking.tools;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class ShopKingTools {
	public static ArrayList<Integer> intToIntegerArray(int number){
		
		ArrayList<Integer> array = new ArrayList<Integer>();
		do{
		    array.add(0,number % 10);
		    number /= 10;
		} while  (number > 0);
		return array;
	}
	
	public static int integerArrayToInt(ArrayList<Integer> numberArray){
		
		int[] arrayint = new int[numberArray.size()];
	    Iterator<Integer> iterator = numberArray.iterator();
	    for (int i = 0; i < arrayint.length; i++)
	    {
	    	arrayint[i] = iterator.next().intValue();
	    }
	    int result = 0;
	    int offset = 1;
	    for(int i = arrayint.length - 1; i >= 0; i--) {
	        result += arrayint[i] * offset;
	        offset *= 10;
	    }
	    return result;
	}
	
	public static ArrayList<Long> longToLongArray(long number){
		
		ArrayList<Long> array = new ArrayList<Long>();
		do{
		    array.add(0,number % 10);
		    number /= 10;
		} while  (number > 0);
		return array;
	}
	
	public static int longArrayToLong(ArrayList<Long> numberArray){
		
		long[] arrayint = new long[numberArray.size()];
	    Iterator<Long> iterator = numberArray.iterator();
	    for (int i = 0; i < arrayint.length; i++)
	    {
	    	arrayint[i] = iterator.next().intValue();
	    }
	    int result = 0;
	    int offset = 1;
	    for(int i = arrayint.length - 1; i >= 0; i--) {
	        result += arrayint[i] * offset;
	        offset *= 10;
	    }
	    return result;
	}
	
	public static void showExceptionDialog(Exception ex) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Zistená výnimka");
		alert.setHeaderText("Bola zachytená výnimka");
		alert.setContentText("");

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("Znenie výnimky:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
	}
	
	public static boolean isNumber(String test) {
		boolean isNumber = true;
		try {
			Double.parseDouble(test);
		}catch(NumberFormatException ex) {
			isNumber = false;
		}
		return isNumber;
	}
	
	public static String getDayOfWeek(int day) {
		switch(day) {
		case 1:
			return "NED";
		case 2:
			return "PON";
		case 3:
			return "UTO";
		case 4:
			return "STR";
		case 5:
			return "ŠTV";
		case 6:
			return "PIA";
		case 7:
			return "SOB";
		default:
			return null;
		}
	}
	
	public static double roundNumber(double number, int desatinneMiesta) {
	    double mierka = Math.pow(10, desatinneMiesta);
	    return Math.round(number * mierka) / mierka;
	}
	
	public static boolean isSameDay(Date d1, Date d2) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(d2);
		if (c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR) && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) {
			return true;
		}
		else {
			return false;
		}
	}
}
