package launcher;

import ioDatas.Log;
import ioDatas.ReadDatas;
import scheduler.PCB;
import scheduler.ProcessList;
import scheduler.ProcessTable;
import scheduler.Scheduler;

import java.io.IOException;
import java.util.List;

public class Escalonador {

	public static int quantum = 3; //Set default quantum as 3

	private static Log logger;
	private static ReadDatas readDatas;

	private static void readProcessFiles(List<Integer> priorities) throws IOException {
		for (int i = 1; i <= priorities.size(); i++) {
			PCB processPCB = readDatas.readFile(i, priorities.get(i - 1));
			ProcessTable.addPCBonProcessTable(processPCB);
			ProcessList.addReadyProcess(processPCB);
		}
	}

	public static void main(String[] args) throws IOException {
		readDatas = new ReadDatas();
		quantum = readDatas.readQuantum();
		logger = Log.getInstance();

		List<Integer> priorities = readDatas.readPriorities();

		readProcessFiles(priorities);

		Scheduler scheduler = new Scheduler();
		scheduler.run();

		logger.closeLogFile();
	}

}
