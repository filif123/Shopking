package sk.shopking.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xddf.usermodel.PresetColor;
import org.apache.poi.xddf.usermodel.XDDFColor;
import org.apache.poi.xddf.usermodel.XDDFLineProperties;
import org.apache.poi.xddf.usermodel.XDDFShapeProperties;
import org.apache.poi.xddf.usermodel.XDDFSolidFillProperties;
import org.apache.poi.xddf.usermodel.chart.AxisCrossBetween;
import org.apache.poi.xddf.usermodel.chart.AxisCrosses;
import org.apache.poi.xddf.usermodel.chart.AxisPosition;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xddf.usermodel.chart.XDDFCategoryAxis;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFChartLegend;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFLineChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFValueAxis;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;

import sk.shopking.*;

/**
 * @author filip
 *
 */
public class ExportToExcel {

	/**
	 * Vytvorí tabuľku v tabuľkovom procesore MS Excel do zadaného súboru a uloží do nej zadané denné závierky.
	 * @param file výstupný súbor
	 * @param denneZavierky denné závierky
	 * @throws IOException ak sa dáta nepodarilo zapísať
	 */
	public static void exportDenneZavierky(File file, List<DenneZavierky> denneZavierky) throws IOException {
		String[] columnsFirst = {"Dátum","Sadzba 10%","","","","","","Sadzba 20%","","","","",""};
		String[] columnsSecond = {"","Kladný obrat","","","Záporný obrat","","","Kladný obrat","","","Záporný obrat","",""};
		String[] columnsThird = {"","Obrat","Základ","DPH","Obrat","Základ","DPH","Obrat","Základ","DPH","Obrat","Základ","DPH"};

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Denné závierky");

		Font hlavickaFont = workbook.createFont();
		hlavickaFont.setBold(true);

		CellStyle stylHlavicky = workbook.createCellStyle();
		stylHlavicky.setFont(hlavickaFont);
		stylHlavicky.setAlignment(HorizontalAlignment.CENTER);

		CellStyle stylCislo = workbook.createCellStyle();
		DataFormat formatNum = workbook.createDataFormat();
		stylCislo.setDataFormat(formatNum.getFormat("0.00"));

		CellStyle stylDatum = workbook.createCellStyle();
		DataFormat formatDate = workbook.createDataFormat();
		stylDatum.setDataFormat(formatDate.getFormat("dd.MM.yyyy"));

		Row prvyRiadokHlavicky = sheet.createRow(0);
		Row druhyRiadokHlavicky = sheet.createRow(1);
		Row tretiRiadokHlavicky = sheet.createRow(2);

		for (int i = 0; i < columnsFirst.length; i++) {
			Cell cell = prvyRiadokHlavicky.createCell(i);
			cell.setCellValue(columnsFirst[i]);
			cell.setCellStyle(stylHlavicky);
		}

		for (int i = 0; i < columnsSecond.length; i++) {
			Cell cell = druhyRiadokHlavicky.createCell(i);
			cell.setCellValue(columnsSecond[i]);
			cell.setCellStyle(stylHlavicky);
		}

		for (int i = 0; i < columnsSecond.length; i++) {
			Cell cell = tretiRiadokHlavicky.createCell(i);
			cell.setCellValue(columnsThird[i]);
			cell.setCellStyle(stylHlavicky);
		}
		//rowFrom,rowTo,colFrom,colTo
		sheet.addMergedRegion(new CellRangeAddress(0,2,0,0));//Datum a cas
		sheet.addMergedRegion(new CellRangeAddress(0,0,1,6));//sadzba10
		sheet.addMergedRegion(new CellRangeAddress(0,0,7,12));//sadzba20

		sheet.addMergedRegion(new CellRangeAddress(1,1,1,3));//kladny obrat v sadzbe10
		sheet.addMergedRegion(new CellRangeAddress(1,1,4,6));//zaporny obrat v sadzbe10
		sheet.addMergedRegion(new CellRangeAddress(1,1,7,9));//kladny obrat v sadzbe20
		sheet.addMergedRegion(new CellRangeAddress(1,1,10,12));//zaporny obrat v sadzbe20

		int cisloRiadka = 3;

		for (DenneZavierky zavierka : denneZavierky) {
			Row riadok = sheet.createRow(cisloRiadka++);

			float zapornyObratSadzba10 = zavierka.getZapornyObratSadzba10();
			float zapornyZakladSadzba10 = zavierka.getZapornyZakladSadzba10();
			float zaporneDPHSadzba10 = zavierka.getZaporneDPHSadzba10();

			float zapornyObratSadzba20 = zavierka.getZapornyObratSadzba20();
			float zapornyZakladSadzba20 = zavierka.getZapornyZakladSadzba20();
			float zaporneDPHSadzba20 = zavierka.getZaporneDPHSadzba20();

			if (zapornyObratSadzba10 != 0) {
				zapornyObratSadzba10=-zapornyObratSadzba10;
			}
			if (zapornyZakladSadzba10 != 0) {
				zapornyZakladSadzba10=-zapornyZakladSadzba10;
			}
			if (zaporneDPHSadzba10 != 0) {
				zaporneDPHSadzba10=-zaporneDPHSadzba10;
			}

			if (zapornyObratSadzba20 != 0) {
				zapornyObratSadzba20=-zapornyObratSadzba20;
			}
			if (zapornyZakladSadzba20 != 0) {
				zapornyZakladSadzba20=-zapornyZakladSadzba20;
			}
			if (zaporneDPHSadzba20 != 0) {
				zaporneDPHSadzba20=-zaporneDPHSadzba20;
			}

			riadok.createCell(0).setCellValue(zavierka.getDatumZavierok());

			riadok.createCell(1).setCellValue(zavierka.getObratSadzba10());
			riadok.createCell(2).setCellValue(zavierka.getZakladSadzba10());
			riadok.createCell(3).setCellValue(zavierka.getDphSadzba10());

			riadok.createCell(4).setCellValue(zapornyObratSadzba10);
			riadok.createCell(5).setCellValue(zapornyZakladSadzba10);
			riadok.createCell(6).setCellValue(zaporneDPHSadzba10);

			riadok.createCell(7).setCellValue(zavierka.getObratSadzba20());
			riadok.createCell(8).setCellValue(zavierka.getZakladSadzba20());
			riadok.createCell(9).setCellValue(zavierka.getDphSadzba20());

			riadok.createCell(10).setCellValue(zapornyObratSadzba20);
			riadok.createCell(11).setCellValue(zapornyZakladSadzba20);
			riadok.createCell(12).setCellValue(zaporneDPHSadzba20);

			riadok.getCell(0).setCellStyle(stylDatum);

			for (int i = 1;i < riadok.getLastCellNum();i++) {
				riadok.getCell(i).setCellStyle(stylCislo);
			}
		}

		for (int i = 0; i < columnsFirst.length; i++) {
			sheet.autoSizeColumn(i);
		}

		FileOutputStream fileOut = new FileOutputStream(file);
		workbook.write(fileOut);
		fileOut.close();
		workbook.close();
	}

