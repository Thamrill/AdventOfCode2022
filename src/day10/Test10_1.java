package day10;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Test10_1 implements Runnable {
	private static final String path = "day10-input1.txt";
	//private static final String path1 = "day10_validation";
	

	public static void main(String[] args) {
		new Test10_1().run();
	}
	
	ArrayList<Integer> xValues;
	Integer currentValue;

	@Override
	public void run() {
		File input = new File(path);
		xValues=new ArrayList<>();
		currentValue=1;
		int[] inputs= new int[] {20, 60, 100, 140, 180, 220};
		try (FileReader fr = new FileReader(input); BufferedReader br = new BufferedReader(fr)) {
			String read = br.readLine();
			while (read != null) {
				String[] cmd = read.split(" ");
				String dir = cmd[0];
				xValues.add(currentValue);
				switch(dir) {
				case "addx":
					int incr=Integer.parseInt(cmd[1]);
					currentValue+=incr;
					xValues.add(currentValue);
					break;
				case "noop":
					break;
				}
				read = br.readLine();
			}
			int sum=0;
			for(int cyc:inputs) {
				sum+=cyc*xValues.get(cyc-2);
				System.out.println(cyc*xValues.get(cyc-2));
			}
			System.out.println(sum);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
