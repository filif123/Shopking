package sk.shopking.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import sk.shopking.*;
import sk.shopking.tools.Database;
import sk.shopking.tools.ExportToExcel;

/**
 * Táto trieda je kontrolér pre okno zobrazenia štatistiky o výkonnosti obchodu.
 * @author Filip
 *
 */
public class CFXMLStatistika implements Initializable{
	
	private List<DennaZavierka> denneZavierky = Database.getDenneZavierky();
	private List<Nakup> vsetkyNakupy = Database.getNakupy();
	
	@FXML private Parent homeMenu;
	@FXML private CFXMLHlavneMenu homeMenuController;
	@FXML private LineChart<String, Number> jlchTrzby,jlchPredajnostTovaru;
	@FXML private RadioButton jrbShowDenneTrzby,jrbShowMesacneTrzby,jrbShowRocneTrzby;
	
	@FXML private TableView<Predajnost> jtPredajnost;
	@FXML private TableColumn<Predajnost, String> cNazov;
	@FXML private TableColumn<Predajnost, String> cCena;
	@FXML private TableColumn<Predajnost, String> cPredajnost;
	@FXML private TableColumn<Predajnost, String> cJednotka;

	@Override
    public void initialize(URL url, ResourceBundle rb) {
		homeMenuController.colorButton("jbStatistika");
		
		jlchTrzby.setAnimated(false);

		initializeDenneTrzbyGraf();
		
		//------------------------------2.STRANA----------------------------------------
		jtPredajnost.setPlaceholder(new Label("Žiaden tovar na zobrazenie"));
		jtPredajnost.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		ListChangeListener<Predajnost> multiSelection = changed -> {
			List<Tovar> itemsToGraph = new ArrayList<>();
			for(Predajnost p : changed.getList()) {
				itemsToGraph.add(p.getPredavajuciTovar());
			}
			initializeGrafPredajnost(itemsToGraph);
		};
		jtPredajnost.getSelectionModel().getSelectedItems().addListener(multiSelection);
		
		setCellValueTable();
		initializeTabPredajnost();
		jlchPredajnostTovaru.setAnimated(false);
		/*jtPredajnost.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
		            if(mouseEvent.getClickCount() == 2){
		            	initializeGrafPredajnost();
		            }
		        }
		    }
		});*/
		
    }
	
	public void initializeDenneTrzbyGraf() {
		XYChart.Series<String,Number> line = new XYChart.Series<>();
		
		ObservableList<Data<String,Number>> denneZavierkyObservableList = FXCollections.observableArrayList();
		
		TreeMap<String,Float> celkoveTrzbyZaDen = new TreeMap<>((o1, o2) -> {
			SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
			try {
				return formatter.parse(o1).compareTo(formatter.parse(o2));
			} catch (ParseException e) {
				return 0;
			}
		});
		
		for (DennaZavierka zavierka : denneZavierky) {
			celkoveTrzbyZaDen.put(new SimpleDateFormat("dd.MM.yyyy").format(zavierka.getCasZavierky()), 0f);
		}
		
		for (DennaZavierka zavierka : denneZavierky) {
			Float obrat = celkoveTrzbyZaDen.get(new SimpleDateFormat("dd.MM.yyyy").format(zavierka.getCasZavierky()));
			celkoveTrzbyZaDen.replace(new SimpleDateFormat("dd.MM.yyyy").format(zavierka.getCasZavierky()), obrat + zavierka.getSumaObrat());
		}
		
		for (String i : celkoveTrzbyZaDen.keySet()) {
			denneZavierkyObservableList.add(new XYChart.Data<>(i,celkoveTrzbyZaDen.get(i)));
		}
		
		line.setData(denneZavierkyObservableList);
		jlchTrzby.getData().clear();
		jlchTrzby.getData().add(line);
		
		line.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: #0066cc;");
		
		for (XYChart.Data<String, Number> data: line.getData()) {
            data.getNode().lookup(".chart-line-symbol").setStyle("-fx-background-color: #0066cc;");
		}
		
        for (Data<String, Number> vstup : line.getData()) {   
            Tooltip t = new Tooltip(new DecimalFormat("#.##").format(vstup.getYValue()) + "\n" + vstup.getXValue());
            Tooltip.install(vstup.getNode(), t);
        }
	}
	