	/**
	 * Vytvorí tabuľku v tabuľkovom procesore MS Excel do zadaného súboru a uloží do nej zadané mesačné závierky.
	 * @param file výstupný súbor
	 * @param mesacneZavierky denné závierky
	 * @throws IOException ak sa dáta nepodarilo zapísať
	 */
	public static void exportMesacneZavierky(File file, List<MesacnaZavierka> mesacneZavierky) throws IOException {
		String[] columnsFirst = {"Dátum","Obdobie od","Obdobie do","Sadzba 10%","","","Sadzba 20%","",""};
		String[] columnsSecond = {"","","","Obrat","Základ","DPH","Obrat","Základ","DPH"};

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Mesačné závierky");

		Font hlavickaFont = workbook.createFont();
		hlavickaFont.setBold(true);

		CellStyle stylHlavicky = workbook.createCellStyle();
		stylHlavicky.setFont(hlavickaFont);
		stylHlavicky.setAlignment(HorizontalAlignment.CENTER);

		CellStyle stylCislo = workbook.createCellStyle();
		DataFormat formatNum = workbook.createDataFormat();
		stylCislo.setDataFormat(formatNum.getFormat("0.00"));

		CellStyle stylDatum = workbook.createCellStyle();
		DataFormat formatDate = workbook.createDataFormat();
		stylDatum.setDataFormat(formatDate.getFormat("dd.MM.yyyy"));

		Row prvyRiadokHlavicky = sheet.createRow(0);
		Row druhyRiadokHlavicky = sheet.createRow(1);

		for (int i = 0; i < columnsFirst.length; i++) {
			Cell cell = prvyRiadokHlavicky.createCell(i);
			cell.setCellValue(columnsFirst[i]);
			cell.setCellStyle(stylHlavicky);
		}

		for (int i = 0; i < columnsSecond.length; i++) {
			Cell cell = druhyRiadokHlavicky.createCell(i);
			cell.setCellValue(columnsSecond[i]);
			cell.setCellStyle(stylHlavicky);
		}

		//rowFrom,rowTo,colFrom,colTo
		sheet.addMergedRegion(new CellRangeAddress(0,1,0,0));//datum
		sheet.addMergedRegion(new CellRangeAddress(0,1,1,1));//odb od
		sheet.addMergedRegion(new CellRangeAddress(0,1,2,2));//obd do

		sheet.addMergedRegion(new CellRangeAddress(0,0,3,5));//sadzba10
		sheet.addMergedRegion(new CellRangeAddress(0,0,6,8));//sadzba20

		int cisloRiadka = 2;

		SimpleDateFormat fDate = new SimpleDateFormat("dd.MM.yyyy");

		for (MesacnaZavierka zavierka : mesacneZavierky) {
			Row riadok = sheet.createRow(cisloRiadka++);

			riadok.createCell(0).setCellValue(fDate.format(zavierka.getCasZavierky()));
			riadok.createCell(1).setCellValue(fDate.format(zavierka.getIntervalOd()));
			riadok.createCell(2).setCellValue(fDate.format(zavierka.getIntervalDo()));

			riadok.createCell(3).setCellValue(zavierka.getObratSadzba10());
			riadok.createCell(4).setCellValue(zavierka.getZakladSadzba10());
			riadok.createCell(5).setCellValue(zavierka.getDphSadzba10());

			riadok.createCell(6).setCellValue(zavierka.getObratSadzba20());
			riadok.createCell(7).setCellValue(zavierka.getZakladSadzba20());
			riadok.createCell(8).setCellValue(zavierka.getDphSadzba20());

			riadok.getCell(0).setCellStyle(stylDatum);
			riadok.getCell(1).setCellStyle(stylDatum);
			riadok.getCell(2).setCellStyle(stylDatum);

			for (int i = 3;i < riadok.getLastCellNum();i++) {
				riadok.getCell(i).setCellStyle(stylCislo);
			}
		}

		for (int i = 0; i < columnsFirst.length; i++) {
			sheet.autoSizeColumn(i);
		}

		FileOutputStream fileOut = new FileOutputStream(file);
		workbook.write(fileOut);
		fileOut.close();
		workbook.close();
	}

