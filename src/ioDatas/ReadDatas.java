package ioDatas;

import scheduler.PCB;
import scheduler.Process;
import scheduler.State;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadDatas {

    private Log logger;

    public ReadDatas() throws IOException {
        logger = Log.getInstance();
    }

    public PCB readFile(int proccessNumber, int processPriority) throws Exception {
        String processPath = "src/processos/" + (proccessNumber < 10 ? "0" + proccessNumber : proccessNumber) + ".txt";
        BufferedReader reader = new BufferedReader(new FileReader(processPath));

        List<String> instructions = new ArrayList<>();

        String fileName = reader.readLine();
        logger.addMessage("LOADING_PROCESS", fileName);

        String content;
        while ((content = reader.readLine()) != null) {
            instructions.add(content);
        }

        Process process = new Process(fileName, instructions.toArray(new String[0]), State.READY);
        return new PCB(process, processPriority);
    }

}
