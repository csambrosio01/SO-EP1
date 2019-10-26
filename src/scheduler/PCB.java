package scheduler;

import java.util.Comparator;

public class PCB {
	private Process process;
	private State state;
	private final int priority;
	private int programCounter;
	private int registerX;
	private int registerY;
	private int credit;
	private int wait;
	private int processQuantum;
	

	public PCB(Process process, int priority, State state) {
		this.process = process;
        this.state = state;
		this.programCounter = 0;
		this.priority = priority;
		this.credit = priority;
		this.registerX = 0;
		this.registerY = 0;
		this.wait = 0;
		this.processQuantum = 1;
	}
	
	// Process methods
	public Process getProcess() {
		return this.process;
	}

	// State methods
	public void setState(State state) {
		this.state = state;
	}

	// Priority methods
	public int getPriority() {
		return this.priority;
	}
	
	// PC methods
	public void increaseProgramCounter() {
		this.programCounter++;
	}
	
	public int getProgramCounter() {
		return this.programCounter;
	}
	
	// Credit methods
	public void decreaseTwoCredits() {
		this.credit = (this.credit <= 2) ? 0 : this.credit - 2;
	}
	
	public void equalsCreditWithPriority() {
		this.credit = this.priority;
	}
	
	public int getCredit() {
		return this.credit;
	}
	
	//Register methods
	public int getRegisterX() {
		return this.registerX;
	}
	
	public void setRegisterX(int x) {
		this.registerX = x;
	}
	
	public int getRegisterY() {
		return this.registerY;
	}
	
	public void setRegisterY(int y) {
		this.registerY = y;
	}
	
	// Wait methods
	public void decreaseWait() {
		this.wait--;
	}
	
	public void setWaitTo2() {
		this.wait = 2;
	}
	
	public int getWait() {
		return this.wait;
	}

	//Quantum methods
	public int getProcessQuantum() {
		return processQuantum;
	}

	public void increaseProcessQuantum() {
		this.processQuantum++;
	}

	public void setProcessQuantumTo1() {
		this.processQuantum = 1;
	}

    /**
     * compareTo for initialization
     * @param pcb is a pcb
     * @return -1 if this process needs to go in front of the pcb
     *          0 if processes have the same credit and same priority
     *          1 if this process needs to go after the pcb
     */
	public int compareToForInitialization(PCB pcb) {
		int condition = commonConditions(pcb);
		if (condition != 0) return condition;
		else {
			Comparator comparator = Comparator.comparing(PCB::removeNumbers).thenComparing(PCB::keepNumbers);
			int result = comparator.compare(this.process.getName(), pcb.process.getName());
			if (result < 0) {
				return -1;
			} else if (result > 0) {
				return 1;
			} else return result;
		}
	}

    /**
     * compareTo between ready process
     * @param pcb is a pcb
     * @return -1 if this process needs to go in front of the pcb
     *          1 if this process needs to go after the pcb
     */
	public int compareToForReadyProcess(PCB pcb) {
		int condition = commonConditions(pcb);
		return condition != 0 ? condition : -1;
	}

    /**
     * compareTo between a ready process and a process that was blocked
     * @param pcb is a pcb
     * @return -1 if this process needs to go in front of the pcb
     *          1 if this process needs to go after the pcb
     */
	public int compareToForBlockedProcess(PCB pcb) {
		int condition = commonConditions(pcb);
		return condition != 0 ? condition : 1;
	}

    /**
     * Checks whether processes are tied or not
     * @param pcb is a pcb
     * @return -1 if this process needs to go in front of the pcb
     *          0 if processes have the same credit and same priority
     *          1 if this process needs to go after the pcb
     */
	private int commonConditions(PCB pcb) {
		if (this.credit < pcb.credit) return 1;
		else if (this.credit > pcb.credit) return -1;
		else if (this.priority < pcb.priority) return 1;
		else if (this.priority > pcb.priority) return -1;
		return 0; //same credit and same priority
	}

	// Method for sort process names
	private static String removeNumbers(String s) {
		return s.replaceAll("\\d", "");
	}

    // Method for sort process names
	private static Integer keepNumbers(String s) {
		String number = s.replaceAll("\\D", "");
		if (!number.isEmpty()) {
			return Integer.parseInt(number);
		}
		return 0;
	}
}
