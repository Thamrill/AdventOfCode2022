package day6;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Test6_2 implements Runnable {
	private static final String path = "day6-input1.txt";

	public static void main(String[] args) {
		new Test6_2().run();
	}

	@Override
	public void run() {
		File input = new File(path);

		try (FileReader fr = new FileReader(input); BufferedReader br = new BufferedReader(fr)) {
			String read = br.readLine();
			String subS;
			for(int ii=14; ii<read.length(); ii++) {
				subS=read.substring(ii-14, ii);
				if(!hasRepeatedCharacters(subS)) {
					System.out.println(ii+":"+ subS);
					System.out.println(read.substring(0, ii).length()+":"+read.substring(0, ii));
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private boolean hasRepeatedCharacters(String str) {
		for(int ii=0; ii<str.length(); ii++) {
			if(str.lastIndexOf(str.charAt(ii))!=ii) {
				return true;
			}
		}
		return false;
	}
}
