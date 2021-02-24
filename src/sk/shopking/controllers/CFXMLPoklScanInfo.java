package sk.shopking.controllers;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sk.shopking.Tovar;
import sk.shopking.TovarZlavaCena;
import sk.shopking.TovarZlavaMnozstvo;
import sk.shopking.tools.*;

/**
 * @author Filip
 *
 */
public class CFXMLPoklScanInfo implements Initializable{

    private final List<Tovar> vsetkyTovary = Database.getTovary();
    private BarcodeScanner scanner;
    private GetDataFromAndroidPokladnik getDataFromAndroidPokladnik;


    @FXML private Label jlStav,jlNazov,jlCena,jlPLU,jlEAN;

    private final ScanInfoListener scanInfoListener = new ScanInfoListener();
    private final GetDataFromAndroidPokladnikListener getDataFromAndroidPokladnikListener = new AppInfoListener();

    @Override
    public void initialize(URL url, ResourceBundle rb) {


    }

    @FXML
    private void jbOKAction (ActionEvent event) {
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

    class ScanInfoListener implements BarcodeScannerListener{

        @Override
        public void barcodeEvent(String barcode) {
            Platform.runLater(() -> {
                for (Tovar tovar : vsetkyTovary) {
                    if (tovar.getTovarEAN() == Long.parseLong(barcode)) {
                        jlStav.setStyle("-fx-text-fill: green;");
                        jlStav.setText("Oskenované");

                        jlNazov.setText(tovar.getTovarName());
                        DecimalFormat df = new DecimalFormat("0.00");
                        if (tovar instanceof TovarZlavaCena){
                            TovarZlavaCena tc = (TovarZlavaCena)tovar;
                            jlCena.setText(df.format(tc.getNovaCena()));
                        }
                        else if (tovar instanceof TovarZlavaMnozstvo){
                            TovarZlavaMnozstvo tm = (TovarZlavaMnozstvo) tovar;
                            jlCena.setText(df.format(tm.getTovarJednotkovaCena()));
                        }
                        else {
                            jlCena.setText(new DecimalFormat("0.00").format(tovar.getTovarJednotkovaCena()));
                        }
                        jlEAN.setText("" + tovar.getTovarEAN());
                        jlPLU.setText("" + tovar.getTovarPLU());

                        return;
                    }

                }
                jlStav.setStyle("-fx-text-fill: red;");
                jlStav.setText("Oskenovaný tovar nenájdený");

                jlNazov.setText("Nezadané");
                jlCena.setText("0,00");
                jlEAN.setText("Nezadané");
                jlPLU.setText("Nezadané");
            });
        }
    }

    class AppInfoListener implements GetDataFromAndroidPokladnikListener{

        @Override
        public void receivedBarcodeEvent(String barcode) {
            Platform.runLater(() -> {
                for (Tovar tovar : vsetkyTovary) {
                    if (tovar.getTovarEAN() == Long.parseLong(barcode)) {
                        jlStav.setStyle("-fx-text-fill: green;");
                        jlStav.setText("Oskenované");

                        jlNazov.setText(tovar.getTovarName());
                        jlCena.setText(new DecimalFormat("0.00").format(tovar.getTovarJednotkovaCena()));
                        jlEAN.setText("" + tovar.getTovarEAN());
                        jlPLU.setText("" + tovar.getTovarPLU());

                        return;
                    }

                }
                jlStav.setStyle("-fx-text-fill: red;");
                jlStav.setText("Oskenovaný tovar nenájdený");

                jlNazov.setText("Nezadané");
                jlCena.setText("0,00");
                jlEAN.setText("Nezadané");
                jlPLU.setText("Nezadané");
            });
        }
    }
}
