package sk.shopking;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import sk.shopking.tools.Database;

import java.util.List;

public class ServiceRocnaZavierka extends Service<List<RocnaZavierka>> {
    @Override
    protected Task<List<RocnaZavierka>> createTask() {
        return new Task<List<RocnaZavierka>>() {
            @Override
            protected List<RocnaZavierka> call() {
                return Database.getRocneZavierky();
            }
        };
    }
}
