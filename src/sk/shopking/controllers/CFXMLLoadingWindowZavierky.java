package sk.shopking.controllers;


import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import sk.shopking.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author filip
 *
 */
public class CFXMLLoadingWindowZavierky implements Initializable {

    List<DennaZavierka> denneZavierky = new ArrayList<>();
    List<MesacnaZavierka> mesacneZavierky = new ArrayList<>();
    List<RocnaZavierka> rocneZavierky = new ArrayList<>();
    List<DenneZavierky> skupinaDennychZavierok = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        final ServiceDennaZavierka serviceDennaZavierka = new ServiceDennaZavierka();
        final ServiceMesacnaZavierka serviceMesacnaZavierka = new ServiceMesacnaZavierka();
        final ServiceRocnaZavierka serviceRocnaZavierka = new ServiceRocnaZavierka();
        final ServiceDenneZavierky serviceDenneZavierky = new ServiceDenneZavierky();

        serviceDennaZavierka.setOnSucceeded(event -> {
            denneZavierky = serviceDennaZavierka.getValue();
        });

        serviceMesacnaZavierka.setOnSucceeded(event -> {
            mesacneZavierky = serviceMesacnaZavierka.getValue();
        });

        serviceRocnaZavierka.setOnSucceeded(event -> {
            rocneZavierky = serviceRocnaZavierka.getValue();
        });

        serviceDenneZavierky.setOnSucceeded(event -> {
            skupinaDennychZavierok = serviceDenneZavierky.getValue();
        });

        serviceDennaZavierka.start();
        serviceMesacnaZavierka.start();
        serviceRocnaZavierka.start();
        serviceDenneZavierky.start();

    }

    public List<DennaZavierka> getDenneZavierky() {
        return denneZavierky;
    }

    public List<MesacnaZavierka> getMesacneZavierky() {
        return mesacneZavierky;
    }

    public List<RocnaZavierka> getRocneZavierky() {
        return rocneZavierky;
    }

    public List<DenneZavierky> getSkupinaDennychZavierok() {
        return skupinaDennychZavierok;
    }

}
