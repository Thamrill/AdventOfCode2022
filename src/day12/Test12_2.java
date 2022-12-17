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

public class Test12_2 implements Runnable {
	private static final String path = "day12-input1.txt";

	public static void main(String[] args) {
		new Test12_2().run();
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
		
		public void reset() {
			G=Integer.MAX_VALUE;
			previous=null;
		}
	}

	HashMap<Point, GraphNode> graph;

	GraphNode end;

	LinkedList<GraphNode> openList;
	LinkedList<GraphNode> closedList;

	@Override
	public void run() {
		graph = new HashMap<>();
		File input = new File(path);
		openList = new LinkedList<>();
		closedList = new LinkedList<>();
		ArrayList<Point> possibleStarts=new ArrayList<>();
		int row = 0;
		try (FileReader fr = new FileReader(input); BufferedReader br = new BufferedReader(fr)) {
			String read = br.readLine();
			while (read != null) {
				for (int col = 0; col < read.length(); col++) {
					Point p = new Point(col, row);
					switch (read.charAt(col)) {
					case 'S':
						graph.put(p, new GraphNode(0, p));
						possibleStarts.add(p);
						break;
					case 'E':
						end = new GraphNode('z' - 'a', p);
						graph.put(p, end);
						break;
					default:
						int el=read.charAt(col) - 'a';
						graph.put(p, new GraphNode(el, p));
						if(el==0) {
							possibleStarts.add(p);
						}
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
		int best=Integer.MAX_VALUE;
		
		for(Point p:possibleStarts) {
			int cost=ComputePathLength(p);
			if(cost<best) {
				best=cost;
			}
		}
		System.out.println(best);
	}
	
	public void reset() {
		for(GraphNode node:graph.values()) {
			node.reset();
		}
		openList.clear();
		closedList.clear();
	}
	
	private int ComputePathLength(Point startPoint) {
		reset();
		GraphNode neighbour;
		GraphNode startNode=graph.get(startPoint);
		startNode.computeHCost(end.coordinates);
		startNode.G = 0;

		GraphNode tempnode = startNode;
		openList.add(tempnode);
		int tentativeGCost;
		while (!openList.isEmpty()) {
			tempnode = getNextCandidate();
			if (tempnode == end) {
				return calculatePath(end);
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
		return Integer.MAX_VALUE;
	}

	private int calculatePath(GraphNode target) {
		LinkedList<Point> path=new LinkedList<>();
		path.add(target.coordinates);
		while(target.previous!=null) {
			target=graph.get(target.previous);
			path.add(target.coordinates);
		}
		return path.size()-1;
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
