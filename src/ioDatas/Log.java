package ioDatas;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Log {

	private PrintStream logFile;
	private ResourceBundle bundle;

	private static Log logInstance;

	private Log() throws IOException {
		Locale ptBR = new Locale("pt","BR");
		this.bundle = ResourceBundle.getBundle("messages", ptBR);

		Scanner scanner = new Scanner(new File("src/processos/quantum.txt"));
		int quantum = scanner.nextInt();

		String filename = quantum < 10 ? ("log0" +quantum+ ".txt") : ("log" +quantum+ ".txt");
		logFile = new PrintStream("src/processos/" +filename);
	}
	
	public static Log getInstance () throws IOException {
		if (logInstance == null) logInstance = new Log();
		
		return logInstance;
	}
	
	public void addMessage(String message, Object... args) throws IOException {
		MessageFormat formatter = new MessageFormat("");
		formatter.applyPattern(bundle.getString(message));
		logFile.println(formatter.format(args));
	}
	
	public void closeLogFile() throws IOException {
		logFile.close();
	}
}
