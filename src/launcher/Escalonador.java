package launcher;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Escalonador {

	public static int quantum = 3; //Set default quantum as 3

	private static void readQuantum() throws IOException {
		Scanner scanner = new Scanner(new File("src/processos/quantum.txt"));
		quantum = scanner.nextInt();
	}

	public static void main(String[] args) throws IOException {
		readQuantum();
	}

}
