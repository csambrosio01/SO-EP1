package scheduler;

import java.util.ArrayList;
import java.util.List;

public class ProcessTable {
	// List with all PCBs
	public static List<PCB> processTable = new ArrayList<PCB>();

	// Add PCB on processTable
	public static void addPCBonProcessTable(PCB pcb) {
		processTable.add(pcb);
	}

	// Remove PCB from processTable
	public static void removePCBofProcessTable(PCB pcb) {
		processTable.remove(pcb);
	}
}
