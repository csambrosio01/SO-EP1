package scheduler;

import ioDatas.Log;
import launcher.Escalonador;

import java.io.IOException;

public class Scheduler {

    private Log logger;
    private double changes = 0;
    private double instructionsRan = 0;
    private double quantunsRan = 0;

    /**
     * Constructor of class Scheduler
     * @throws IOException
     */
    public Scheduler() throws IOException {
        logger = Log.getInstance();
    }

    /**
     * Execute process instructions
     * @param processPCB is process PCB
     * @param roundRobin is true if scheduler is configured to run in round robin mode,
     *                   is false when execution is normal
     */
    private void executeProcess(PCB processPCB, boolean roundRobin) {
        boolean runAgain;
        do {
            runAgain = false;
            Process process = processPCB.getProcess();
            logger.addMessage("RUNNING_PROCESS", process.getName());
            processPCB.setState(State.RUNNING);

            quantunsRan += processPCB.getProcessQuantum();

            /*
              When running in round robin mode, each process will execute 1 quantum,
              otherwise it executes how many quantums process has on that moment
             */
            int instructionsToRun = Escalonador.quantum * (roundRobin ? 1 : processPCB.getProcessQuantum());

            // Initialize variables that control end of process execution and IO instruction called
            boolean processEnd = false;
            boolean processIO = false;
            int i;
            // Loop that consumes each instruction
            loop: for (i = 0; i < instructionsToRun; i++) {
                Instruction instruction = process.getTypeOfInstruction(processPCB.getProgramCounter());
                String instructionExecutor = process.getInstruction(processPCB.getProgramCounter());
                processPCB.increaseProgramCounter();
                switch (instruction) { // Check instruction type and execute as needed
                    case ATTRIBUTIONX:
                        // Set register X with new value
                        String xAttribute = instructionExecutor.split("=")[1];
                        processPCB.setRegisterX(Integer.parseInt(xAttribute));
                        break;
                    case ATTRIBUTIONY:
                        // Set register Y with new value
                        String yAttribute = instructionExecutor.split("=")[1];
                        processPCB.setRegisterY(Integer.parseInt(yAttribute));
                        break;
                    case INTERRUPTION:
                        // Log a message saying that an IO instructions was called on process
                        logger.addMessage("IO_STARTED", process.getName());
                        // Set processIO variable to true, saying that an IO instruction was called
                        processIO = true;
                        i++;
                        break loop;
                    case END:
                        // Set processEnd variable to true, which means process has finished execution
                        processEnd = true;
                        i++;
                        break loop;
                    default:
                        // If instruction is COM, does nothing
                        break;
                }
            }

            instructionsRan += i;

            logger.addMessage(i == 1 ? "INTERRUPTING_PROCESS_1" : "INTERRUPTING_PROCESS_2", process.getName(), i);

            // If process execution mode is not round robin
            if (!roundRobin) {
                processPCB.increaseProcessQuantum();
                processPCB.decreaseTwoCredits();
            }

            // If process did not finish
            if (!processEnd) {
                // If IO instruction is called
                if (processIO) {
                    processPCB.setState(State.BLOCKED);
                    processPCB.setWaitTo2();
                    ProcessList.decreaseBlockedListWait();
                    ProcessList.addBlockedProcess(processPCB);
                } else {
                    // If process execution mode is round robin, add process to end of readyList
                    if (roundRobin) {
                        processPCB.setState(State.READY);
                        ProcessList.addReadyProcessInLastPosition(processPCB);
                        ProcessList.decreaseBlockedListWait();
                    } else {
                        // If process execution is normal execution
                        ProcessList.decreaseBlockedListWait();
                        // Verify if process continues to be highest credit on ready list, if it is continues execution
                        if (ProcessList.shouldContinue(processPCB)) {
                            runAgain = true;
                        } else {
                            // Add process to correct position on ready list
                            processPCB.setState(State.READY);
                            ProcessList.addReadyProcessDuringExecution(processPCB);
                        }
                    }
                }
            } else {
                // If process finished, log message and remove it from process table.
                logger.addMessage("ENDING_PROCESS", process.getName(), processPCB.getRegisterX(), processPCB.getRegisterY());
                processPCB.setState(State.FINISHED);
                ProcessTable.removePCBofProcessTable(processPCB);
                ProcessList.decreaseBlockedListWait();
            }
        } while (runAgain);
    }

    /**
     * Manages scheduler execution
     */
    public void run() {
        int numOfProcess = ProcessTable.processTable.size();

        // Run while process table has any process
        while (ProcessTable.processTable.size() != 0) {
            // If readyList is not empty
            if (!ProcessList.readyList.isEmpty()) {
                // If all processes in readyList are zero credit
                if (ProcessList.allProcessInReadyListWithZEROCredit()) {
                    // If all processes in blockedList are zero credit and not all processes in readyList are zero priority
                    if (ProcessList.allProcessInBlockedListWithZEROCredit() && !ProcessList.allProcessInReadyListWithZEROPriority()) {
                        ProcessList.resetReadyAndBlockedList(); // Reset readyList and blockedList
                    } else {
                        // Execute process in round robin mode
                        executeProcess(ProcessList.removeNextInReadyList(), true);
                        changes++;
                    }
                }
                // If at least one process in readyList is not zero credit
                else {
                    // Execute process normally
                    executeProcess(ProcessList.removeNextInReadyList(), false);
                    changes++;
                }
            }
            // If readyList is empty
            else {
                ProcessList.decreaseBlockedListWait();
            }
        }

        // Write statistics
        logger.addMessage("AVERAGE_EXCHANGES", changes / numOfProcess);
        logger.addMessage("INSTRUCTION_AVERAGE", instructionsRan / quantunsRan);
        logger.addMessage("QUANTUM", Escalonador.quantum);
    }

}
