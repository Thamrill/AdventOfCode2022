package day3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Test3_2 implements Runnable {

	private static final String path = "day3-input1.txt";

	public static class Triplet{
		String elf1;
		String elf2;
		String elf3;

		public Triplet(String elf1, String elf2, String elf3) {
			super();
			this.elf1 = elf1;
			this.elf2 = elf2;
			this.elf3 = elf3;
		}



		public char badge() {
			char c= ' ';
			for(int ii=0; ii<elf1.length(); ii++) {
				c=elf1.charAt(ii);
				if(elf2.contains(c+"")&&elf3.contains(c+"")) {
					return c;
				}
			}
			return ' ';
		}
	}

	public static void main(String[] args) {
		new Test3_2().run();
	}

	@Override
	public void run() {
		File input = new File(path);

		int score = 0;

		try (FileReader fr = new FileReader(input); BufferedReader br = new BufferedReader(fr)) {
			String read1 = br.readLine();
			String read2 = br.readLine();
			String read3 = br.readLine();
			while (read1 != null&&read2!=null&&read3!=null) {
				Triplet triplet=new Triplet(read1, read2, read3);
				score+=getPriority(triplet.badge());
				read1 = br.readLine();
				read2 = br.readLine();
				read3 = br.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(score);
	}

	private int getPriority(char c) {
		if(c>='a'&&c<='z') {
			return (int) c-'a'+1;
		}else {
			return (int) c-'A'+27;
		}
	}
}
