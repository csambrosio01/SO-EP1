package escalonador;
public class Process {
	static final char READY = 'r';   // the process is ready
	static final char RUNNING = 'u'; // the process is running
	static final char BLOCKED = 'b'; // the process is blocked
	private final String [] instructions; 
	private char state;
	private String name;

	
	public Process(String name, String[] instructions, char state) {
		this.name = name;
		this.setState(state);
		this.instructions = instructions;
	}
	
	public String getInstruction(int index) {
		return this.instructions[index];
	}

	public char getState() {
		return this.state;
	}

	public void setState(char state) {
		this.state = state;
	}
	
	public String getName() {
		return this.name;
	}
}
