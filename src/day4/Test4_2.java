package day4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test4_2 implements Runnable {

	private static final String path = "day4-input1.txt";

	public static void main(String[] args) {
		new Test4_2().run();
	}

	@Override
	public void run() {
		File input = new File(path);

		int score = 0;
		Pattern pattern = Pattern.compile("(\\d+)-(\\d+),(\\d+)-(\\d+)");
		Matcher matcher;

		try (FileReader fr = new FileReader(input); BufferedReader br = new BufferedReader(fr)) {
			String read = br.readLine();

			while (read != null) {
				matcher = pattern.matcher(read);
				if (matcher.find()) {
					int a0 = Integer.parseInt(matcher.group(1));
					int a1 = Integer.parseInt(matcher.group(2));
					int b0 = Integer.parseInt(matcher.group(3));
					int b1 = Integer.parseInt(matcher.group(4));
					if(a0>a1) {
						int temp=a1;
						a1=a0;
						a0=temp;
					}
					if(b0>b1) {
						int temp=b1;
						b1=b0;
						b0=temp;
					}
					boolean isOverlapping=overlap(a0, a1, b0, b1);
					System.out.println(read+">"+isOverlapping);
					if(isOverlapping) {
						score++;
					}
				}
				read = br.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(score);
	}
	
	public boolean overlap(int a0, int a1, int b0, int b1) {
		return isAinB(a0, a1, b0, b1)||isBinA(a0, a1, b0, b1);
	}

	public boolean isAinB(int a0, int a1, int b0, int b1) {
		return isBetween(a0, b0, b1)||isBetween(a1, b0, b1);
	}

	public boolean isBinA(int a0, int a1, int b0, int b1) {
		return isBetween(b0, a0, a1)||isBetween(b1, a0, a1);
	}
	
	public boolean isBetween(int value, int min, int max) {
		return value>=min&&value<=max;
	}
}
