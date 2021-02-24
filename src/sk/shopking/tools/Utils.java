package sk.shopking.tools;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Utils {

	/**
	 * Otestuje, či zadaný reťazec je číslo
	 * @return TRUE ak string je číslo, inak FALSE
	 */
	public static boolean isNumber(String test) {
		boolean isNumber = true;
		try {
			Double.parseDouble(test);
		}catch(NumberFormatException ex) {
			isNumber = false;
		}
		return isNumber;
	}
	
	/**
	 * Vráti skratku dna podľa jeho poradového čísla
	 * @param day cislo dna v tyzdni
	 * @return skratku dna ako String
	 */
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
	
	/**
	 * Zaokrúhli zadané číslo podľa počtu desatinných miest
	 * @param number cislo
	 * @param desatinneMiesta pocet desatinnych miest
	 * @return zaokruhlene cislo
	 */
	public static double roundNumber(double number, int desatinneMiesta) {
	    double mierka = Math.pow(10, desatinneMiesta);
	    return Math.round(number * mierka) / mierka;
	}
	
	/**
	 * Zistí, či zadané dátumy sú v rovnaký den
	 * @param d1 prvy datum
	 * @param d2 druhy datum
	 * @return TRUE ak sú v rovnaký den,inak FALSE
	 */
	public static boolean isSameDay(Date d1, Date d2) {
		try {
			Calendar c1 = Calendar.getInstance();
			c1.setTime(d1);
			Calendar c2 = Calendar.getInstance();
			c2.setTime(d2);
			return c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR) && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR);
		} catch (NullPointerException e) {
			return false;
		}
		
	}
	
	/**
	 * Odstráni všetky duplikáty v zadanom Liste
	 * @param <T> lubovolny objekt
	 * @param list list s duplikatmi
	 * @return list s odstranenymi duplikatmi
	 */
	public static <T> List<T> removeDuplicates(List<T> list){ 
        List<T> newList = new ArrayList<>();
        for (T element : list) { 
            if (!newList.contains(element)) { 
                newList.add(element); 
            } 
        }  
        return newList; 
    } 
	
	/**
	 * Zistí, či zadaný dátum je v intervale zadaných dátumov
	 * @param myDate - porovnávací čas
	 * @param intervalOd - začiatok intervalu
	 * @param intervalDo - koniec intervalu
	 * @return ci je datum v intervale zadanych datumov
	 */
	public static boolean isBetweenDates(Date myDate, Date intervalOd, Date intervalDo) {
		return intervalOd.compareTo(myDate) * myDate.compareTo(intervalDo) > 0;
	}

	//https://stackoverflow.com/questions/10308356/how-to-obtain-the-start-time-and-end-time-of-a-day
	public static Date atStartOfDay(Date date) {
		LocalDateTime localDateTime = dateToLocalDateTime(date);
		LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
		return localDateTimeToDate(startOfDay);
	}

	public static Date atEndOfDay(Date date) {
		LocalDateTime localDateTime = dateToLocalDateTime(date);
		LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
		return localDateTimeToDate(endOfDay);
	}

	private static LocalDateTime dateToLocalDateTime(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}

	private static Date localDateTimeToDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}
}