	/**
	 * Vytvorí tabuľku v tabuľkovom procesore MS Excel do zadaného súboru a uloží do nej zadané ročné závierky.
	 * @param file výstupný súbor
	 * @param rocneZavierky ročné závierky
	 * @throws IOException ak sa dáta nepodarilo zapísať
	 */
	public static void exportRocneZavierky(File file, List<RocnaZavierka> rocneZavierky) throws IOException {
		String[] columnsFirst = {"Dátum","Obdobie od","Obdobie do","Sadzba 10%","","","Sadzba 20%","",""};
		String[] columnsSecond = {"","","","Obrat","Základ","DPH","Obrat","Základ","DPH"};

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Ročné závierky");

		Font hlavickaFont = workbook.createFont();
		hlavickaFont.setBold(true);

		CellStyle stylHlavicky = workbook.createCellStyle();
		stylHlavicky.setFont(hlavickaFont);
		stylHlavicky.setAlignment(HorizontalAlignment.CENTER);

		CellStyle stylCislo = workbook.createCellStyle();
		DataFormat formatNum = workbook.createDataFormat();
		stylCislo.setDataFormat(formatNum.getFormat("0.00"));

		CellStyle stylDatum = workbook.createCellStyle();
		DataFormat formatDate = workbook.createDataFormat();
		stylDatum.setDataFormat(formatDate.getFormat("dd.MM.yyyy"));

		Row prvyRiadokHlavicky = sheet.createRow(0);
		Row druhyRiadokHlavicky = sheet.createRow(1);

		for (int i = 0; i < columnsFirst.length; i++) {
			Cell cell = prvyRiadokHlavicky.createCell(i);
			cell.setCellValue(columnsFirst[i]);
			cell.setCellStyle(stylHlavicky);
		}

		for (int i = 0; i < columnsSecond.length; i++) {
			Cell cell = druhyRiadokHlavicky.createCell(i);
			cell.setCellValue(columnsSecond[i]);
			cell.setCellStyle(stylHlavicky);
		}

		//rowFrom,rowTo,colFrom,colTo
		sheet.addMergedRegion(new CellRangeAddress(0,1,0,0));//datum
		sheet.addMergedRegion(new CellRangeAddress(0,1,1,1));//odb od
		sheet.addMergedRegion(new CellRangeAddress(0,1,2,2));//obd do

		sheet.addMergedRegion(new CellRangeAddress(0,0,3,5));//sadzba10
		sheet.addMergedRegion(new CellRangeAddress(0,0,6,8));//sadzba20

		int cisloRiadka = 2;

		for (RocnaZavierka zavierka : rocneZavierky) {
			Row riadok = sheet.createRow(cisloRiadka++);

			riadok.createCell(0).setCellValue(zavierka.getCasZavierky());
			riadok.createCell(1).setCellValue(zavierka.getIntervalOd());
			riadok.createCell(2).setCellValue(zavierka.getIntervalDo());

			riadok.createCell(3).setCellValue(zavierka.getObratSadzba10());
			riadok.createCell(4).setCellValue(zavierka.getZakladSadzba10());
			riadok.createCell(5).setCellValue(zavierka.getDphSadzba10());

			riadok.createCell(6).setCellValue(zavierka.getObratSadzba20());
			riadok.createCell(7).setCellValue(zavierka.getZakladSadzba20());
			riadok.createCell(8).setCellValue(zavierka.getDphSadzba20());

			riadok.getCell(0).setCellStyle(stylDatum);
			riadok.getCell(1).setCellStyle((stylDatum));
			riadok.getCell(2).setCellStyle((stylDatum));

			for (int i = 3;i < riadok.getLastCellNum();i++) {
				riadok.getCell(i).setCellStyle(stylCislo);
			}
		}

		//Row riadokSum = sheet.createRow(cisloRiadka);

		for (int i = 0; i < columnsFirst.length; i++) {
			sheet.autoSizeColumn(i);
		}

		FileOutputStream fileOut = new FileOutputStream(file);
		workbook.write(fileOut);
		fileOut.close();
		workbook.close();
	}

