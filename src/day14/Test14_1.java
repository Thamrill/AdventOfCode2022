package day14;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Test14_1 implements Runnable {
	public Test14_1() {
	}

	@Override
	public void run() {
		File inputFile = new File("day14-input1.txt");
		//inputFile = new File("day14_validation");

		Point sourceCoords = new Point(500, 0);
		ArrayList<ArrayList<Point>> rockLines = new ArrayList<>();
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		HashMap<Point, Character> map=new HashMap<>();
		try (FileReader fr = new FileReader(inputFile); BufferedReader br = new BufferedReader(fr)) {
			String line = br.readLine();
			while (line != null) {
				ArrayList<Point> points = parseList(line);
				rockLines.add(points);

				for (Point p : points) {
					if (p.x < minX) {
						minX = p.x;
					}
					if (p.x > maxX) {
						maxX = p.x;
					}
					if (p.y < minY) {
						minY = p.y;
					}
					if (p.y > maxY) {
						maxY = p.y;
					}
				}

				line = br.readLine();
			}
			minX--;
			maxX++;
			maxY++;
			minY=0;
			for (int ix = minX; ix <= maxX; ix++) {
				for (int iy = minY; iy <= maxY; iy++) {
					map.put(new Point(ix, iy),  '.');
				}
			}
			
			map.replace(sourceCoords, '+');
			
			for(ArrayList<Point> rockLine:rockLines) {
				for(int ii=0; ii<rockLine.size()-1; ii++) {
					Point start=rockLine.get(ii);
					Point end=rockLine.get(ii+1);
					int deltaX=end.x-start.x;
					int startCoord;
					int endCoord;
					if(deltaX==0) {
						startCoord=Math.min(start.y, end.y);
						endCoord=Math.max(start.y, end.y);
						int x=start.x;
						for(int jj=startCoord; jj<=endCoord; jj++) {
							map.replace(new Point(x, jj), '#');
						}
					}else {
						startCoord=Math.min(start.x, end.x);
						endCoord=Math.max(start.x, end.x);
						int y=start.y;
						for(int jj=startCoord; jj<=endCoord; jj++) {
							map.replace(new Point(jj, y), '#');
						}
					}
				}
			}
			runSimulation(map, new int[] {minX, maxX, minY, maxY}, sourceCoords);
			printMap(map, new int[] {minX, maxX, minY, maxY});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void runSimulation(HashMap<Point, Character> map, int[] ranges, Point source) {
		int spawned=0;
		Point coords;
		Point nextPoint;
		boolean reachedVoid=false;
		while(!reachedVoid) {
			spawned++;
			coords=new Point(source.x, source.y);
			while(true) {
				if(coords.y==ranges[3]) {
					reachedVoid=true;
					break;
				}
				nextPoint=new Point(coords.x, coords.y+1);
				if(map.get(nextPoint)=='.') {
					coords.y=nextPoint.y;
					coords.x=nextPoint.x;
					continue;
				}
				nextPoint=new Point(coords.x-1, coords.y+1);
				if(map.get(nextPoint)=='.') {
					coords.y=nextPoint.y;
					coords.x=nextPoint.x;
					continue;
				}
				nextPoint=new Point(coords.x+1, coords.y+1);
				if(map.get(nextPoint)=='.') {
					coords.y=nextPoint.y;
					coords.x=nextPoint.x;
					continue;
				}
				map.replace(coords, 'o');
				if(spawned%100==0) {
					printMap(map, ranges);
				}
				break;
			}
		}
		System.out.println(spawned);
	}
	
	public void printMap(HashMap<Point, Character> map, int[] ranges) {
		String str="";
		for(int iy=ranges[2]; iy<=ranges[3]; iy++) {
			for(int ix=ranges[0]; ix<=ranges[1]; ix++) {
				str+=map.get(new Point(ix, iy));
			}
			str+="\n";
		}
		System.out.println(str.length()-str.replaceAll("o", "").length());
		System.out.println(str);
	}

	protected ArrayList<Point> parseList(String input) {
		ArrayList<Point> points = new ArrayList<>();
		String[] coords = input.split("->");
		for (String coord : coords) {
			String[] pointCoord = coord.trim().split(",");
			points.add(new Point(Integer.parseInt(pointCoord[0]), Integer.parseInt(pointCoord[1])));
		}
		return points;
	}

	public static void main(String[] args) {
		new Test14_1().run();
	}
}
