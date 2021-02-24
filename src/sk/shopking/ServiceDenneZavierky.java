package sk.shopking;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import sk.shopking.tools.Database;

import java.util.List;

public class ServiceDenneZavierky extends Service<List<DenneZavierky>> {
    @Override
    protected Task<List<DenneZavierky>> createTask() {
        return new Task<List<DenneZavierky>>() {
            @Override
            protected List<DenneZavierky> call() {
                return Database.getSkupinyDennychZavierok();
            }
        };
    }
}