	/**
	 * Vytvorí tabuľku v tabuľkovom procesore MS Excel do zadaného súboru a uloží do nej tržby obchodnej prevádzky.
	 * @param file výstupný súbor
	 * @param denneZavierky denné závierky
	 * @throws IOException ak sa dáta nepodarilo zapísať
	 */
	public static void exportTrzby(File file, List<DenneZavierky> denneZavierky) throws IOException {
		String[] columnsFirst = {"Dátum","Obrat"};

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Denné tržby");

		Font hlavickaFont = workbook.createFont();
		hlavickaFont.setBold(true);

		CellStyle stylHlavicky = workbook.createCellStyle();
		stylHlavicky.setFont(hlavickaFont);
		stylHlavicky.setAlignment(HorizontalAlignment.CENTER);

		CellStyle stylCislo = workbook.createCellStyle();
		DataFormat formatNum = workbook.createDataFormat();
		stylCislo.setDataFormat(formatNum.getFormat("0.00"));

		CellStyle stylDatum = workbook.createCellStyle();
		DataFormat formatDate = workbook.createDataFormat();
		stylDatum.setDataFormat(formatDate.getFormat("dd.MM.yyyy"));

		Row prvyRiadokHlavicky = sheet.createRow(0);

		for (int i = 0; i < columnsFirst.length; i++) {
			Cell cell = prvyRiadokHlavicky.createCell(i);
			cell.setCellValue(columnsFirst[i]);
			cell.setCellStyle(stylHlavicky);
		}

		int cisloRiadka = 1;

		for (DenneZavierky zavierka : denneZavierky) {
			Row riadok = sheet.createRow(cisloRiadka++);

			riadok.createCell(0).setCellValue(zavierka.getDatumZavierok());
			riadok.createCell(1).setCellValue(zavierka.getSumaObrat());

			riadok.getCell(0).setCellStyle(stylDatum);
			riadok.getCell(1).setCellStyle(stylCislo);
		}

		for (int i = 0; i < columnsFirst.length; i++) {
			sheet.autoSizeColumn(i);
		}

		if(denneZavierky.size() != 0){
			//rowFrom,rowTo,colFrom,colTo
			addLineChart(sheet, new CellRangeAddress(0, denneZavierky.size(), 0, 1), // chart in range D1:O16
					new CellRangeAddress[]{
							new CellRangeAddress(0, 0, 0, 0), // title for x (cat)
							new CellRangeAddress(0, 0, 1, 1) // title for series 1
					},
					new CellRangeAddress[]{
							new CellRangeAddress(1, denneZavierky.size(), 0, 0), // data x (cat)
							new CellRangeAddress(1, denneZavierky.size(), 1, 1) // data series 1
					},
					new PresetColor[] {PresetColor.BLUE}
			);
		}

		FileOutputStream fileOut = new FileOutputStream(file);
		workbook.write(fileOut);
		fileOut.close();
		workbook.close();
	}

