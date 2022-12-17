package day11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Test11_1 implements Runnable {

	public static void main(String[] args) {
		new Test11_1().run();
	}

	protected abstract static class Monkey {
		ArrayList<Long> items;
		int falseTarget;
		int trueTarget;
		int inspectionCount;
		int testValue;

		public Monkey(ArrayList<Integer> items, int falseTarget, int trueTarget, int testValue) {
			super();
			this.items = new ArrayList<>();
			for (Integer integer : items) {
				this.items.add(integer.longValue());
			}
			this.falseTarget = falseTarget;
			this.trueTarget = trueTarget;
			this.testValue = testValue;
			inspectionCount = 0;
		}

		public abstract long computeNewWorry(long old);

		public boolean test(long worry) {
			return ((worry + testValue) % testValue) == 0;
		}

		public long[] inspect() {
			long[] returnValue = new long[2];
			if (items.isEmpty()) {
				returnValue[0] = -1;
				return returnValue;
			}
			inspectionCount++;
			long inspected = items.remove(0);
			inspected = computeNewWorry(inspected);
			System.out.println(inspected);
			inspected = Math.floorDiv(inspected, 3);
			returnValue[1] = inspected;
			if (test(inspected)) {
				returnValue[0] = trueTarget;
			} else {
				returnValue[0] = falseTarget;
			}
			return returnValue;
		}

		public void addItem(long value) {
			items.add(value);
		}
	}

	ArrayList<Monkey> monkeys;

	@Override
	public void run() {
		monkeys = new ArrayList<>();
		// 0
		monkeys.add(new Monkey(new ArrayList<Integer>(Arrays.asList(72, 64, 51, 57, 93, 97, 68)), 7, 4, 17) {

			@Override
			public long computeNewWorry(long old) {
				return old * 19;
			}
		});
		// 1
		monkeys.add(new Monkey(new ArrayList<Integer>(Arrays.asList(62)), 2, 3, 3) {
			@Override
			public long computeNewWorry(long old) {
				return old * 11;
			}
		});
		// 2
		monkeys.add(new Monkey(new ArrayList<Integer>(Arrays.asList(57, 94, 69, 79, 72)), 4, 0, 19) {

			@Override
			public long computeNewWorry(long old) {
				return old + 6;
			}
		});
		// 3
		monkeys.add(new Monkey(new ArrayList<Integer>(Arrays.asList(80, 64, 92, 93, 64, 56)), 0, 2, 7) {

			@Override
			public long computeNewWorry(long old) {
				return old + 5;
			}
		});
		// 4
		monkeys.add(new Monkey(new ArrayList<Integer>(Arrays.asList(70, 88, 95, 99, 78, 72, 65, 94)), 5, 7, 2) {
			@Override
			public long computeNewWorry(long old) {
				return old + 7;
			}
		});
		// 5
		monkeys.add(new Monkey(new ArrayList<Integer>(Arrays.asList(57, 95, 81, 61)), 6, 1, 5) {

			@Override
			public long computeNewWorry(long old) {
				return old * old;
			}
		});
		// 6
		monkeys.add(new Monkey(new ArrayList<Integer>(Arrays.asList(79, 99)), 1, 3, 11) {

			@Override
			public long computeNewWorry(long old) {
				return old + 2;
			}
		});
		// 7
		monkeys.add(new Monkey(new ArrayList<Integer>(Arrays.asList(68, 98, 62)), 6, 5, 13) {

			@Override
			public long computeNewWorry(long old) {
				return old + 3;
			}
		});

		for (int ii = 0; ii < 20; ii++) {
			playRound();
		}
		ArrayList<Integer> inspections = new ArrayList<>();
		for (int ii = 0; ii < monkeys.size(); ii++) {
			inspections.add(monkeys.get(ii).inspectionCount);
		}
		Collections.sort(inspections);
		Collections.reverse(inspections);
		System.out.println(inspections.get(0) * inspections.get(1));
	}

	public void playRound() {
		for (int ii = 0; ii < monkeys.size(); ii++) {
			playMonkeyRound(ii);
		}
	}

	private void playMonkeyRound(int ii) {
		Monkey monkey = monkeys.get(ii);

		long[] returnedValue = monkey.inspect();
		while (returnedValue[0] != -1) {
			monkeys.get((int) returnedValue[0]).addItem(returnedValue[1]);
			returnedValue = monkey.inspect();
		}
	}

}
