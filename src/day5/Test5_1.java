package day5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test5_1 implements Runnable {

	private static final String path = "day5-input1.txt";

	public static void main(String[] args) {
		new Test5_1().run();
	}
	
	ArrayList<Stack<Character>> stacks;

	@Override
	public void run() {
		File input = new File(path);
		
		stacks=new ArrayList<>();
		ArrayList<ArrayList<Character>> temp=new ArrayList<>();
		for(int ii=1; ii<=9; ii++) {
			stacks.add(new Stack<>());
			temp.add(new ArrayList<>());
		}
		
		Pattern pattern = Pattern.compile("move (\\d+) from (\\d+) to (\\d+)");
		Matcher matcher;
		
		try (FileReader fr = new FileReader(input); BufferedReader br = new BufferedReader(fr)) {
			String read;
			Character crate;
			for(int ii=0; ii<8; ii++) {
				read= br.readLine();
				for(int jj=0; jj<9; jj++) {
					crate=read.charAt(jj*4+1);
					if(!crate.toString().isBlank()) {
						temp.get(jj).add(crate);
					}
				}
			}
			for(int ii=0; ii<temp.size(); ii++) {
				Collections.reverse(temp.get(ii));
				stacks.get(ii).addAll(temp.get(ii));
			}
			read= br.readLine();
			while (read != null) {
				matcher = pattern.matcher(read);
				if (matcher.find()) {
					int n=Integer.parseInt(matcher.group(1));
					int from=Integer.parseInt(matcher.group(2));
					int to=Integer.parseInt(matcher.group(3));
					
					for(int ii=0; ii<n; ii++) {
						Character c=stacks.get(from-1).pop();
						stacks.get(to-1).push(c);
					}
				}
				read = br.readLine();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String tops="";
		for(int ii=0; ii<stacks.size(); ii++) {
			tops+=stacks.get(ii).pop();
		}
		
		System.out.println(tops);
	}

	public boolean isAnyContained(int a0, int a1, int b0, int b1) {
		return isAinB(a0, a1, b0, b1) || isBinA(a0, a1, b0, b1);
	}

	public boolean isAinB(int a0, int a1, int b0, int b1) {
		return a0>=b0&&a1<=b1;
	}

	public boolean isBinA(int a0, int a1, int b0, int b1) {
		return b0>=a0&&b1<=a1;
	}
}
