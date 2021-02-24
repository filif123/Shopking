package sk.shopking.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sk.shopking.NakupenyTovar;
import sk.shopking.tools.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CFXMLPoklImportNakupu implements Initializable {

    private final List<NakupenyTovar> importovanyNakup = new ArrayList<>();
    private int idZakaznik;

    private BarcodeScanner scanner;
    private GetDataFromAndroidPokladnik getDataFromAndroidPokladnik;

    private final ScanInfoListener scanInfoListener = new ScanInfoListener();
    private final GetDataFromAndroidPokladnikListener getDataFromAndroidPokladnikListener = new AppInfoListener();

    @FXML
    private TextField jtfCode;
    @FXML private Label jlStav;
    @FXML private Button jbImport;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        jbImport.setDisable(true);

        jtfCode.textProperty().addListener((obs, oldText, newText) -> {
            if(!jtfCode.getText().isEmpty())
                search(newText);
        });
    }

    @FXML
    private void jbImportAction (ActionEvent event) {
        if (scanner != null) scanner.removeListener(scanInfoListener);
        getDataFromAndroidPokladnik.removeListener(getDataFromAndroidPokladnikListener);
        if (this.idZakaznik != 0){
            Database.setUkoncenyNakupZakaznika(this.idZakaznik);
        }
        FXMLTools.closeWindow(event);
    }

    @FXML
    private void jbStornoAction (ActionEvent event) {
        importovanyNakup.clear();
        if (scanner != null) scanner.removeListener(scanInfoListener);
        getDataFromAndroidPokladnik.removeListener(getDataFromAndroidPokladnikListener);

        FXMLTools.closeWindow(event);
    }

    public void setScanner(BarcodeScanner scanner) {
        this.scanner = scanner;
        scanner.addListener(scanInfoListener);
    }

    public void setApp(GetDataFromAndroidPokladnik getDataFromAndroidPokladnik) {
        this.getDataFromAndroidPokladnik = getDataFromAndroidPokladnik;
        getDataFromAndroidPokladnik.addListener(getDataFromAndroidPokladnikListener);
    }

    public List<NakupenyTovar> getNakup() {
        return importovanyNakup;
    }

    class ScanInfoListener implements BarcodeScannerListener {

        @Override
        public void barcodeEvent(String barcode) {
            Platform.runLater(() -> {
                search(barcode);
            });
        }
    }

    class AppInfoListener implements GetDataFromAndroidPokladnikListener {

        @Override
        public void receivedBarcodeEvent(String barcode) {
            Platform.runLater(() -> {
                search(barcode);
            });
        }
    }

    private void search(String barcode) {
        List<NakupenyTovar> nakup;
        jtfCode.setText(barcode);
        try {
            CFXMLPoklImportNakupu.this.idZakaznik = Integer.parseInt(barcode);
            if (!Database.isNakupZakaznikaUkonceny(CFXMLPoklImportNakupu.this.idZakaznik)) {
                nakup = Database.getNakupAppZakaznika(CFXMLPoklImportNakupu.this.idZakaznik);
                if (nakup == null || nakup.isEmpty()) {
                    jlStav.setStyle("-fx-text-fill: red;");
                    jlStav.setText("Nákup zákazníka nenájdený");
                    jbImport.setDisable(true);
                } else {
                    jlStav.setStyle("-fx-text-fill: green;");
                    jlStav.setText("Nákup priaty. Bude pridaných " + nakup.size() + " položiek");
                    importovanyNakup.clear();
                    importovanyNakup.addAll(nakup);
                    jbImport.setDisable(false);
                }
            } else {
                jlStav.setStyle("-fx-text-fill: red;");
                jlStav.setText("Tento nákup už bol uskutočnený");
                jbImport.setDisable(true);
            }

        } catch (NumberFormatException ignored) {
            jlStav.setStyle("-fx-text-fill: red;");
            jlStav.setText("Chybný čiarový kód");
            jbImport.setDisable(true);
        }
    }
}
