package ioDatas;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class Log {

	private PrintStream logFile;
	private static Log logInstance;

	private static final String 
		LOADING_PROCESS = "Carregando %s",
		RUNNING_PROCESS = "Executando %s",
		ENDING_PROCESS = "%s terminado. X=%s. Y=%s",
		INTERRUPTING_PROCESS_1 = "Interrompendo %s após %s instrução",
		INTERRUPTING_PROCESS_2 = "Interrompendo %s após %s instruções",
		IO_STARTED = "E/S iniciada em %s",
		AVERAGE_EXCHANGES = "MEDIA DE TROCAS: %s",
		INSTRUCTION_AVERAGE = "MEDIA DE INSTRUCOES: %s",
		QUANTUM = "QUANTUM: %s";

	private Log() throws IOException {
		Scanner scanner = new Scanner(new File("src/processos/quantum.txt"));
		int quantum = scanner.nextInt();

		String filename = quantum < 10 ? ("log0" +quantum+ ".txt") : ("log" +quantum+ ".txt");
		logFile = new PrintStream("src/processos/" +filename);
	}
	
	public static Log getInstance () throws IOException {
		if (logInstance == null) logInstance = new Log();
		
		return logInstance;
	}
	
	public void addMessage(String message) throws IOException {
		logFile.println(message);
	}
	
	public void closeLogFile() throws IOException {
		logFile.close();
	}
	
	
	public static String loadingProcessMessage(String testName) {
		return String.format(LOADING_PROCESS, testName);
	}
	
	public static String runningProcessMessage(String testName) {
		return String.format(RUNNING_PROCESS, testName);
	}
	
	public static String endingProcessMessage(String testName, int registerX, int registerY) {
		return String.format(ENDING_PROCESS, testName, registerX, registerY);
	}
	
	public static String interruptingProcessMessage(String testName, int numberOfInstructions) {
		String usedString = numberOfInstructions <= 1 ? INTERRUPTING_PROCESS_1 : INTERRUPTING_PROCESS_2;
		return String.format(usedString, testName, numberOfInstructions);
	}
	
	public static String IOMessage(String testName) {
		return String.format(IO_STARTED, testName);
	}
	
	public static String averageExchangesMessage(double averageExchanges) {
		return String.format(AVERAGE_EXCHANGES, averageExchanges);
	}
	
	public static String instructionAverageMessage(double instructionAverage) {
		return String.format(INSTRUCTION_AVERAGE, instructionAverage);
	}
	
	public static String quantumMessage(int quantum) {
		return String.format(QUANTUM, quantum);
	}
}
