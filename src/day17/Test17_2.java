package day17;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.TreeSet;

import day15.Test15_1.Point;

public class Test17_2 implements Runnable {

	public static void main(String[] args) {
		new Test17_2().run();
	}

	public static abstract class RockShape {
		public abstract Point[] toCheckForMovement(Direction direction);

		public abstract Point[] occupied();
	}

	public enum Direction {
		DOWN, LEFT, RIGHT;
	}

	public static class Rock {
		RockShape shape;
		Point position;
		TreeSet<Point> occupied;

		public Rock(RockShape shape, Point position) {
			super();
			this.shape = shape;
			this.position = position;
			occupied = new TreeSet<>();
			computeOccupied();
		}

		public boolean canMove(TreeSet<Point> occupied, Direction direction) {
			Point p;
			Point[] toCheck = shape.toCheckForMovement(direction);
			for (Point point : toCheck) {
				p = new Point(point.x + position.x, point.y + position.y);
				if (occupied.contains(p)) {
					return false;
				}
			}
			switch (direction) {
			case LEFT:
				for (Point point : toCheck) {
					p = new Point(point.x + position.x, point.y + position.y);
					if (p.x < 0) {
						return false;
					}
				}
				break;
			case RIGHT:
				for (Point point : toCheck) {
					p = new Point(point.x + position.x, point.y + position.y);
					if (p.x > 6) {
						return false;
					}
				}
				break;
			default:
				break;

			}
			return true;
		}

		public long getMaxHeight() {
			long maxLocal = 0;
			for (Point p : shape.occupied()) {
				maxLocal = Math.max(maxLocal, p.y);
			}
			return maxLocal + position.y;
		}

		public void move(Direction direction) {
			switch (direction) {
			case DOWN:
				position.y -= 1;
				break;
			case LEFT:
				position.x -= 1;
				break;
			case RIGHT:
				position.x += 1;
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + direction);
			}
			computeOccupied();
		}

		public boolean isInRock(Point p) {
			return occupied.contains(p);
		}

		private void computeOccupied() {
			occupied.clear();
			Point[] toCheck = shape.occupied();
			for (Point point : toCheck) {
				occupied.add(new Point(point.x + position.x, point.y + position.y));
			}
		}

