package scheduler;

import ioDatas.Log;
import launcher.Escalonador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Scheduler {

    private Log logger;

    //TODO: Verify if changes, instructionsRan and average of instructions are calculated and showed correctly
    private double changes = -1;
    private List<Double> instructionsRan = new ArrayList<>();

    public Scheduler() throws IOException {
        logger = Log.getInstance();
    }

    private void executeProcess(PCB processPCB, boolean roundRobin) {
        boolean runAgain;
        do {
            runAgain = false;
            Process process = processPCB.getProcess();
            logger.addMessage("RUNNING_PROCESS", process.getName());
            process.setState(State.RUNNING);
            ProcessList.decreaseBlockedListWait();

            int instructionsToRun = Escalonador.quantum * (roundRobin ? 1 : processPCB.getProcessQuantum());

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

            instructionsRan.add((double) i);

            logger.addMessage(i == 1 ? "INTERRUPTING_PROCESS_1" : "INTERRUPTING_PROCESS_2", process.getName(), i);

            if (!roundRobin) {
                processPCB.increaseProcessQuantum();
                processPCB.decreaseTwoCredits();
            }

            if (!processEnd) {
                if (processIO) {
                    process.setState(State.BLOCKED);
                    processPCB.setWaitTo2();
                    ProcessList.addBlockedProcess(processPCB);
                } else {
                    if (roundRobin) {
                        process.setState(State.READY);
                        ProcessList.addReadyProcessInLastPosition(processPCB);
                    } else {
                        if (ProcessList.shouldContinue(processPCB)) runAgain = true;
                        else {
                            process.setState(State.READY);
                            ProcessList.addReadyProcess(processPCB);
                        }
                    }
                }
            } else {
                logger.addMessage("ENDING_PROCESS", process.getName(), processPCB.getRegisterX(), processPCB.getRegisterY());
                process.setState(State.FINISHED);
                ProcessTable.removePCBofProcessTable(processPCB);
            }
        } while (runAgain);
    }

    public void run() {
        while (ProcessTable.processTable.size() != 0) {
            if (!ProcessList.readyList.isEmpty()) {
                if (ProcessList.allProcessInReadyListWithZEROCredit()) {
                    if (ProcessList.blockedList.isEmpty() && !ProcessList.allProcessInReadyListWithZEROPriority()) {
                        ProcessList.resetReadyList();
                    } else {
                        changes++;
                        executeProcess(ProcessList.removeNextInReadyList(), true);
                    }
                } else {
                    changes++;
                    executeProcess(ProcessList.removeNextInReadyList(), false);
                }
            } else {
                ProcessList.decreaseBlockedListWait();
            }
        }

        double instructions = 0;
        for (Double instructionRan : instructionsRan) {
            instructions += instructionRan;
        }

        logger.addMessage("AVERAGE_EXCHANGES", changes);
        logger.addMessage("INSTRUCTION_AVERAGE", instructions / changes);
        logger.addMessage("QUANTUM", Escalonador.quantum);
    }

}
