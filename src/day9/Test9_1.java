package day9;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;

public class Test9_1 implements Runnable {

	private static final String path = "day9-input1.txt";

	public static void main(String[] args) {
		new Test9_1().run();
	}

	HashSet<Point> tailUniquePositions;
	ArrayList<Point> headPositions;
	ArrayList<Point> tailPositions;

	int hX;
	int hY;
	int tX;
	int tY;

	@Override
	public void run() {
		File input = new File(path);
		tailPositions = new ArrayList<>();
		headPositions = new ArrayList<>();
		tailUniquePositions=new HashSet<>();
		int dx;
		int dy;
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
					hX+=mx;
					hY+=my;
					
					dx=hX-tX;
					dy=hY-tY;
					Point head=new Point(hX, hY);
					Point tail=new Point(tX, tY);
					
					if(Math.abs(dx)<=1&&Math.abs(dy)<=1) {
						tailPositions.add(tail);
						headPositions.add(head);
						tailUniquePositions.add(tail);
						continue;
					}
					if(dx==0) {
						dy=dy>0?1:-1;
					}else if(dy==0) {
						dx=dx>0?1:-1;
					}else {
						dx=dx>0?1:-1;
						dy=dy>0?1:-1;
					}
					tX+=dx;
					tY+=dy;
					tail=new Point(tX, tY);
					tailPositions.add(tail);
					headPositions.add(head);
					tailUniquePositions.add(tail);
				}

				read = br.readLine();
			}
			System.out.println(tailUniquePositions.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
