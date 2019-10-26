package scheduler;

public class Process {
	private final String [] instructions;
	private String name;
	
	public Process(String name, String[] instructions) {
		this.name = name;
		this.instructions = instructions;
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
	
	public String getName() {
		return this.name;
	}
}
