package launcher;

import ioDatas.Log;
import ioDatas.ReadDatas;
import scheduler.PCB;
import scheduler.ProcessList;
import scheduler.ProcessTable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Escalonador {

	public static int quantum = 3; //Set default quantum as 3

	private static Log logger;

	private static void readQuantum() throws IOException {
		Scanner scanner = new Scanner(new File("src/processos/quantum.txt"));
		quantum = scanner.nextInt();
	}

	private static List<Integer> readPriorities() throws IOException {
		List<Integer> priorities = new ArrayList<>();

		Scanner prioritiesFile = new Scanner(new File("src/processos/prioridades.txt"));

		while (prioritiesFile.hasNext()) {
			String priority = prioritiesFile.next();
			priorities.add(Integer.parseInt(priority));
		}

		return priorities;
	}

	private static void readProcessFiles(List<Integer> priorities) throws IOException {
		ReadDatas readDatas = new ReadDatas();

		for (int i = 1; i <= priorities.size(); i++) {
			PCB processPCB = readDatas.readFile(i, priorities.get(i - 1));
			ProcessTable.addPCBonProcessTable(processPCB);
			ProcessList.addReadyProcess(processPCB);
		}
	}

	public static void main(String[] args) throws IOException {
		readQuantum();
		logger = Log.getInstance();
	}

}