	/**
	 * Vytvorí tabuľku v tabuľkovom procesore MS Excel do zadaného súboru a uloží do nej zadané tovary a ich predajnosť.
	 * @param file výstupný súbor
	 * @param vybraneTovary tovar s predajnostou, z ktorého sa bude vytvárať štatistika
	 * @throws IOException ak sa dáta nepodarilo zapísať
	 */
	public static void exportPredajnost(File file, List<Tovar> vybraneTovary) throws IOException, ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		vybraneTovary.sort(Comparator.comparing(Tovar::getTovarName));
		List<String> firstRow = new ArrayList<>();

		TreeMap<String, TreeMap<String,Float>> predajnostiTovarovZaDen = new TreeMap<>((o1, o2) -> {
			try {
				return dateFormat.parse(o1).compareTo(dateFormat.parse(o2));
			} catch (ParseException e) {
				return 0;
			}
		});

		List<Nakup> vsetkyNakupy = Database.getNakupy();

		for (Tovar tovar : vybraneTovary) {
			firstRow.add(tovar.getTovarName());
		}

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Predajnosť");

		Font hlavickaFont = workbook.createFont();
		hlavickaFont.setBold(true);

		CellStyle stylHlavicky = workbook.createCellStyle();
		stylHlavicky.setFont(hlavickaFont);
		stylHlavicky.setAlignment(HorizontalAlignment.CENTER);

		CellStyle stylCislo = workbook.createCellStyle();
		DataFormat formatNum = workbook.createDataFormat();
		stylCislo.setDataFormat(formatNum.getFormat("@"));

		CellStyle stylPrvehoStlpca = workbook.createCellStyle();
		DataFormat formatDate = workbook.createDataFormat();
		stylPrvehoStlpca.setFont(hlavickaFont);
		stylPrvehoStlpca.setAlignment(HorizontalAlignment.CENTER);
		stylPrvehoStlpca.setDataFormat(formatDate.getFormat("dd.MM.yyyy"));

