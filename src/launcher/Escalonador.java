package launcher;

import ioDatas.Log;
import ioDatas.ReadDatas;
import scheduler.Scheduler;

import java.io.IOException;

public class Escalonador {

	public static int quantum = 3; //Set default quantum as 3

	public static void main(String[] args) throws IOException {
		ReadDatas readDatas = new ReadDatas();
		quantum = readDatas.readQuantum();

		Log logger = Log.getInstance();

		readDatas.readProcessFiles();

		Scheduler scheduler = new Scheduler();
		scheduler.run();

		logger.closeLogFile();
	}

}
