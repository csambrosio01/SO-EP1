import java.util.ArrayList;
import java.util.List;

public class ProcessTable {
	public static List<PCB> processTable = new ArrayList<PCB>();
	
	public static void addPCBonProcessTable(PCB pcb) {
		processTable.add(pcb);
	}
	
	public static void removePCBofProcessTable(PCB pcb) {
		processTable.remove(pcb);
	}
}
