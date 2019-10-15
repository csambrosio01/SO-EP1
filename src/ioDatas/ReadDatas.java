package ioDatas;

import scheduler.*;
import scheduler.Process;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReadDatas {

    private String processDirectoryPath = "src/processos/";

    public int readQuantum() throws IOException {
        Scanner scanner = new Scanner(new File(processDirectoryPath + "quantum.txt"));
        int quantum = scanner.nextInt();
        scanner.close();
        return quantum;
    }

    public void readProcessFiles() throws IOException {
        List<Integer> priorities = readPriorities();
        for (int i = 1; i <= priorities.size(); i++) {
            PCB processPCB = readFile(i, priorities.get(i - 1));
            ProcessTable.addPCBonProcessTable(processPCB);
            ProcessList.addReadyProcess(processPCB);
        }
        logProcessName();
    }

    private List<Integer> readPriorities() throws IOException {
        List<Integer> priorities = new ArrayList<>();

        Scanner prioritiesFile = new Scanner(new File(processDirectoryPath + "prioridades.txt"));

        while (prioritiesFile.hasNext()) {
            String priority = prioritiesFile.next();
            priorities.add(Integer.parseInt(priority));
        }

        prioritiesFile.close();

        return priorities;
    }

    private PCB readFile(int processNumber, int processPriority) throws IOException {
        String processPath = processDirectoryPath + (processNumber < 10 ? "0" + processNumber : processNumber) + ".txt";
        Scanner scanner = new Scanner(new File(processPath));

        List<String> instructions = new ArrayList<>();

        String fileName = scanner.nextLine();

        while (scanner.hasNextLine()) {
            String content = scanner.nextLine();
            instructions.add(content);
        }

        Process process = new Process(fileName, instructions.toArray(new String[0]), State.READY);

        scanner.close();
        return new PCB(process, processPriority);
    }

    private void logProcessName() throws IOException {
        Log logger = Log.getInstance();
        for (PCB pcb : ProcessList.readyList) {
            logger.addMessage("LOADING_PROCESS", pcb.getProcess().getName());
        }
    }

}