		Row prvyRiadokHlavicky = sheet.createRow(0);

		for (int i = 0; i < firstRow.size(); i++) {
			Cell cell = prvyRiadokHlavicky.createCell(i+1);
			cell.setCellValue(firstRow.get(i));
			cell.setCellStyle(stylHlavicky);
		}

		for(Nakup nakup : vsetkyNakupy){
			Date casNakupu = nakup.getDokladNakupu().getCasNakupu();
			if(!predajnostiTovarovZaDen.containsKey(dateFormat.format(casNakupu))) {
				predajnostiTovarovZaDen.put(dateFormat.format(casNakupu), new TreeMap<>(Comparator.naturalOrder()));
			}
		}


		for(String datum : predajnostiTovarovZaDen.keySet()) {
			for (Nakup nakupy : vsetkyNakupy){
				for (NakupenyTovar nakupenyTovar : nakupy.getNakupenyTovar()){
					if (contains(vybraneTovary,nakupenyTovar)){
						predajnostiTovarovZaDen.get(datum).put(nakupenyTovar.getTovarName(),0f);
					}
				}
			}
		}

		for(String datum : predajnostiTovarovZaDen.keySet()) {
			List<Nakup> nakupyVDanyDen = Database.getNakupy(dateFormat.parse(datum));
			for (Nakup nakup : nakupyVDanyDen){
				for (NakupenyTovar nakupenyTovar : nakup.getNakupenyTovar()){
					if (contains(vybraneTovary,nakupenyTovar)){
						if(!predajnostiTovarovZaDen.get(datum).containsKey(nakupenyTovar.getTovarName())){
							predajnostiTovarovZaDen.get(datum).put(nakupenyTovar.getTovarName(),nakupenyTovar.getNakupeneMnozstvo());
						}

						else {
							predajnostiTovarovZaDen.get(datum).put(nakupenyTovar.getTovarName(),nakupenyTovar.getNakupeneMnozstvo() + predajnostiTovarovZaDen.get(datum).get(nakupenyTovar.getTovarName()));
						}
					}

				}
			}
		}

		int cisloRiadka = 1;
		for (String datum : predajnostiTovarovZaDen.keySet()){
			Row riadok = sheet.createRow(cisloRiadka++);

			Cell cell = riadok.createCell(0);
			cell.setCellValue(datum);
			cell.setCellStyle(stylPrvehoStlpca);

			int cisloStlpca = 1;

			for (String tovarName : predajnostiTovarovZaDen.get(datum).keySet()){
				riadok.createCell(cisloStlpca).setCellValue(predajnostiTovarovZaDen.get(datum).get(tovarName));
				riadok.getCell(cisloStlpca).setCellStyle(stylCislo);
				cisloStlpca++;
			}
		}

		for (int i = 0; i <= firstRow.size(); i++) {
			sheet.autoSizeColumn(i);
		}

