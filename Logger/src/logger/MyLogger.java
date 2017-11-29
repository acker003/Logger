package logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Stellt Funktionen zum Logging bereit.
 * @author Christian
 */
public class MyLogger {

	/**
	 * Kennzeichnet das Log-Level 1 (von 3).
	 */
	public static final int LOG_LEVEL_1 = 1;
	
	/**
	 * Kennzeichnet das Log-Level 2 (von 3).
	 */
	public static final int LOG_LEVEL_2 = 2;
	
	/**
	 * Kennzeichnet das Log-Level 3 (von 3).
	 */
	public static final int LOG_LEVEL_3 = 3;
	
	/**
	 * Log-Mode: Logging auf der Konsole.
	 */
	public static final int LOG_CONSOLE = 1;
	
	/**
	 * Log-Mode: Logging in eine Datei.
	 */
	public static final int LOG_FILE = 2;
	
	/**
	 * Log-Mode: Logging auf Konsole und in Datei.
	 */
	public static final int LOG_BOTH = 3;
	
	
	
	/**
	 * Format des Zeitstempels fuer die Log-Ausgaben.<br>
	 * dd.MM.YYYY HH:MM:ss.SSS<br>
	 * Bsp. 27.11.2017 13:48:40.000
	 */
	private static final SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.YYYY HH:MM:ss.SSS");
	
	/**
	 * Zaehler fuer die Anzahl an erfolgten Log-Ausgaben.
	 */
	private static int counter = 0;
	
	/**
	 * FileWriter der Log-Datei.
	 */
	private static FileWriter fw;
	
	/**
	 * Map aller Logger mit Namen als Key.
	 */
	private static Map<String, MyLogger> allLogger= new HashMap<String, MyLogger>();
	
	/**
	 * Aktuelles Log-Level. Standard: 3
	 */
	private static int logLevel = 3;
	
	/**
	 * Aktueller Log-Mode. Standard: Log on Console
	 */
	private static int logMode = LOG_CONSOLE;
	
	/**
	 * Gibt an, ob neue Logger zu Beginn aktiviert sein sollen.
	 */
	private static boolean initialActivated = true;
	
	
	
	/**
	 * FileWriter der eigenen zusaetzlichen Log-Datei.
	 */
	private FileWriter personalFW = null;
	
	/**
	 * Gibt an, ob der Logger aktiv ist.
	 */
	private boolean isActive = true;
	
	/**
	 * Gibt den Namen des Loggers an.
	 */
	private String name;
	
	
	
