package escalonador;

import java.io.FileWriter;
import java.io.IOException;

public class LogFile {
	
	private FileWriter logFile;
	
	public LogFile(int quantum) throws IOException {
		String filename = quantum < 10 ? ("log0" +quantum+ ".txt") : ("log" +quantum+ ".txt");
		logFile = new FileWriter("src/processos/" +filename);
	}
	
	public void addMessage(String message) throws IOException {
		logFile.write(message);
	}
	
	public void closeLogFile() throws IOException {
		logFile.close();
	}
}
