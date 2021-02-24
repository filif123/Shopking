/**
 * 
 */
package sk.shopking.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * 
 *@author https://www.daniweb.com/programming/software-development/threads/83331/how-to-stop-opening-multiple-instance-for-a-jar-file
 *
 *
 */
public class ControlInstances{
	
	private static RandomAccessFile randomAccessFile;

	public static boolean isApplicationStarted() {
		try {
			File lockFile = new File(XMLParser.cestaKuKlientovi,".lock");
			if (lockFile.exists())
		        lockFile.delete();
		    FileOutputStream lockFileOS = new FileOutputStream(lockFile);
		    lockFileOS.close();
		    randomAccessFile = new RandomAccessFile(lockFile,"rw");
		    FileChannel lockChannel = randomAccessFile.getChannel();
		    FileLock lock = lockChannel.tryLock();
		    if (lock == null) {
			    throw new Exception("Unable to obtain lock");
			} 
		    return false;
		}catch(Exception e) {
			return true;
		}
	}
	
}
