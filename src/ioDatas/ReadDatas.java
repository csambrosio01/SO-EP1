package ioDatas;

import scheduler.PCB;
import scheduler.Process;
import scheduler.State;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReadDatas {

    private String processDirectoryPath = "src/processos/";

    public int readQuantum() throws IOException {
        Scanner scanner = new Scanner(new File(processDirectoryPath + "quantum.txt"));
        return scanner.nextInt();
    }

    public List<Integer> readPriorities() throws IOException {
        List<Integer> priorities = new ArrayList<>();

        Scanner prioritiesFile = new Scanner(new File(processDirectoryPath + "prioridades.txt"));

        while (prioritiesFile.hasNext()) {
            String priority = prioritiesFile.next();
            priorities.add(Integer.parseInt(priority));
        }

        return priorities;
    }

    public PCB readFile(int proccessNumber, int processPriority) throws IOException {
        Log logger = Log.getInstance();

        String processPath = processDirectoryPath + (proccessNumber < 10 ? "0" + proccessNumber : proccessNumber) + ".txt";
        Scanner scanner = new Scanner(new File(processPath));

        List<String> instructions = new ArrayList<>();

        String fileName = scanner.nextLine();
        logger.addMessage("LOADING_PROCESS", fileName);

        while (scanner.hasNextLine()) {
            String content = scanner.nextLine();
            instructions.add(content);
        }

        Process process = new Process(fileName, instructions.toArray(new String[0]), State.READY);
        return new PCB(process, processPriority);
    }

}
