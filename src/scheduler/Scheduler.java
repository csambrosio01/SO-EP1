package scheduler;

import ioDatas.Log;
import launcher.Escalonador;

import java.io.IOException;

public class Scheduler {

    private Log logger;
    private double changes = 0;
    private double instructionsRan = 0;

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

            instructionsRan += i;

            logger.addMessage(i == 1 ? "INTERRUPTING_PROCESS_1" : "INTERRUPTING_PROCESS_2", process.getName(), i);

            if (!roundRobin) {
                processPCB.increaseProcessQuantum();
                processPCB.decreaseTwoCredits();
            }

            if (!processEnd) {
                if (processIO) {
                    process.setState(State.BLOCKED);
                    processPCB.setWaitTo2();
                    ProcessList.decreaseBlockedListWait(roundRobin);
                    ProcessList.addBlockedProcess(processPCB);
                } else {
                    if (roundRobin) {
                        process.setState(State.READY);
                        ProcessList.addReadyProcessInLastPosition(processPCB);
                        ProcessList.decreaseBlockedListWait(roundRobin);
                    } else {
                        ProcessList.decreaseBlockedListWait(roundRobin);
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
        int numOfProcess = ProcessTable.processTable.size();

        while (ProcessTable.processTable.size() != 0) {
            if (!ProcessList.readyList.isEmpty()) {
                if (ProcessList.allProcessInReadyListWithZEROCredit()) {
                    if (ProcessList.blockedList.isEmpty() && !ProcessList.allProcessInReadyListWithZEROPriority()) {
                        ProcessList.resetReadyList();
                    } else {
                        executeProcess(ProcessList.removeNextInReadyList(), true);
                        changes++;
                    }
                } else {
                    executeProcess(ProcessList.removeNextInReadyList(), false);
                    changes++;
                }
            } else {
                ProcessList.decreaseBlockedListWait(false);
            }
        }

        logger.addMessage("AVERAGE_EXCHANGES", changes / numOfProcess);
        logger.addMessage("INSTRUCTION_AVERAGE", instructionsRan / changes);
        logger.addMessage("QUANTUM", Escalonador.quantum);
    }

}
