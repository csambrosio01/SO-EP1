public class Log {
	private static final String 
		LOADING_PROCESS = "Carregando %s",
		RUNNING_PROCESS = "Executando %s",
		ENDING_PROCESS = "%s terminado. X=%s. Y=%s",
		INTERRUPTING_PROCESS_1 = "Interrompendo %s ap�s %s instru��o",
		INTERRUPTING_PROCESS_2 = "Interrompendo %s ap�s %s instru��es",
		IO_STARTED = "E/S iniciada em %s",
		AVERAGE_EXCHANGES = "MEDIA DE TROCAS: %s",
		INSTRUCTION_AVERAGE = "MEDIA DE INSTRUCOES: %s",
		QUANTUM = "QUANTUM: %s";
	
	
	public static String loadingProcessMessage(int testeName) {
		return String.format(LOADING_PROCESS, testeName);
	}
	
	public static String runningProcessMessage(int testeName) {
		return String.format(RUNNING_PROCESS, testeName);
	}
	
	public static String endingProcessMessage(int testeName, int registerX, int registerY) {
		return String.format(ENDING_PROCESS, testeName, registerX, registerY);
	}
	
	public static String interruptingProcessMessage(int testeName, int numberOfInstructions) {
		String usedString = numberOfInstructions <= 1 ? INTERRUPTING_PROCESS_1 : INTERRUPTING_PROCESS_2;
		return String.format(usedString, testeName, numberOfInstructions);
	}
	
	public static String IOMessage(int testeName) {
		return String.format(IO_STARTED, testeName);
	}
	
	public static String averageExchangesMessage(int averageExchanges) {
		return String.format(AVERAGE_EXCHANGES, averageExchanges);
	}
	
	public static String instructionAverageMessage(int instructionAverage) {
		return String.format(INSTRUCTION_AVERAGE, instructionAverage);
	}
	
	public static String quantumMessage(int quantum) {
		return String.format(QUANTUM, quantum);
	}
}
