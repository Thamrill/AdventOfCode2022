package day8;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class Test8_1 implements Runnable {

	private static final String path = "day8-input1.txt";

	public static void main(String[] args) {
		new Test8_1().run();
	}
	
	HashMap<Point, Integer> trees;
	HashMap<Point, Boolean> visible;
	

	@Override
	public void run() {
		File input = new File(path);
		trees=new HashMap<>();
		visible=new HashMap<>();
		int cols=0;
		int rows=0;
		
		try (FileReader fr = new FileReader(input); BufferedReader br = new BufferedReader(fr)) {
			String read=br.readLine();
			int row=0;
			while(read!=null) {
				cols=read.length()-1;
				for(int column=0; column<read.length(); column++) {
					Point p=new Point(column, row);
					trees.put(p, Integer.parseInt(read.charAt(column)+""));
					visible.put(p, false);
				}
				row++;
				
				read=br.readLine();
			}
			rows=row-1;
			for(int col=0; col<=cols; col++) {
				int max=-1;
				for(row=0; row<=rows; row++) {
					Point p=new Point(col, row);
					int height=trees.get(p);
					visible.replace(p, visible.get(p)||height>max);
					if(height>max) {
						max=height;
					}
				}
				
				max=-1;
				for(row=rows; row>=0; row--) {
					Point p=new Point(col, row);
					int height=trees.get(p);
					visible.replace(p, visible.get(p)||height>max);
					if(height>max) {
						max=height;
					}
				}
			}
			for(row=0; row<=rows; row++) {
				int max=-1;
				for(int col=0; col<=cols; col++) {
					Point p=new Point(col, row);
					int height=trees.get(p);
					visible.replace(p, visible.get(p)||height>max);
					if(height>max) {
						max=height;
					}
				}
				
				max=-1;
				for(int col=cols; col>=0; col--) {
					Point p=new Point(col, row);
					int height=trees.get(p);
					visible.replace(p, visible.get(p)||height>max);
					if(height>max) {
						max=height;
					}
				}
			}
			
			String str="";
			for(int ii=0; ii<=rows; ii++) {
				for(int jj=0; jj<=cols; jj++) {
					Point p=new Point(jj, ii);
					str+=visible.get(p).toString().charAt(0);
				}
				str+="\n";
			}
			System.out.println(str);
			System.out.println();
			int count=0;
			for(Boolean b:visible.values()) {
				if(b) {
					count++;
				}
			}
			System.out.println(count);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
