package scheduler;

public class Process {
	private final String [] instructions;
	private String name;

	/**
	 * Constructor of Process class
	 * @param name is the process name
	 * @param instructions is a instruction array
	 */
	public Process(String name, String[] instructions) {
		this.name = name;
		this.instructions = instructions;
	}

	/**
	 * @param index is a index of instruction
	 * @return the instruction
	 */
	public String getInstruction(int index) {
		return this.instructions[index];
	}

	/**
	 * Get type of instruction
	 * @param index is a index of instruction
	 * @return the type of instruction
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

	/**
	 * @return process name
	 */
	public String getName() {
		return this.name;
	}
}
