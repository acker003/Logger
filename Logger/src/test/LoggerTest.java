package test;

import logger.MyLogger;

public class LoggerTest {

	public static void main(String[] args) {
		MyLogger log1 = MyLogger.getLogger("Log1");
		MyLogger log2 = MyLogger.getLogger("Log2");
		MyLogger.setLogLevel(MyLogger.LOG_LEVEL_3);
		MyLogger.setLogMode(MyLogger.LOG_BOTH);
		log1.log("Test1", 1);
		log1.log("Test2", 2);
		log1.log("Test3", 3);
		log2.log("Test1", 1);
		log2.setPersonalLogFile("MyPersonalLog.txt");
		log2.log("Test2", 2);
		log2.log("Test3", 3);
		
		
	}

}
