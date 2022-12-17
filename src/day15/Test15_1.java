package day15;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test15_1 implements Runnable {

	public static class Point implements Comparable<Point> {
		public long x;
		public long y;

		public long manhattanDistanceFrom(Point p1) {
			return Math.abs(x - p1.x) + Math.abs(y - p1.y);
		}

		@Override
		public int compareTo(Point o) {
			if (Long.compare(x, o.x) == 0) {
				return Long.compare(y, o.y);
			}
			return Long.compare(x, o.x);
		}

		public Point(long x, long y) {
			super();
			this.x = x;
			this.y = y;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Point) {
				return compareTo((Point) obj)==0;
			}
			return false;
		}
	}

	@Override
	public void run() {
		long minX = Long.MAX_VALUE;
		long maxX = Long.MIN_VALUE;
		long minY = Long.MAX_VALUE;
		long maxY = Long.MIN_VALUE;

		File inputText = new File("day15-input1.txt");
		//inputText = new File("day15_validation.txt");

		TreeMap<Point, Point> closest = new TreeMap<>();
		TreeMap<Point, Long> distance = new TreeMap<>();
		TreeSet<Point> beacons=new TreeSet<>();
		try (FileReader fr = new FileReader(inputText); BufferedReader br = new BufferedReader(fr)) {
			String line = br.readLine();
			Pattern pattern = Pattern
					.compile("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)");
			Matcher matcher;
			
			while (line != null) {
				matcher = pattern.matcher(line);
				if (matcher.find()) {
					Long xS = Long.parseLong(matcher.group(1));
					Long yS = Long.parseLong(matcher.group(2));
					Long xB = Long.parseLong(matcher.group(3));
					Long yB = Long.parseLong(matcher.group(4));
					Point sensor = new Point(xS, yS);
					Point beacon = new Point(xB, yB);
					long dist=sensor.manhattanDistanceFrom(beacon);
					beacons.add(beacon);
					closest.put(sensor, beacon);
					distance.put(sensor, dist);
					minX = Math.min(minX, xS-dist);
					maxX = Math.max(maxX, xS+dist);
					minY = Math.min(minY, yS-dist);
					maxY = Math.max(maxY, yS+dist);
				}
				line = br.readLine();
			}
			long count = 0;
			long y = 2000000;
			boolean valid;
			
			for (long ix = minX-10; ix <= maxX+10; ix++) {
				valid = false;
				Point test = new Point(ix, y);
				for (Point p : distance.keySet()) {
					if(beacons.contains(test)) {
						break;
					}else if (p.manhattanDistanceFrom(test) <= distance.get(p)) {
						valid = true;
						break;
					}
				}
				if (valid) {
					count++;
				}
			}
			System.out.println(count);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Test15_1().run();
	}

}
