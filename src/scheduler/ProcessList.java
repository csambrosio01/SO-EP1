package scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProcessList {
	// The readyList is sorted in descending order
	public static List<PCB> readyList = new ArrayList<PCB>();
	// The blockedList keeps the data in order of entry
	public static List<PCB> blockedList = new ArrayList<PCB>();
	

	public static void addReadyProcess(PCB pcb) {
		readyList.add(pcb);

		for (int i = readyList.size()-1; i > 0 && pcb.compareTo(readyList.get(i-1)) < 0; i--) 
			Collections.swap(readyList, i, i-1);
	}

	public static void addReadyProcessInLastPosition(PCB pcb) {
		readyList.add(pcb);
	}
	
	public static void addBlockedProcess(PCB pcb) {
		blockedList.add(pcb);
	}
	
	public static PCB removeNextInReadyList() {
		if (readyList.size() > 0) {
			return readyList.remove(0); // PCB with more priority
		} else {
			return null;
		}
	}

	public static void decreaseBlockedListWait() {
		if (blockedList.size() > 0) {
			for (PCB pcb : blockedList) {
				pcb.decreaseWait();
			}
			int blockedListSize = blockedList.size();
			for (int i = 0; i < blockedListSize; i++) {
				if (blockedList.get(0).getWait() == 0) {
					PCB pcb = blockedList.remove(0);
					pcb.getProcess().setState(State.READY);
					if (pcb.getCredit() > 0) {
						addReadyProcess(pcb);
					} else {
						if (allProcessInReadyListWithZEROCredit()) {
							addReadyProcessInLastPosition(pcb);
						} else {
							addReadyProcess(pcb);
						}
					}
				} else {
					break;
				}
			}
		}
	}

	public static boolean shouldContinue(PCB pcb) {
		if (readyList.size() > 0) {
			return pcb.compareTo(readyList.get(0)) < 0;
		} else return true;
	}

	public static void resetReadyList() {
		for (PCB pcb : readyList) {
			pcb.equalsCreditWithPriority();
			pcb.setProcessQuantumTo1();
		}

		for (int i = readyList.size(); i >= 1; i--) {
			for (int j = 1; j < i; j++) {
				if (readyList.get(j - 1).compareTo(readyList.get(j)) < 0) {
					Collections.swap(readyList, j, j-1);
				}
			}
		}
	}

	public static boolean allProcessInReadyListWithZEROCredit() {
		for (PCB pcb : readyList) {
			if (pcb.getCredit() > 0) return false;
		}
		return true;
	}

	public static boolean allProcessInReadyListWithZEROPriority() {
		for (PCB pcb : readyList) {
			if (pcb.getPriority() > 0) return false;
		}
		return true;
	}

	public static boolean allProcessInBlockedListWithZEROCredit() {
		for (PCB pcb : blockedList) {
			if (pcb.getCredit() > 0) return false;
		}
		return true;
	}
}
