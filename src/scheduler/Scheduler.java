package scheduler;

import ioDatas.Log;
import launcher.Escalonador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Scheduler {

    private Log logger;

    private List<Integer> instructionsRan = new ArrayList<>();

    public Scheduler() throws IOException {
        logger = Log.getInstance();
    }

    private void executeProcess(PCB processPCB) {
        boolean runAgain;
        do {
            runAgain = false;
            Process process = processPCB.getProcess();
            logger.addMessage("RUNNING_PROCESS", process.getName());
            process.setState(State.RUNNING);
            ProcessList.decreaseBlockedListWait();

            int instructionsToRun = Escalonador.quantum * processPCB.getProcessQuantum();

            boolean processEnd = false;
            boolean processIO = false;
            int i;
            loop: for (i = 0; i < instructionsToRun; i++) {
                Instruction instruction = process.getTypeOfInstruction(processPCB.getProgramCounter());
                String instructionExecutor = process.getInstruction(processPCB.getProgramCounter());
                processPCB.increaseProgramCounter();
                switch (instruction) {
                    case ATTRIBUTIONX:
                        String xAttribute = instructionExecutor.split("=")[1];
                        processPCB.setRegisterX(Integer.parseInt(xAttribute));
                        break;
                    case ATTRIBUTIONY:
                        String yAttribute = instructionExecutor.split("=")[1];
                        processPCB.setRegisterY(Integer.parseInt(yAttribute));
                        break;
                    case INTERRUPTION:
                        logger.addMessage("IO_STARTED", process.getName());
                        processIO = true;
                        i++;
                        break loop;
                    case END:
                        processEnd = true;
                        i++;
                        break loop;
                    default:
                        break;
                }
            }

            instructionsRan.add(i);

            logger.addMessage(i == 0 ? "INTERRUPTING_PROCESS_1" : "INTERRUPTING_PROCESS_2", process.getName(), i);
            processPCB.increaseProcessQuantum();
            processPCB.decreaseTwoCredits();

            if (!processEnd) {
                if (processIO) {
                    process.setState(State.BLOCKED);
                    processPCB.setWaitTo2();
                    ProcessList.addBlockedProcess(processPCB);
                } else {
                    if (ProcessList.shouldContinue(processPCB)) {
                        runAgain = true;
                    } else {
                        process.setState(State.READY);
                        ProcessList.addReadyProcess(processPCB);
                    }
                }
            } else {
                logger.addMessage("ENDING_PROCESS", process.getName(), processPCB.getRegisterX(), processPCB.getRegisterY());
                process.setState(State.FINISHED);
                ProcessTable.removePCBofProcessTable(processPCB);
            }
        } while (runAgain);
    }
}
