package sk.shopking;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import sk.shopking.tools.Database;

import java.util.List;

public class ServiceMesacnaZavierka extends Service<List<MesacnaZavierka>> {
    @Override
    protected Task<List<MesacnaZavierka>> createTask() {
        return new Task<List<MesacnaZavierka>>() {
            @Override
            protected List<MesacnaZavierka> call() {
                return Database.getMesacneZavierky();
            }
        };
    }
}
