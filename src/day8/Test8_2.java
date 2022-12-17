package day8;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class Test8_2 implements Runnable {

	private static final String path = "day8-input1.txt";

	public static void main(String[] args) {
		new Test8_2().run();
	}

	HashMap<Point, Integer> trees;
	HashMap<Point, Integer> scores;

	@Override
	public void run() {
		File input = new File(path);
		trees = new HashMap<>();
		scores = new HashMap<>();
		int cols = 0;
		int rows = 0;

		try (FileReader fr = new FileReader(input); BufferedReader br = new BufferedReader(fr)) {
			String read = br.readLine();
			int row = 0;
			while (read != null) {
				cols = read.length() - 1;
				for (int column = 0; column < read.length(); column++) {
					Point p = new Point(column, row);
					trees.put(p, Integer.parseInt(read.charAt(column) + ""));
					scores.put(p, 0);
				}
				row++;

				read = br.readLine();
			}
			rows = row - 1;
			for (int col = 1; col <= cols - 1; col++) {
				for (row = 1; row <= rows - 1; row++) {
					Point p = new Point(col, row);
					int height = trees.get(p);
					int scoreLeft = 0;
					int scoreRight = 0;
					int scoreUp = 0;
					int scoreDown = 0;
					for (int ix = col - 1; ix >= 0; ix--) {
						scoreLeft++;
						if (trees.get(new Point(ix, row)) < height) {

						} else {
							break;
						}
					}
					for (int ix = col + 1; ix <= cols; ix++) {
						scoreRight++;
						if (trees.get(new Point(ix, row)) < height) {

						} else {
							break;
						}
					}

					for (int iy = row + 1; iy <= rows; iy++) {
						scoreDown++;
						if (trees.get(new Point(col, iy)) < height) {

						} else {
							break;
						}
					}

					for (int iy = row - 1; iy >= 0; iy--) {
						scoreUp++;
						if (trees.get(new Point(col, iy)) < height) {

						} else {
							break;
						}
					}
					scores.put(p, scoreLeft * scoreRight * scoreUp * scoreDown);
				}

			}

//			String str="";
//			for(int ii=0; ii<=rows; ii++) {
//				for(int jj=0; jj<=cols; jj++) {
//					Point p=new Point(jj, ii);
//					str+=visible.get(p).toString().charAt(0);
//				}
//				str+="\n";
//			}
//			System.out.println(str);
//			System.out.println();

			int maxScore = Integer.MIN_VALUE;
			for (Point p : scores.keySet()) {
				int score = scores.get(p);
				if (score > maxScore) {
					maxScore = score;
					System.out.println(p);
				}
			}
			System.out.println(maxScore);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