		FileOutputStream fileOut = new FileOutputStream(file);
		workbook.write(fileOut);
		fileOut.close();
		workbook.close();
	}

	/*
	 * https://stackoverflow.com/questions/58458419/create-java-apache-poi-line-chart-where-dates-appear-on-the-horizontal-x-axis
	 * EDITED
	 */
	private static void addLineChart(Sheet sheet, CellRangeAddress anchorRange,CellRangeAddress[] titleRanges, CellRangeAddress[] dataRanges, PresetColor[] lineColors) {

		XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
		XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, anchorRange.getFirstColumn(), anchorRange.getFirstRow(),anchorRange.getLastColumn(),anchorRange.getLastRow());
		XSSFChart chart = drawing.createChart(anchor);
		XDDFChartLegend legend = chart.getOrAddLegend();
		legend.setPosition(LegendPosition.BOTTOM);

		XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);

		XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
		leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
		leftAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
		XDDFChartData data = chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
		data.setVaryColors(false);

		XDDFDataSource<Double> cat = XDDFDataSourcesFactory.fromNumericCellRange((XSSFSheet) sheet, dataRanges[0]);
		for (int s = 1; s < dataRanges.length; s++) {
			XDDFNumericalDataSource<Double> ser = XDDFDataSourcesFactory.fromNumericCellRange((XSSFSheet) sheet, dataRanges[s]);
			XDDFChartData.Series series = data.addSeries(cat, ser);
			series.setTitle(
				sheet.getRow(titleRanges[s].getFirstRow()).getCell(titleRanges[s].getFirstColumn()).getStringCellValue(),
				new CellReference(sheet.getSheetName(), titleRanges[s].getFirstRow(), titleRanges[s].getFirstColumn(), true, true)
			);
			if (series instanceof XDDFLineChartData.Series) {
			    ((XDDFLineChartData.Series)series).setSmooth(false);
			}
		}
		chart.plot(data);

		for (int s = 0; s < data.getSeriesCount(); s++) {
			solidLineSeries(data, s, lineColors[s]);
		}

		if (chart.getCTChart().getPlotArea().getCatAxList().size() > 0) {
			chart.getCTChart().getPlotArea().getCatAxArray(0).addNewAuto().setVal(false);
			chart.getCTChart().getPlotArea().getCatAxArray(0).addNewNumFmt().setSourceLinked(true);
			chart.getCTChart().getPlotArea().getCatAxArray(0).getNumFmt().setFormatCode("");
		} else if (chart.getCTChart().getPlotArea().getDateAxList().size() > 0) {
			chart.getCTChart().getPlotArea().getDateAxArray(0).addNewAuto().setVal(false);
			chart.getCTChart().getPlotArea().getDateAxArray(0).addNewNumFmt().setSourceLinked(true);
			chart.getCTChart().getPlotArea().getDateAxArray(0).getNumFmt().setFormatCode("");
		}

		CTMarker chartEndCoords = CTMarker.Factory.newInstance();
		chartEndCoords.setCol(12);
		chartEndCoords.setColOff(15);
		chartEndCoords.setRow(14);
		chartEndCoords.setRowOff(8);
		//drawing.getCTDrawing().getTwoCellAnchorArray(0).setFrom(chartStartCoords);
		drawing.getCTDrawing().getTwoCellAnchorArray(0).setTo(chartEndCoords);
	}

	/*
	 * https://stackoverflow.com/questions/58458419/create-java-apache-poi-line-chart-where-dates-appear-on-the-horizontal-x-axis
	 * EDITED
	 */
	private static void solidLineSeries(XDDFChartData data, int index, PresetColor color) {
		XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(color));
		XDDFLineProperties line = new XDDFLineProperties();
		line.setFillProperties(fill);
		XDDFChartData.Series series = data.getSeries(index);
		XDDFShapeProperties properties = series.getShapeProperties();
		if (properties == null) {
			properties = new XDDFShapeProperties();
		}
		properties.setLineProperties(line);
		series.setShapeProperties(properties);
	}

	/*
	 * https://stackoverflow.com/questions/58458419/create-java-apache-poi-line-chart-where-dates-appear-on-the-horizontal-x-axis
	 * EDITED
	 */
	private static void solidLineSeriesRandomColor(XDDFChartData data, int index) {
		XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(randomColor()));
		XDDFLineProperties line = new XDDFLineProperties();
		line.setFillProperties(fill);
		XDDFChartData.Series series = data.getSeries(index);
		XDDFShapeProperties properties = series.getShapeProperties();
		if (properties == null) {
			properties = new XDDFShapeProperties();
		}
		properties.setLineProperties(line);
		series.setShapeProperties(properties);
	}

	private static byte[] randomColor(){
		byte r = (byte)(Math.random() * 255);
		byte g = (byte)(Math.random() * 255);
		byte b = (byte)(Math.random() * 255);
		return new byte[]{r,g,b};
	}

	private static boolean contains(List<Tovar> tovary,Tovar t){
		for (Tovar tovar : tovary){
			if (tovar.getTovarName().equals(t.getTovarName())){
				return true;
			}
		}
		return false;
	}

}

