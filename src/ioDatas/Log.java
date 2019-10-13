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

	private Log() throws IOException {
		Locale ptBR = new Locale("pt","BR");
		this.bundle = ResourceBundle.getBundle("resources/messages", ptBR);

		int quantum = Escalonador.quantum;

		String filename = quantum < 10 ? ("log0" +quantum+ ".txt") : ("log" +quantum+ ".txt");
		logFile = new PrintStream("src/processos/" +filename);
	}
	
	public static Log getInstance () throws IOException {
		if (logInstance == null) logInstance = new Log();
		
		return logInstance;
	}
	
	public void addMessage(String message, Object... args) {
		MessageFormat formatter = new MessageFormat("");
		formatter.applyPattern(bundle.getString(message));
		logFile.println(formatter.format(args));
	}
	
	public void closeLogFile() {
		logFile.close();
	}
}
