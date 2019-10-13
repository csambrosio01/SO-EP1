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
			for (int i = 0; i < blockedList.size(); i++) {
				PCB pcb = blockedList.remove(i);
				pcb.decreaseWait();
				if (pcb.getWait() == 0) {
					pcb.getProcess().setState(State.RUNNING);
					addReadyProcess(pcb);
				} else {
					blockedList.add(pcb);
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
					PCB aux = readyList.get(j);
					readyList.set(j, readyList.get(j - 1));
					readyList.set(j - 1, aux);
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
}
