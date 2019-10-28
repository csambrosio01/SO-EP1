package scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProcessList {

	public static List<PCB> readyList = new ArrayList<PCB>();
	public static List<PCB> blockedList = new ArrayList<PCB>();

	/**
	 * Add pcb on readyList when program is initializing
	 * @param pcb pcb that will be added, came from execution
	 */
	public static void addReadyProcessOnInitialization(PCB pcb) {
		readyList.add(pcb);

		for (int i = readyList.size()-1; i > 0 && pcb.compareToForInitialization(readyList.get(i-1)) < 0; i--)
			Collections.swap(readyList, i, i-1);
	}

	/**
	 * Add pcb that was executing to readyList
	 * @param pcb pcb that will be added
	 */
	public static void addReadyProcessDuringExecution(PCB pcb) {
		readyList.add(pcb);

		for (int i = readyList.size()-1; i > 0 && pcb.compareToForReadyProcess(readyList.get(i-1)) < 0; i--)
			Collections.swap(readyList, i, i-1);
	}

	/**
	 * Add pcb that was blocked to readyList
	 * @param pcb pcb that will be added, came from blocked list
	 */
	public static void addBlockedProcessDuringExecution(PCB pcb) {
		readyList.add(pcb);

		for (int i = readyList.size()-1; i > 0 && pcb.compareToForBlockedProcess(readyList.get(i-1)) < 0; i--)
			Collections.swap(readyList, i, i-1);
	}

	/**
	 * Add pcb in last position of readyList, this method is used when execution mode is round robin
	 * @param pcb pcb that will be added, came from execution
	 */
	public static void addReadyProcessInLastPosition(PCB pcb) {
		readyList.add(pcb);
	}

	/**
	 * Add pcb in last position of blockedList
	 * @param pcb pcb that is blocked and should be added to blocked list
	 */
	public static void addBlockedProcess(PCB pcb) {
		blockedList.add(pcb);
	}

	/**
	 * Remove next available pcb in readyList
	 * @return pcb
	 */
	public static PCB removeNextInReadyList() {
		if (readyList.size() > 0) {
			return readyList.remove(0); // PCB with more priority
		} else {
			return null;
		}
	}

	/**
	 * Decrease wait of blocked pcb on blocked list
	 */
	public static void decreaseBlockedListWait() {
		// Only executes if blocked list is not empty
		if (blockedList.size() > 0) {
			// Decrease each pcb wait
			for (PCB pcb : blockedList) {
				pcb.decreaseWait();
			}

			// Remove pcb if its wait is zero
			int blockedListSize = blockedList.size();
			for (int i = 0; i < blockedListSize; i++) {
				if (blockedList.get(0).getWait() == 0) {
					PCB pcb = blockedList.remove(0);
					pcb.setState(State.READY);
					if (pcb.getCredit() > 0) {
						addBlockedProcessDuringExecution(pcb);
					} else {
						if (allProcessInReadyListWithZEROCredit()) {
							addReadyProcessInLastPosition(pcb);
						} else {
							addBlockedProcessDuringExecution(pcb);
						}
					}
				} else {
					// If first pcb does not have wait zero, break loop
					break;
				}
			}
		}
	}

	/**
	 * Checks whether a pcb should continue executing or not
	 * @param pcb pcb that will be used to make check
	 * @return true if should continue or false if it should not
	 */
	public static boolean shouldContinue(PCB pcb) {
		if (readyList.size() > 0) {
			return pcb.compareToForReadyProcess(readyList.get(0)) < 0;
		} else return true;
	}

	/**
	 * Redistribute credits from pcb on ready and blocked list
	 */
	public static void resetReadyAndBlockedList() {
		// Reset credits of readyList
		for (PCB pcb : readyList) {
			pcb.equalsCreditWithPriority();
			pcb.setProcessQuantumTo1();
		}

		// Sort readyList
		for (int i = readyList.size(); i >= 1; i--) {
			for (int j = 1; j < i; j++) {
				if (readyList.get(j - 1).compareToForInitialization(readyList.get(j)) > 0) {
					Collections.swap(readyList, j, j-1);
				}
			}
		}

		// Reset credits of blockedList
		for (PCB pcb : blockedList) {
			pcb.equalsCreditWithPriority();
			pcb.setProcessQuantumTo1();
		}

		// blocked list does not need sorting
	}

	/**
	 * Checks if all pcb in readyList are with zero credit
	 */
	public static boolean allProcessInReadyListWithZEROCredit() {
		for (PCB pcb : readyList) {
			if (pcb.getCredit() > 0) return false;
		}
		return true;
	}

	/**
	 * Checks if all pcb in readyList are with zero priority
	 */
	public static boolean allProcessInReadyListWithZEROPriority() {
		for (PCB pcb : readyList) {
			if (pcb.getPriority() > 0) return false;
		}
		return true;
	}

	/**
	 * Checks if all pcb in blockedList are with zero credit
	 */
	public static boolean allProcessInBlockedListWithZEROCredit() {
		for (PCB pcb : blockedList) {
			if (pcb.getCredit() > 0) return false;
		}
		return true;
	}
}