	public void initializeMesacneTrzbyGraf() {
		XYChart.Series<String,Number> line = new XYChart.Series<>();

		ObservableList<Data<String,Number>> denneZavierkyObservableList = FXCollections.observableArrayList();
		
		TreeMap<String,Float> celkoveTrzbyZaDen = new TreeMap<>((o1, o2) -> {
			SimpleDateFormat formatter = new SimpleDateFormat("MM. yyyy");
			try {
				return formatter.parse(o1).compareTo(formatter.parse(o2));
			} catch (ParseException e) {
				return 0;
			}
		});
		
		for (DennaZavierka zavierka : denneZavierky) {
			celkoveTrzbyZaDen.put(new SimpleDateFormat("MM. yyyy").format(zavierka.getCasZavierky()), 0f);
		}
		
		for (DennaZavierka zavierka : denneZavierky) {
			Float obrat = celkoveTrzbyZaDen.get(new SimpleDateFormat("MM. yyyy").format(zavierka.getCasZavierky()));
			celkoveTrzbyZaDen.replace(new SimpleDateFormat("MM. yyyy").format(zavierka.getCasZavierky()), obrat + zavierka.getSumaObrat());
		}
		
		for (String i : celkoveTrzbyZaDen.keySet()) {
			denneZavierkyObservableList.add(new XYChart.Data<>(i,celkoveTrzbyZaDen.get(i)));
		}
		
		line.setData(denneZavierkyObservableList);
		jlchTrzby.getData().clear();
		jlchTrzby.getData().add(line);
		
		line.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: #0066cc;");
		
		for (XYChart.Data<String, Number> data: line.getData()) {
            data.getNode().lookup(".chart-line-symbol").setStyle("-fx-background-color: #0066cc;");
		}
		
        for (Data<String, Number> vstup : line.getData()) {   
            Tooltip t = new Tooltip(new DecimalFormat("#.##").format(vstup.getYValue()) + "\n" + vstup.getXValue());
            Tooltip.install(vstup.getNode(), t);
        }
	}
	
	public void initializeRocneTrzbyGraf() {
		XYChart.Series<String,Number> line = new XYChart.Series<>();

		ObservableList<Data<String,Number>> denneZavierkyObservableList = FXCollections.observableArrayList();
		
		TreeMap<String,Float> celkoveTrzbyZaDen = new TreeMap<>((o1, o2) -> {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
			try {
				return formatter.parse(o1).compareTo(formatter.parse(o2));
			} catch (ParseException e) {
				return 0;
			}
		});
		
		for (DennaZavierka zavierka : denneZavierky) {
			celkoveTrzbyZaDen.put(new SimpleDateFormat("yyyy").format(zavierka.getCasZavierky()), 0f);
		}
		
		for (DennaZavierka zavierka : denneZavierky) {
			Float obrat = celkoveTrzbyZaDen.get(new SimpleDateFormat("yyyy").format(zavierka.getCasZavierky()));
			celkoveTrzbyZaDen.replace(new SimpleDateFormat("yyyy").format(zavierka.getCasZavierky()), obrat + zavierka.getSumaObrat());
		}
		
		for (String i : celkoveTrzbyZaDen.keySet()) {
			denneZavierkyObservableList.add(new XYChart.Data<>(i, celkoveTrzbyZaDen.get(i)));
		}
		
		line.setData(denneZavierkyObservableList);
		jlchTrzby.getData().clear();
		jlchTrzby.getData().add(line);
		
		line.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: #0066cc;");
		
		for (XYChart.Data<String, Number> data: line.getData()) {
            data.getNode().lookup(".chart-line-symbol").setStyle("-fx-background-color: #0066cc;");
		}
		
        for (Data<String, Number> vstup : line.getData()) {   
            Tooltip t = new Tooltip(new DecimalFormat("#.##").format(vstup.getYValue()) + "\n" + vstup.getXValue());
            Tooltip.install(vstup.getNode(), t);
        }
	}
	
