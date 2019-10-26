package launcher;

import ioDatas.Log;
import ioDatas.ReadDatas;
import scheduler.Scheduler;

import java.io.IOException;

public class Escalonador {

	public static int quantum = 3; //Set default quantum as 3

	public static void main(String[] args) throws IOException {
		ReadDatas readDatas = new ReadDatas();

		// Read quantum
		quantum = readDatas.readQuantum();

		Log logger = Log.getInstance();

		// Ready priorities and process files
		readDatas.readProcessFiles();

		// Execute scheduler
		Scheduler scheduler = new Scheduler();
		scheduler.run();

		logger.closeLogFile();
	}

}
