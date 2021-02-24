package sk.shopking.tools;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * @author Filip
 *
 */
public class SKLog {
	
	private final Logger logger;

    public SKLog(Logger logger) {
        this.logger = logger;
        logger.setLevel(Level.ALL);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        logger.addHandler(consoleHandler);

        try {
            Handler fileHandler = new FileHandler(AppSettings.cestaKuKlientovi + "\\logs\\logs.log", true);
            MyFormatter formatter = new MyFormatter();
            fileHandler.setFormatter(formatter);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setEncoding("UTF-8");
            logger.addHandler(fileHandler);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SKLog getLogger() {
        return new SKLog(Logger.getAnonymousLogger());
    }

    /*public void entering(String sourceClass, String sourceMethod){
        logger.entering(sourceClass, sourceMethod);
    }*/
    
    public void info(String msg){
        logger.info(msg);
    }
    
    public void log(Level level, String msg){
        logger.log(level,msg);
    }
    
    public void severe(String msg){
        logger.severe(msg);
    }
    
    public void warning(String msg){
        logger.warning(msg);
    }
    
    public Handler[] getHandlers(){
        return logger.getHandlers();
    }
    
    static class MyFormatter extends Formatter {
        private final DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        public String format(LogRecord record) {
            StringBuilder builder = new StringBuilder(1000);
            builder.append(df.format(new Date(record.getMillis()))).append(" - ");
            builder.append("[").append(record.getLevel()).append("] - ");
            builder.append(formatMessage(record));
            builder.append("\n");
            return builder.toString();
        }

        public String getHead(Handler h) {
            return super.getHead(h);
        }

        public String getTail(Handler h) {
            return super.getTail(h);
        }
    }
}
