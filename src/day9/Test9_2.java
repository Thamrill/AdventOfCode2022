package day9;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;

public class Test9_2 implements Runnable {

	private static final String path = "day9-input1.txt";

	public static void main(String[] args) {
		new Test9_2().run();
	}

	HashSet<Point> tailUniquePositions;

	Point[] rope=new Point[10] ;

	@Override
	public void run() {
		File input = new File(path);
		tailUniquePositions=new HashSet<>();
		for(int ii=0; ii<10; ii++) {
			rope[ii]=new Point();
		}
		try (FileReader fr = new FileReader(input); BufferedReader br = new BufferedReader(fr)) {
			String read = br.readLine();
			while (read != null) {
				String[] cmd = read.split(" ");
				String dir = cmd[0];
				int qty = Integer.parseInt(cmd[1]);
				int mx=0;
				int my=0;
				switch(dir) {
				case "U":
					my=1;
					break;
				case "D":
					my=-1;
					break;
				case "L":
					mx=-1;
					break;
				case "R":
					mx=1;
					break;
				}
				for (int ii = 0; ii < qty; ii++) {
					rope[0].x+=mx;
					rope[0].y+=my;
					
					for(int jj=1; jj<rope.length; jj++) {
						rope[jj]=calculateNextPosition(rope[jj-1], rope[jj]);
					}
					printRope();
					tailUniquePositions.add(rope[9]);
				}

				read = br.readLine();
			}
			System.out.println(tailUniquePositions.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Point calculateNextPosition(Point head, Point tail) {
		int dx=head.x-tail.x;
		int dy=head.y-tail.y;
		
		if(Math.abs(dx)<=1&&Math.abs(dy)<=1) {
			return tail;
		}
		if(dx==0) {
			dy=dy>0?1:-1;
		}else if(dy==0) {
			dx=dx>0?1:-1;
		}else {
			dx=dx>0?1:-1;
			dy=dy>0?1:-1;
		}
		return new Point(tail.x+dx, tail.y+dy);
	}
	
	private void printRope() {
		String str="";
		for(Point p:rope) {
			str+=", ("+p.x+","+p.y+")"; 
		}
		str=str.substring(2);
		System.out.println(str);
	}
}
