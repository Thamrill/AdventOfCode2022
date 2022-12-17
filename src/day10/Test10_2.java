package day10;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Test10_2 implements Runnable {
	private static final String path = "day10-input1.txt";
	

	public static void main(String[] args) {
		new Test10_2().run();
	}
	
	ArrayList<Integer> xValues;
	Integer currentValue;

	@Override
	public void run() {
		File input = new File(path);
		xValues=new ArrayList<>();
		currentValue=1;
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
			String str="";
			int pointer;
			for(int ii=0; ii<xValues.size(); ii++) {
				pointer=ii%40+1;
				if(Math.abs(xValues.get(ii)-pointer)>1) {
					str+=".";
				}else {
					str+="#";
				}
				if(pointer==40) {
					str+="\n";
				}
			}
			System.out.println(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
