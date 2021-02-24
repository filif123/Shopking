/**
 * 
 */
package sk.shopking.tools;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author Filip
 *
 */
public class SKLog {
	
	private Logger logger;

    public SKLog(Logger logger) {
        this.logger = logger;
        logger.setLevel(Level.ALL);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        logger.addHandler(consoleHandler);

        try {
            Handler fileHandler = new FileHandler(AppSettings.cestaKuKlientovi + "\\logs.log", true);
            SimpleFormatter simple = new SimpleFormatter();
            fileHandler.setFormatter(simple);
            fileHandler.setLevel(Level.ALL);
            logger.addHandler(fileHandler);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SKLog getLogger() {
        return new SKLog(Logger.getAnonymousLogger());
    }

    public void entering(String sourceClass, String sourceMethod){
        logger.entering(sourceClass, sourceMethod);
    }
    
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
}
