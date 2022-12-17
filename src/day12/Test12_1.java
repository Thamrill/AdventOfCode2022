package day12;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class Test12_1 implements Runnable {
	private static final String path = "day12-input1.txt";

	public static void main(String[] args) {
		new Test12_1().run();
	}

	public static class GraphNode {

		public GraphNode(int elevation, Point coordinates) {
			super();
			this.elevation = elevation;
			this.coordinates = coordinates;
			validNeighbours = new HashSet<>();
		}

		int elevation;
		int H;
		int G = Integer.MAX_VALUE;
		Point coordinates;
		HashSet<Point> validNeighbours;

		Point previous;

		public HashSet<Point> getValidNeighbours() {
			return validNeighbours;
		}

		public void computeHCost(Point target) {
			H = (int) Math
					.sqrt((target.x * 1 - coordinates.x * 1) ^ 2 + (target.y * 1 - coordinates.y * 1) ^ 2)/1;
		}

		public int getFCost() {
			return G + H;
		}
	}

	HashMap<Point, GraphNode> graph;

	GraphNode start;
	GraphNode end;

	LinkedList<GraphNode> openList;
	LinkedList<GraphNode> closedList;

	@Override
	public void run() {
		graph = new HashMap<>();
		File input = new File(path);
		openList = new LinkedList<>();
		closedList = new LinkedList<>();
		int row = 0;
		try (FileReader fr = new FileReader(input); BufferedReader br = new BufferedReader(fr)) {
			String read = br.readLine();
			while (read != null) {
				for (int col = 0; col < read.length(); col++) {
					Point p = new Point(col, row);
					switch (read.charAt(col)) {
					case 'S':
						start = new GraphNode(0, p);
						graph.put(p, start);
						break;
					case 'E':
						end = new GraphNode('z' - 'a', p);
						graph.put(p, end);
						break;
					default:
						graph.put(p, new GraphNode(read.charAt(col) - 'a', p));
						break;
					}
				}
				row++;
				read = br.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Point p1;
		GraphNode thisNode;
		GraphNode neighbour;
		for (Point p : graph.keySet()) {
			thisNode = graph.get(p);
			p1 = new Point(p.x - 1, p.y);
			neighbour = graph.get(p1);
			if (neighbour != null && neighbour.elevation - thisNode.elevation <= 1) {
				thisNode.getValidNeighbours().add(p1);
			}
			p1 = new Point(p.x + 1, p.y);
			neighbour = graph.get(p1);
			if (neighbour != null && neighbour.elevation - thisNode.elevation <= 1) {
				thisNode.getValidNeighbours().add(p1);
			}
			p1 = new Point(p.x, p.y - 1);
			neighbour = graph.get(p1);
			if (neighbour != null && neighbour.elevation - thisNode.elevation <= 1) {
				thisNode.getValidNeighbours().add(p1);
			}
			p1 = new Point(p.x, p.y + 1);
			neighbour = graph.get(p1);
			if (neighbour != null && neighbour.elevation - thisNode.elevation <= 1) {
				thisNode.getValidNeighbours().add(p1);
			}
		}
		start.computeHCost(end.coordinates);
		start.G = 0;

		GraphNode tempnode = start;
		openList.add(tempnode);
		int tentativeGCost;
		while (!openList.isEmpty()) {
			tempnode = getNextCandidate();
			if (tempnode == end) {
				calculatePath(end);
			}
			openList.remove(tempnode);
			closedList.add(tempnode);

			for (Point key : tempnode.getValidNeighbours()) {
				if (!graph.containsKey(key)) {
					continue;
				}
				neighbour = graph.get(key);
				if (closedList.contains(neighbour)) {
					continue;
				}
				tentativeGCost = tempnode.G + 1;
				if (tentativeGCost < neighbour.G) {
					neighbour.G = tentativeGCost;
					neighbour.previous=tempnode.coordinates;
					neighbour.computeHCost(end.coordinates);
					if(!openList.contains(neighbour)) {
						openList.add(neighbour);
					}
				}
			}
		}

	}

	private void calculatePath(GraphNode target) {
		LinkedList<Point> path=new LinkedList<>();
		path.add(target.coordinates);
		while(target.previous!=null) {
			target=graph.get(target.previous);
			path.add(target.coordinates);
		}
		System.out.println(path);
		System.out.println(path.size());
	}

	private GraphNode getNextCandidate() {
		Collections.sort(openList, new Comparator<GraphNode>() {

			@Override
			public int compare(GraphNode arg0, GraphNode arg1) {

				return Integer.compare(arg0.getFCost(), arg1.getFCost());
			}
		});
		return openList.getFirst();
	}

}
