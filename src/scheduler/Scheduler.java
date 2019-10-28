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
     * Constructor of Scheduler class
     * @throws IOException
     */
    public Scheduler() throws IOException {
        logger = Log.getInstance();
    }

    /**
     * Execute the process using priority or round robin
     * @param processPCB is the PCB
     * @param roundRobin is true if it is need to run round robin,
     *                   or is false if it is need to run using priority
     */
    private void executeProcess(PCB processPCB, boolean roundRobin) {
        boolean runAgain;
        do {
            runAgain = false;
            Process process = processPCB.getProcess();
            logger.addMessage("RUNNING_PROCESS", process.getName());
            processPCB.setState(State.RUNNING);

            quantunsRan += processPCB.getProcessQuantum();

            /**
             * If it is running round robin, each process executes quantum instructions,
             * else executes quantum x processQuantum instructions
             */
            int instructionsToRun = Escalonador.quantum * (roundRobin ? 1 : processPCB.getProcessQuantum());

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
                        // Modify registerX with its value
                        String xAttribute = instructionExecutor.split("=")[1];
                        processPCB.setRegisterX(Integer.parseInt(xAttribute));
                        break;
                    case ATTRIBUTIONY:
                        // Modify registerY with its value
                        String yAttribute = instructionExecutor.split("=")[1];
                        processPCB.setRegisterY(Integer.parseInt(yAttribute));
                        break;
                    case INTERRUPTION:
                        // Sets the processIO variable equal to true, which means the process must be locked
                        logger.addMessage("IO_STARTED", process.getName());
                        processIO = true;
                        i++;
                        break loop;
                    case END:
                        // Sets the processEnd variable equal to true, which means the process must be ended
                        processEnd = true;
                        i++;
                        break loop;
                    default:
                        // In case the instruction is a command
                        break;
                }
            }

            instructionsRan += i;

            logger.addMessage(i == 1 ? "INTERRUPTING_PROCESS_1" : "INTERRUPTING_PROCESS_2", process.getName(), i);

            // If process is executing using priority
            if (!roundRobin) {
                processPCB.increaseProcessQuantum();
                processPCB.decreaseTwoCredits();
            }

            if (!processEnd) {
                if (processIO) {
                    // If process does not end and is blocked
                    processPCB.setState(State.BLOCKED);
                    processPCB.setWaitTo2();
                    ProcessList.decreaseBlockedListWait();
                    ProcessList.addBlockedProcess(processPCB);
                } else {
                    if (roundRobin) {
                        // If process does not end and is executing round robin
                        processPCB.setState(State.READY);
                        ProcessList.addReadyProcessInLastPosition(processPCB);
                        ProcessList.decreaseBlockedListWait();
                    } else {
                        // If process does not end and is executing using priority
                        ProcessList.decreaseBlockedListWait();
                        if (ProcessList.shouldContinue(processPCB)) {
                            // Execute again
                            runAgain = true;
                        } else {
                            // Enter in the ready list in their respective position
                            processPCB.setState(State.READY);
                            ProcessList.addReadyProcessDuringExecution(processPCB);
                        }
                    }
                }
            } else {
                // If process end
                logger.addMessage("ENDING_PROCESS", process.getName(), processPCB.getRegisterX(), processPCB.getRegisterY());
                processPCB.setState(State.FINISHED);
                ProcessTable.removePCBofProcessTable(processPCB);
                ProcessList.decreaseBlockedListWait();
            }
        } while (runAgain);
    }

    /**
     * Manages all scheduler execution
     */
    public void run() {
        int numOfProcess = ProcessTable.processTable.size();

        // While the processTable contains some process
        while (ProcessTable.processTable.size() != 0) {
            // If readyList is not empty
            if (!ProcessList.readyList.isEmpty()) {
                // If all processes in readyList are zero credit
                if (ProcessList.allProcessInReadyListWithZEROCredit()) {
                    // If all processes in blockedList are zero credit and not all processes in readyList are zero priority
                    if (ProcessList.allProcessInBlockedListWithZEROCredit() && !ProcessList.allProcessInReadyListWithZEROPriority()) {
                        ProcessList.resetReadyAndBlockedList(); // Reset readyList and blockedList
                    } else {
                        // Execute round robin
                        executeProcess(ProcessList.removeNextInReadyList(), true);
                        changes++;
                    }
                }
                //If at least one process in readyList is not zero credit
                else {
                    // Execute using priority
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