	@FXML
	private void jbExportTrzbyAction(ActionEvent event) throws IOException {
		FileChooser fileChooser = new FileChooser();
	    FileChooser.ExtensionFilter xlsx = new FileChooser.ExtensionFilter("Zošit programu Excel", "*.xlsx");
	    fileChooser.getExtensionFilters().add(xlsx);
        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile != null) {
        	List<DenneZavierky> denneZavierkyDB = Database.getSkupinyDennychZavierok();
        	List<DenneZavierky> denneZavierky = new ArrayList<>();
        	TreeMap<String,DenneZavierky> celkoveTrzbyList = new TreeMap<>((o1, o2) -> {
				SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
				try {
					return formatter.parse(o1).compareTo(formatter.parse(o2));
				} catch (ParseException e) {
					return 0;
				}
			});
        	
        	for (DenneZavierky dennaZavierka : denneZavierkyDB) {
        		celkoveTrzbyList.put(new SimpleDateFormat("dd.MM.yyyy").format(dennaZavierka.getDatumZavierok()), dennaZavierka);
			}
        	
        	for (String i : celkoveTrzbyList.keySet()) {
        		denneZavierky.add(celkoveTrzbyList.get(i));
    		}
        	
        	ExportToExcel.exportTrzby(selectedFile, denneZavierky);
		}
	}
	
	@FXML
	private void jbObnovitTrzbyAction(ActionEvent event) {
		denneZavierky = Database.getDenneZavierky();
		obnovitTrzby();
	}
	
	private void obnovitTrzby() {
		if (jrbShowDenneTrzby.isSelected()) {
			initializeDenneTrzbyGraf();
		}
		else if (jrbShowMesacneTrzby.isSelected()) {
			initializeMesacneTrzbyGraf();
		}
		else {
			initializeRocneTrzbyGraf();
		}
	}
	
	@FXML
	private void jrbShowDenneTrzbyAction(ActionEvent event) {
		initializeDenneTrzbyGraf();
	}
	
	@FXML
	private void jrbShowMesacneTrzbyAction(ActionEvent event) {
		initializeMesacneTrzbyGraf();
	}
	
	@FXML
	private void jrbShowRocneTrzbyAction(ActionEvent event) {
		initializeRocneTrzbyGraf();
	}
	
	//------------------------------2.STRANA----------------------------------------
	
	private void setCellValueTable() {
		cNazov.setCellValueFactory(new PropertyValueFactory<>("nazovProperty"));
		cCena.setCellValueFactory(new PropertyValueFactory<>("cenaProperty"));
		cPredajnost.setCellValueFactory(new PropertyValueFactory<>("predajnostProperty"));
		cJednotka.setCellValueFactory(new PropertyValueFactory<>("jednotkaProperty"));
	}
	
	private void initializeTabPredajnost() {
		jtPredajnost.getItems().setAll(Database.getPredajnostTovarov());
	}
	
	private void initializeGrafPredajnost(List<Tovar> daneTovary) {
		List<XYChart.Series<String,Number>> linieList = new ArrayList<>();
		List<TreeMap<String,Float>> predajnostiTovarovZaDen = new ArrayList<>();
		
		List<ObservableList<Data<String,Number>>> predajnostObservableList = new ArrayList<>();
		
		for (int i = 0 ; i< daneTovary.size() ; i++) {
			predajnostObservableList.add(FXCollections.observableArrayList());
			predajnostiTovarovZaDen.add(new TreeMap<>((o1, o2) -> {
				SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
				try {
					return formatter.parse(o1).compareTo(formatter.parse(o2));
				} catch (ParseException e) {
					return 0;
				}
			}));
		}
		
		for (int i = 0 ; i < daneTovary.size() ; i++) {
			for (Nakup nakup : vsetkyNakupy) {
				predajnostiTovarovZaDen.get(i).put(new SimpleDateFormat("dd.MM.yyyy").format(nakup.getDokladNakupu().getCasNakupu()), 0f);
			}
		}
		
		for (int i = 0 ; i < daneTovary.size() ; i++) {
			for (Nakup nakup : vsetkyNakupy) {
				for (NakupenyTovar nakupenyTovar : nakup.getNakupenyTovar()) {
					if (nakupenyTovar.getTovarPLU() == daneTovary.get(i).getTovarPLU()) {
						Float predavanostTovaru = predajnostiTovarovZaDen.get(i).get(new SimpleDateFormat("dd.MM.yyyy").format(nakup.getDokladNakupu().getCasNakupu()));
						predajnostiTovarovZaDen.get(i).replace(new SimpleDateFormat("dd.MM.yyyy").format(nakup.getDokladNakupu().getCasNakupu()), predavanostTovaru + nakupenyTovar.getNakupeneMnozstvo());
					}
						
				}
			}
		}
		
		for (int i = 0; i < daneTovary.size(); i++) {
			for (String s : predajnostiTovarovZaDen.get(i).keySet()) {
				predajnostObservableList.get(i).add(new XYChart.Data<>(s,predajnostiTovarovZaDen.get(i).get(s)));
			}
		}
		
		for (int i = 0; i< predajnostObservableList.size();i++) {
			linieList.add(i,new XYChart.Series<>());
			linieList.get(i).setData(predajnostObservableList.get(i));
			linieList.get(i).setName(daneTovary.get(i).getTovarName());
		}
		jlchPredajnostTovaru.getData().clear();
		jlchPredajnostTovaru.getData().addAll(linieList);

		for (XYChart.Series<String, Number> stringNumberSeries : linieList) {
			for (Data<String, Number> vstup : stringNumberSeries.getData()) {
				Tooltip t = new Tooltip(stringNumberSeries.getName() + "\n" + vstup.getXValue() + "\n" + new DecimalFormat("#.##").format(vstup.getYValue()));
				Tooltip.install(vstup.getNode(), t);
			}
		}
	}
	
	@FXML
	private void jbExportPredajnostAction(ActionEvent event) throws IOException, ParseException {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter xlsx = new FileChooser.ExtensionFilter("Zošit programu Excel", "*.xlsx");
		fileChooser.getExtensionFilters().add(xlsx);
		File selectedFile = fileChooser.showSaveDialog(null);
		if (selectedFile != null) {

			List<Predajnost> predajnostVybranychTovarov = jtPredajnost.getSelectionModel().getSelectedItems();
			List<Tovar> vybraneTovary = new ArrayList<>();
			for (Predajnost predajnost : predajnostVybranychTovarov) {
				vybraneTovary.add(predajnost.getPredavajuciTovar());
			}

			/*List<NakupenyTovar> nakupenyTovarList = new ArrayList<NakupenyTovar>();
			TreeMap<String,NakupenyTovar> map = new TreeMap<String,NakupenyTovar>(new Comparator<String>() {

				@Override
				public int compare(String o1, String o2) {
					SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
					try {
						return formatter.parse(o1).compareTo(formatter.parse(o2));
					} catch (ParseException e) {
						return 0;
					}
				}
			});

			for (Nakup nakup : vsetkyNakupyDB) {
				for (NakupenyTovar nakupenyTovar : nakup.getNakupenyTovar()) {
					map.put(new SimpleDateFormat("dd.MM.yyyy").format(nakup.getDokladNakupu().getCasNakupu()), nakupenyTovar);
				}
			}

			for (String i : map.keySet()) {
				nakupenyTovarList.add(map.get(i));
			}*/

			ExportToExcel.exportPredajnost(selectedFile, vybraneTovary);
		}
	}
	
	@FXML
	private void jbObnovitPredajnostAction(ActionEvent event) {
		vsetkyNakupy = Database.getNakupy();
		initializeTabPredajnost();
		
	}
	
}
