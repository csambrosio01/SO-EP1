package escalonador;
public class Log {
	private static final String 
		LOADING_PROCESS = "Carregando %s\n",
		RUNNING_PROCESS = "Executando %s\n",
		ENDING_PROCESS = "%s terminado. X=%s. Y=%s\n",
		INTERRUPTING_PROCESS_1 = "Interrompendo %s após %s instrução\n",
		INTERRUPTING_PROCESS_2 = "Interrompendo %s após %s instruções\n",
		IO_STARTED = "E/S iniciada em %s\n",
		AVERAGE_EXCHANGES = "MEDIA DE TROCAS: %s\n",
		INSTRUCTION_AVERAGE = "MEDIA DE INSTRUCOES: %s\n",
		QUANTUM = "QUANTUM: %s";
	
	
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
