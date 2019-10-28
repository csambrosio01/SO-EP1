package ioDatas;

import launcher.Escalonador;

import java.io.IOException;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class Log {

	private PrintStream logFile;
	private ResourceBundle bundle;

	private static Log logInstance;
	private MessageFormat formatter;

	/**
	 * Constructor of Log
	 * @throws IOException
	 */
	private Log() throws IOException {
		Locale ptBR = new Locale("pt","BR");
		this.bundle = ResourceBundle.getBundle("resources/messages", ptBR);

		formatter = new MessageFormat("", Locale.US);

		int quantum = Escalonador.quantum;

		String filename = quantum < 10 ? ("log0" +quantum+ ".txt") : ("log" +quantum+ ".txt");
		logFile = new PrintStream("src/saveLogFile/" +filename);
	}

	/**
	 * Get a instance of logFile (it is Singleton)
	 * @return a logFile instance
	 * @throws IOException
	 */
	public static Log getInstance () throws IOException {
		if (logInstance == null) logInstance = new Log();
		
		return logInstance;
	}

	/**
	 * Add messages in logFile
	 * @param message is a model of message
	 * @param args are the arguments of message
	 */
	public void addMessage(String message, Object... args) {
		formatter.applyPattern(bundle.getString(message));
		logFile.println(formatter.format(args));
	}

	/**
	 * Close logFile
	 */
	public void closeLogFile() {
		logFile.close();
	}
}
