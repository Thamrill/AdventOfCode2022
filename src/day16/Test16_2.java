package day16;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test16_2 implements Runnable {

	public static void main(String[] args) {
		new Test16_2().run();
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
			for (String neigbour : neighboursKeys) {
				neigh += "," + neigbour;
			}
			neigh = "{" + neigh.substring(1) + "}";
			return valveName + "(" + valveFlow + ")" + neigh;
		}
	}

	public static class Action {
		protected int step;

		public Action(int step) {
			super();
			this.step = step;
		}
	};

	public static class Move extends Action {
		String targetNode;

		public Move(String targetNode, int step) {
			super(step);
			this.targetNode = targetNode;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Move) {
				return targetNode.equalsIgnoreCase(((Move) obj).targetNode);
			}
			return false;
		}

		@Override
		public String toString() {
			return ">" + targetNode;
		}
	};

	public static class OpenValve extends Action {
		String valve;

		public OpenValve(String valve, int step) {
			super(step);
			this.valve = valve;
		}

		@Override
		public String toString() {
			return "{" + valve + "}";
		}
	};

	public static class Path {
		ArrayList<Action> actions;
		ArrayList<Action> actionsElefant;
		TreeMap<String, Integer> openedValves;
		Node currentValveNode;
		Node currentValveNodeElefant;
		int value;

		public static int maxValves;
		
		private static long lastPrint=0;

		public Path(Node currentpos) {
			actions = new ArrayList<>();
			actionsElefant = new ArrayList<>();
			openedValves = new TreeMap<>();
			currentValveNode = currentpos;
			currentValveNodeElefant = currentpos;
		}

		public Path(Path original, Action[] newActions, TreeMap<String, Node> map) {
			this(original.currentValveNode);
			currentValveNode=original.currentValveNode;
			currentValveNodeElefant=original.currentValveNodeElefant;
			actions = new ArrayList<>(original.actions);
			actionsElefant = new ArrayList<>(original.actionsElefant);
			openedValves = new TreeMap<>();
			openedValves.putAll(original.openedValves);
			Action action = newActions[0];
			actions.add(action);
			if (action instanceof Move) {
				move((Move) action, map);
			} else if (action instanceof OpenValve) {
				openvalve((OpenValve) action);
			}
			action = newActions[1];
			actionsElefant.add(action);
			if (action instanceof Move) {
				moveElefant((Move) action, map);
			} else if (action instanceof OpenValve) {
				openvalve((OpenValve) action);
			}
		}

		public void openvalve(OpenValve action) {
			openedValves.putIfAbsent(action.valve, action.step);
		}

		public void move(Move move, TreeMap<String, Node> map) {
			currentValveNode = map.get(move.targetNode);
		}

		public void moveElefant(Move move, TreeMap<String, Node> map) {
			currentValveNodeElefant = map.get(move.targetNode);
		}

		@Override
		public String toString() {
			String str = "";

			for (Action action : actions) {
				str += "," + action.toString();
			}
			str += "\n";
			for (Action action : actionsElefant) {
				str += "," + action.toString();
			}
			str += "\t[" + value + "]";
			return str;
		}

		public int lastValveOpenedIndex() {
			for (int ii = actions.size() - 1; ii >= 0; ii--) {
				if (actions.get(ii) instanceof OpenValve) {
					return ii;
				}
			}
			return -1;
		}

		public int lastValveOpenedByElefantIndex() {
			for (int ii = actionsElefant.size() - 1; ii >= 0; ii--) {
				if (actionsElefant.get(ii) instanceof OpenValve) {
					return ii;
				}
			}
			return -1;
		}

		public int value(int step, TreeMap<String, Node> map) {
			int total = 0;
			for (String key : openedValves.keySet()) {
				total += map.get(key).valveFlow * (step - openedValves.get(key) + 1);
			}
			value = total;
			return total;
		}
		
		public boolean prune(TreeMap<String, Node> nodes) {
			
			boolean valve=false;
			for(Action action:actions) {
				if(action instanceof OpenValve) {
					valve=true;
					break;
				}
			}
			if(!valve) {
				return true;
			}
			valve=false;
			for(Action action:actionsElefant) {
				if(action instanceof OpenValve) {
					valve=true;
					break;
				}
			}
			if(!valve) {
				return true;
			}
			return false;
			
		}

		public int process(TreeMap<String, Node> nodes, int depth, int maxDepth, int currentBest) {
			ArrayList<Action> possibleActions = new ArrayList<>();
			
			if(System.currentTimeMillis()-lastPrint>1000*60*30) {
				lastPrint=System.currentTimeMillis();
				System.out.print("[update]");
				System.out.println(this);
			}

			if (openedValves.size() == maxValves) {
				int max = value(maxDepth - 1, nodes);
				if (max > currentBest) {
					System.out.println(this);
				} else {
					max = currentBest;
				}
				return max;
			}

			
			for (String target : currentValveNode.neighboursKeys) {
				Move move = new Move(target, depth);
				if (actions.contains(move)) {
					int index = actions.lastIndexOf(move);
					int lastValveOpened = lastValveOpenedIndex();
					if (index > lastValveOpened) {
						continue;
					}
				}
				possibleActions.add(move);
			}
			Collections.shuffle(possibleActions);
			
			
			if ((!openedValves.containsKey(currentValveNode.valveName)) && currentValveNode.valveFlow > 0) {
				possibleActions.add(0, new OpenValve(currentValveNode.valveName, depth));
			}
			ArrayList<Action> possibleElefantActions = new ArrayList<>();
			for (String target : currentValveNodeElefant.neighboursKeys) {
				Move move = new Move(target, depth);
				if (actionsElefant.contains(move)) {
					int index = actionsElefant.lastIndexOf(move);
					int lastValveOpened =lastValveOpenedByElefantIndex();
					if (index > lastValveOpened) {
						continue;
					}
				}
				possibleElefantActions.add(move);
			}
			Collections.shuffle(possibleElefantActions);
			
			if ((!openedValves.containsKey(currentValveNodeElefant.valveName))
					&& currentValveNodeElefant.valveFlow > 0) {
				possibleElefantActions.add(0, new OpenValve(currentValveNodeElefant.valveName, depth));
			}

			if(possibleElefantActions.isEmpty()||possibleActions.isEmpty()) {
				return 0;
			}
			
			if(depth==10&&prune(nodes)) {
				return 0;
			}
			
			int max = value(depth - 1, nodes);
			if (max > currentBest) {
				System.out.println(this);
			} else {
				max = currentBest;
			}

			Path testPath;

			for (Action playerAction : possibleActions) {
				for (Action elefantAction : possibleElefantActions) {
					testPath = new Path(this, new Action[] { playerAction, elefantAction }, nodes);
					if (depth < maxDepth) {
						max = Math.max(max, testPath.process(nodes, depth + 1, maxDepth, max));
					}
				}
			}

			return max;
		}
	}

	@Override
	public void run() {
		TreeMap<String, Node> nodes = new TreeMap<>();
		File inputFile = new File("day16-input1.txt");
		//inputFile=new File("day16_validation.txt");

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
			int count = 0;
			for (Node node1 : nodes.values()) {
				if (node1.valveFlow > 0) {
					count++;
				}
			}
			Path.maxValves = count;

			Path path = new Path(nodes.get("AA"));
			path.actions.add(new Move("AA", 0));
			path.actionsElefant.add(new Move("AA", 0));

			path.process(nodes, 1, 26, 0);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
