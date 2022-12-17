package day16;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test16_1 implements Runnable {

	public static void main(String[] args) {
		new Test16_1().run();
	}

	public static class Node {
		String valveName;
		int valveFlow;
		String[] neighboursKeys;

		public Node(String valveName, int valveFlow, String[] neighboursKeys) {
			super();
			this.valveName = valveName;
			this.valveFlow = valveFlow;
			this.neighboursKeys = neighboursKeys;
		}

		@Override
		public String toString() {
			String neigh = "";
			for(String neigbour:neighboursKeys) {
				neigh+=","+neigbour;
			}
			neigh="{"+neigh.substring(1)+"}";
			return valveName + "(" + valveFlow + ")"+neigh;
		}
	}

	public static class Path {
		ArrayList<Node> nodes;
		TreeSet<String> openedValves;
		Node currentValveNode;

		public Path() {
			nodes = new ArrayList<>();
			openedValves = new TreeSet<>();
		}

		public Path(Path path, Node addedNode) {
			this();
			if (path != null) {
				for (Node node : path.nodes) {
					this.nodes.add(node);
				}
				currentValveNode = path.currentValveNode;
				openedValves = new TreeSet<>(path.openedValves);
			}

			if (addedNode == null) {
				openedValves.add(currentValveNode.valveName);
			} else {

				currentValveNode = addedNode;
			}
			this.nodes.add(addedNode);

		}

		public int pathValue() {
			int value = 0;
			TreeSet<String> opened = new TreeSet<>();
			for (int ii = nodes.size()-1; ii >=0; ii--) {
				if (nodes.get(ii) != null) {
					if (openedValves.contains(nodes.get(ii).valveName) && !opened.contains(nodes.get(ii).valveName)) {
						value += nodes.get(ii).valveFlow * (ii-1);
						opened.add(nodes.get(ii).valveName);
					}

				}
			}
			return value;
		}

		public void expand(TreeMap<String, Node> nodesMap, int depth, int maxDepth) {
			// ArrayList<Path> possiblePaths = new ArrayList<Test16_1.Path>();
			boolean isValveOpen = openedValves.contains(currentValveNode.valveName);
			if (depth == maxDepth) {
				//if (pathValue() > 1700)
					System.out.println(this);
				return;
			}

			if (currentValveNode != null && !isValveOpen && currentValveNode.valveFlow > 0) {
				new Path(this, null).expand(nodesMap, depth + 1, maxDepth);
			}
			for (String key : currentValveNode.neighboursKeys) {
				Node toAdd = nodesMap.get(key);
				if (this.nodes.contains(toAdd)) {
					int last = this.nodes.lastIndexOf(toAdd);
					int lastOpened = this.nodes.lastIndexOf(null);
					if (lastOpened < last) {
						continue;
					}
				}
				new Path(this, toAdd).expand(nodesMap, depth + 1, maxDepth);
			}
		}

		@Override
		public String toString() {
			String str = "";
			Node lastValidNode = null;
			for (Node node : nodes) {
				if (node != null) {
					str += node.valveName;
					lastValidNode = node;
				} else {
					str += "O(" + lastValidNode + ")";
				}
			}
			str += "[" + pathValue() + "]";
			return str;
		}
	}

	@Override
	public void run() {
		TreeMap<String, Node> nodes = new TreeMap<>();
		File inputFile = new File("day16_validation.txt");

		try (FileReader fr = new FileReader(inputFile); BufferedReader br = new BufferedReader(fr)) {
			String line = br.readLine();
			Pattern pattern = Pattern.compile("Valve (\\w\\w) has flow rate=(\\d+); tunnels? leads? to valves? (.+)");
			Matcher matcher;
			Node node;
			while (line != null) {
				matcher = pattern.matcher(line);
				if (matcher.find()) {
					node = new Node(matcher.group(1), Integer.parseInt(matcher.group(2)), matcher.group(3).split(", "));
					System.out.println(node);
					nodes.put(node.valveName, node);
				}
				line = br.readLine();
			}
			Path testPath = new Path(null, nodes.get("AA"));
			testPath.expand(nodes, 1, 3);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
