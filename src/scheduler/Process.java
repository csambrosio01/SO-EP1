package scheduler;

public class Process {
	private final String [] instructions; 
	private State state;
	private String name;
	private int number;

	
	public Process(String name, String[] instructions, State state, int number) {
		this.name = name;
		this.setState(state);
		this.instructions = instructions;
		this.number = number;
	}
	
	public String getInstruction(int index) {
		return this.instructions[index];
	}
	
	public Instruction getTypeOfInstruction(int index) {
		String instruction = getInstruction(index).toUpperCase();
		
		switch(instruction) {
			case "E/S":
				return Instruction.INTERRUPTION;
			case "COM":
				return Instruction.COMMAND;
			case "SAIDA":
				return Instruction.END;
			default:
				if (instruction.contains("X=")) 
					return Instruction.ATTRIBUTIONX;
				else 
					return Instruction.ATTRIBUTIONY;
		}
	}

	public State getState() {
		return this.state;
	}

	public void setState(State state) {
		this.state = state;
	}
	
	public String getName() {
		return this.name;
	}

	public int getNumber() {
		return this.number;
	}
}
