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
	
	public static PCB removeNextInBlockedList() {
		return blockedList.remove(0);
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
}