		public TreeSet<Point> getOccupied() {
			return occupied;
		}
	}

	private RockShape[] generateShapes() {
		RockShape[] shapes = new RockShape[5];
		shapes[0] = new RockShape() {

			@Override
			public Point[] toCheckForMovement(Direction direction) {

				switch (direction) {
				case DOWN:
					return new Point[] { new Point(0, -1), new Point(1, -1), new Point(2, -1), new Point(3, -1) };
				case LEFT:
					return new Point[] { new Point(-1, 0) };
				case RIGHT:
					return new Point[] { new Point(4, 0) };
				default:
					throw new IllegalArgumentException("Unexpected value: " + direction);
				}
			}

			@Override
			public Point[] occupied() {
				return new Point[] { new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(3, 0) };
			}
		};
		shapes[1] = new RockShape() {

			@Override
			public Point[] toCheckForMovement(Direction direction) {

				switch (direction) {
				case DOWN:
					return new Point[] { new Point(0, 0), new Point(1, -1), new Point(2, 0) };
				case LEFT:
					return new Point[] { new Point(0, 0), new Point(-1, 1), new Point(0, 2) };
				case RIGHT:
					return new Point[] { new Point(2, 0), new Point(3, 1), new Point(2, 2) };
				default:
					throw new IllegalArgumentException("Unexpected value: " + direction);
				}
			}

			@Override
			public Point[] occupied() {
				return new Point[] { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1),
						new Point(1, 2) };
			}
		};
		shapes[2] = new RockShape() {

			@Override
			public Point[] toCheckForMovement(Direction direction) {

				switch (direction) {
				case DOWN:
					return new Point[] { new Point(0, -1), new Point(1, -1), new Point(2, -1) };
				case LEFT:
					return new Point[] { new Point(-1, 0), new Point(1, 1), new Point(1, 2) };
				case RIGHT:
					return new Point[] { new Point(3, 0), new Point(3, 1), new Point(3, 2) };
				default:
					throw new IllegalArgumentException("Unexpected value: " + direction);
				}
			}

			@Override
			public Point[] occupied() {
				return new Point[] { new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(2, 1),
						new Point(2, 2) };
			}
		};
		shapes[3] = new RockShape() {

			@Override
			public Point[] toCheckForMovement(Direction direction) {

				switch (direction) {
				case DOWN:
					return new Point[] { new Point(0, -1) };
				case LEFT:
					return new Point[] { new Point(-1, 0), new Point(-1, 1), new Point(-1, 2), new Point(-1, 3) };
				case RIGHT:
					return new Point[] { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) };
				default:
					throw new IllegalArgumentException("Unexpected value: " + direction);
				}
			}

			@Override
			public Point[] occupied() {
				return new Point[] { new Point(0, 0), new Point(0, 1), new Point(0, 2), new Point(0, 3) };
			}
		};
		shapes[4] = new RockShape() {

			@Override
			public Point[] toCheckForMovement(Direction direction) {

				switch (direction) {
				case DOWN:
					return new Point[] { new Point(0, -1), new Point(1, -1) };
				case LEFT:
					return new Point[] { new Point(-1, 0), new Point(-1, 1) };
				case RIGHT:
					return new Point[] { new Point(2, 0), new Point(2, 1) };
				default:
					throw new IllegalArgumentException("Unexpected value: " + direction);
				}
			}

			@Override
			public Point[] occupied() {
				return new Point[] { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) };
			}
		};
		return shapes;
	}

	TreeSet<Point> occupiedPositions;

	@Override
	public void run() {
		RockShape[] shapes = generateShapes();
		String path = "";
		occupiedPositions = new TreeSet<>();

		long numToFall = 1000000000000l;

		File inputFile = new File("day17-input1.txt");

		try (FileReader fr = new FileReader(inputFile); BufferedReader br = new BufferedReader(fr)) {
			path = br.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//path = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>";
		int shapePointer = 0;
		int movementPointer = 0;
		long tickCount;
		long spawnedCount = 0;
		long tallestRock = -1;
		for (long ii = -1; ii <= 8; ii++) {
			occupiedPositions.add(new Point(ii, -1));
		}
		Rock rock = null;
		for (spawnedCount = 0; spawnedCount < numToFall; spawnedCount++) {
			rock = new Rock(shapes[shapePointer], new Point(2, tallestRock + 4));
			shapePointer = (shapePointer + 1) % shapes.length;
			/*if(spawnedCount%1000000==0) {
				System.out.println(spawnedCount+"\t"+tallestRock);
			}*/
			
			if(spawnedCount%10090==7571) {
				System.out.println(spawnedCount+"\t"+tallestRock);
			}
			
			long oldTallestRock = tallestRock;
			tallestRock = rock.getMaxHeight();
			/*
			 * for (long ii = oldTallestRock; ii <= tallestRock; ii++) {
			 * occupiedPositions.add(new Point(-1, ii)); occupiedPositions.add(new Point(7,
			 * ii)); }
			 */
			
			

			tickCount = 0;
			boolean landed = false;
			while (!landed) {
				//print(rock, tallestRock);
				tickCount++;
				if (tickCount % 2 == 0) {
					if (rock.canMove(occupiedPositions, Direction.DOWN)) {
						rock.move(Direction.DOWN);
					} else {
						landed = true;
						tallestRock = Math.max(rock.getMaxHeight(), oldTallestRock);
						occupiedPositions.addAll(rock.occupied);
					}
				} else {
					Direction movement;
					switch (path.charAt(movementPointer)) {
					case '<':
						movement = Direction.LEFT;
						break;
					case '>':
						movement = Direction.RIGHT;
						break;
					default:
						throw new IllegalArgumentException("Unexpected value: " + path.charAt(movementPointer));
					}
					if (rock.canMove(occupiedPositions, movement)) {
						rock.move(movement);
					}
					movementPointer = (movementPointer + 1) % path.length();
				}
			}
		}
		//print(null, tallestRock);
		System.out.println(tallestRock+1);
	}

	private void print(Rock rock, long maxHeight) {
		String str = "";
		for (long iy = maxHeight; iy >= 0; iy--) {
			str += "\n█";// ░
			for (long ix = 0; ix < 7; ix++) {
				Point p = new Point(ix, iy);
				if (occupiedPositions.contains(p)) {
					str += "█";
				} else if (rock != null && rock.isInRock(p)) {
					str += "#";
				} else {
					str += "░";
				}
			}
			str += "█";
		}
		str += "\n█████████";

		System.out.println(str);
	}
}
