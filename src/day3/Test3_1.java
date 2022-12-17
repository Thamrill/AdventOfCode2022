package day3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Test3_1 implements Runnable {

	private static final String path = "day3-input1.txt";

	public static class Sack{
		String comp1;
		String comp2;
		
		public Sack(String str) {
			comp1=str.substring(0, str.length()/2);
			comp2=str.substring(str.length()/2);
		}
		
		public char common() {
			char c= ' ';
			for(int ii=0; ii<comp1.length(); ii++) {
				c=comp1.charAt(ii);
				if(comp2.contains(c+"")) {
					return c;
				}
			}
			return ' ';
		}
	}

	public static void main(String[] args) {
		new Test3_1().run();
	}

	@Override
	public void run() {
		File input = new File(path);

		int score = 0;

		try (FileReader fr = new FileReader(input); BufferedReader br = new BufferedReader(fr)) {
			String read = br.readLine();
			while (read != null) {
				Sack sack=new Sack(read);
				score+=getPriority(sack.common());
				read = br.readLine();
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
