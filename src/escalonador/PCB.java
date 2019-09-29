package escalonador;
public class PCB implements Comparable<PCB>{
	private Process process;
	private int priority;
	private int programCounter;
	private int registerX;
	private int registerY;
	private int credit;
	private int wait;
	

	public PCB(Process process, int priority) {
		this.process = process;
		this.programCounter = 0;
		this.priority = priority;
		this.credit = priority;
		this.registerX = 0;
		this.registerY = 0;
		this.wait = 0;
	}
	
	// Process methods
	public Process getProcess() {
		return this.process;
	}
	
	// PC methods
	public void increaseProgramCounter() {
		this.programCounter++;
	}
	
	public int getProgramCounter() {
		return this.programCounter;
	}
	
	// Priority methods
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	// Credit methods
	public void decreaseCredit() {
		this.credit--;
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
		this.registerX = y;
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
	

	@Override
	public int compareTo(PCB pcb) {
		if (this.credit < pcb.credit) return 1;
		else if (this.credit > pcb.credit) return -1;
		else return 0;
	}
}