	/**
	 * Erstellt einen neuen Logger.<br>
	 * Kann nur von static getLogger aufgerufen werden.
	 * @param name Name des Loggers
	 */
	private MyLogger(String name) {
		this.name = name;
		this.isActive = initialActivated;
		try {
			fw = new FileWriter(new File("Logfile.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Erzeugt eine Log-Ausgabe.
	 * @param log Die zu loggende Nachricht
	 */
	public void log(String log) {
		log(log, LOG_LEVEL_3);
	}
	
	/**
	 * Erzeugt eine Log-Ausgabe.
	 * @param log Die zu loggende Nachricht
	 * @param level Das Log-Level
	 */
	public void log(String log, int level) {
		// Wenn der Logger aktiv ist und das Level gewuenscht ist
		if (isActive && logLevel >= level) {
			// Wenn in eine zusaetzliche Datei geloggt werden soll
			if (logMode != LOG_CONSOLE && personalFW != null) {
				this.logToPersonalFile(log);
			}
			// Unterscheidung des Log-Modus
			switch (logMode) {
			case LOG_CONSOLE:
				logToConsole(log, this);
				break;
			case LOG_FILE:
				logToFile(log, this);
				break;
			case LOG_BOTH:
				logToConsole(log, this);
				logToFile(log, this);
				break;
			default:
				System.err.println("Wrong LogMode: " + level);
			}
			// Erhoehung des Log-Counters
			counter++;
		}
	}
	
	/**
	 * Erzeugt eine Log-Ausgabe in die private Log-Datei.
	 * @param log Die zu loggende Nachricht
	 */
	private void logToPersonalFile(String log) {
		try {
			personalFW.write(getLogMessage(log, this));
			personalFW.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Aktiviert den Logger.
	 */
	public void activate() {
		this.isActive = true;
	}
	
	/**
	 * Deaktiviert den Logger.
	 */
	public void deactivate() {
		this.isActive = false;
	}
	
	/**
	 * Legt eine neue private Log-Datei an.
	 * @param string Der Name der Log-Datei
	 */
	public void setPersonalLogFile(String string) {
		setPersonalLogFile(new File(string));
	}
	
	/**
	 * Legt eine neue private Log-Datei an.
	 * @param file Die Log-Datei
	 */
	public void setPersonalLogFile(File file) {
		try {
			if (personalFW != null) personalFW.close();
			personalFW = new FileWriter(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Setzt eine Log-Ausgabe mit allen Informationen zusammen.<br>
	 * Ausgabe besteht aus:
	 * <ul>
	 *   <li>ID (aufsteigend zaehlend)</li>
	 *   <li>Name des Loggers</li>
	 *   <li>Zeitstempel</li>
	 *   <li>Die Nachricht</li>
	 * </ul>
	 * Alle Bestandteile sind mit Tabs getrennt.
	 * @param log Die zu loggende Nachricht
	 * @param logger Der zugehoerige Logger
	 * @return Die tatsaechlich zu loggende Ausgabe
	 */
	private static String getLogMessage(String log, MyLogger logger) {
		// Erzeugung des Zeitstempels
		Date d = new Date(System.currentTimeMillis());
		log = counter + "\t" + logger.name + "\t" + SDF.format(d) + "\t" + log + "\n";
		return log;
	}
	
	/**
	 * Schreibt eine Log-Ausgabe auf die Konsole.
	 * @param log Die zu loggende Nachricht
	 * @param logger Der zugehoerige Logger
	 */
	private static void logToConsole(String log, MyLogger logger) {
		System.out.print(getLogMessage(log, logger));
	}
	
	/**
	 * Schreibt eine Log-Ausgabe in die Log-Datei.
	 * @param log Die zu loggende Nachricht
	 * @param logger Der zugehoerige Logger
	 */
	private static void logToFile(String log, MyLogger logger) {
		try {
			fw.write(getLogMessage(log, logger));
			fw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Gibt den angefragten Logger zurueck oder erstellt einen neuen
	 * mit dem angegebenen Namen.<br>
	 * Dies ist die einzige Moeglichkeit einen neuen Logger anzulegen.
	 * @param logger Der Name des gewuenschten Loggers
	 * @return Der Logger mit dem Namen
	 */
	public static MyLogger getLogger(String logger) {
		// Wenn noch kein Logger mit dem Namen existiert
		if (!allLogger.containsKey(logger)) 
			// Wird er neu angelegt
			allLogger.put(logger, new MyLogger(logger));
		return allLogger.get(logger);
	}
	
	/**
	 * Setzt das Log-Level.
	 * Moeglich sind:
	 * <ul>
	 * <li>LOG_LEVEL_1</li>
	 * <li>LOG_LEVEL_2</li>
	 * <li>LOG_LEVEL_3</li>
	 * </ul>
	 * @param level Das gewuenschte Log-Level
	 */
	public static void setLogLevel(int level) {
		logLevel = level;
	}
	
	/**
	 * Aktiviert aller Logger.
	 */
	public static void activateAll() {
		for (MyLogger log : allLogger.values()) {
			log.activate();
		}
	}
	
	/**
	 * Deaktiviert alle Logger.
	 */
	public static void deactivateAll() {
		for (MyLogger log : allLogger.values()) {
			log.deactivate();
		}
	}
	
	/**
	 * Deaktiviert alle Logger und gibt an, ob die ab jetzt neu erzeugten Logger
	 * aktiviert oder deaktiviert sein sollen.
	 * @param fromNowOnActivated Wahrheitswert, ob neue Logger automatisch
	 * aktiviert sein sollen 
	 */
	public static void deactivateAll(boolean fromNowOnActivated) {
		initialActivated = fromNowOnActivated;
		deactivateAll();
	}
	
	/**
	 * Aktiviert die angegebenen Logger.
	 * @param names Die Namen der zu aktivierenden Logger
	 */
	public static void activate(String... names) {
		for (String name : names) {
			if (allLogger.containsKey(name)) allLogger.get(name).activate();
		}
	}
	
	/**
	 * Aktiviert die angegebenen Logger.
	 * @param logger Die Logger die aktiviert werden sollen
	 */
	public static void activate(MyLogger... logger) {
		for (MyLogger log : logger) log.activate();
	}
	
	/**
	 * Deaktiviert die angegebenen Logger.
	 * @param names Die Namen der zu deaktivierenden Logger
	 */
	public static void deactivate(String... names) {
		for (String name : names) {
			if (allLogger.containsKey(name)) allLogger.get(name).deactivate();
		}
	}
	
	/**
	 * Deaktiviert die angegebenen Logger.
	 * @param logger Die zu deaktivierenden Logger
	 */
	public static void deactivate(MyLogger... logger) {
		for (MyLogger log : logger) log.deactivate();
	}
	
	/**
	 * Setzt den Log-Modus.
	 * @param mode Der Log-Modus
	 */
	public static void setLogMode(int mode) {
		logMode = mode;
	}
	
	/**
	 * Setzt die Log-Datei.
	 * @param string Der Name der neuen Log-Datei
	 */
	public static void setLogFile(String string) {
		setLogFile(new File(string));
	}
	
	/**
	 * Setzt die Log-Datei.
	 * @param file Die neue Log-Datei
	 */
	public static void setLogFile(File file) {
		try {
			fw.close();
			fw = new FileWriter(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Schliesst alle vorhandenen FileWriter.
	 */
	public static void closeAll() {
		try {
			fw.close();
			for (MyLogger myLogger : allLogger.values()) {
				if (myLogger.personalFW != null) myLogger.personalFW.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
