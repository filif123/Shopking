package sk.shopking;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import sk.shopking.tools.Database;

import java.util.List;

public class ServiceDennaZavierka extends Service<List<DennaZavierka>> {
    @Override
    protected Task<List<DennaZavierka>> createTask() {
        return new Task<List<DennaZavierka>>() {
            @Override
            protected List<DennaZavierka> call() {
                return Database.getDenneZavierky();
            }
        };
    }
}
