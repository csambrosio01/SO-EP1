package scheduler;

public class Process {
	private final String [] instructions;
	private String name;

	/**
	 * Constructor of class Process
	 * @param name is the process name
	 * @param instructions is an array of instructions
	 */
	public Process(String name, String[] instructions) {
		this.name = name;
		this.instructions = instructions;
	}

	/**
	 * @param index is index of instruction that should be get
	 * @return the instruction
	 */
	public String getInstruction(int index) {
		return this.instructions[index];
	}

	/**
	 * Get type of instruction
	 * @param index is index of instruction that should be used
	 * @return the type of instruction localized on position that index represents
	 */
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
