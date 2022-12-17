package day11;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import day11.Test11_2.Monkey.Operation;

public class Test11_2 implements Runnable {

	public static void main(String[] args) {
		new Test11_2().run();
	}

	public static BigInteger MAGICVALUE = BigInteger.valueOf(9699690);

	protected static class Monkey {
		ArrayList<BigInteger> items;
		int falseTarget;
		int trueTarget;
		long inspectionCount;
		BigInteger testValue;

		public enum Operation {
			ADD, MULT, SQUARE
		};

		BigInteger constant;
		Operation operation;

		public Monkey(ArrayList<Integer> items, int falseTarget, int trueTarget, int testValue, Operation operation,
				int constant) {
			super();
			this.items = new ArrayList<>();
			for (Integer integer : items) {
				this.items.add(BigInteger.valueOf(integer));
			}
			this.falseTarget = falseTarget;
			this.trueTarget = trueTarget;
			this.testValue = BigInteger.valueOf(testValue);
			this.operation = operation;
			this.constant = BigInteger.valueOf(constant);
			inspectionCount = 0;
		}

		public BigInteger computeNewWorry(BigInteger old) {
			switch (operation) {
			case ADD:
				return old.add(constant);
			case MULT:
				return old.multiply(constant);
			case SQUARE:
				return old.multiply(old);
			}
			return null;
		}

		public boolean test(BigInteger worry) {
			return worry.mod(testValue).compareTo(BigInteger.ZERO) == 0;
		}

		public BigInteger[] inspect() {
			BigInteger[] returnValue = new BigInteger[2];
			if (items.isEmpty()) {
				returnValue[0] = BigInteger.valueOf(-1);
				return returnValue;
			}
			inspectionCount++;
			BigInteger inspected;
			inspected = items.remove(0);
			inspected = computeNewWorry(inspected);
			inspected = inspected.mod(MAGICVALUE);
			returnValue[1] = inspected;
			if (test(inspected)) {
				returnValue[0] = BigInteger.valueOf(trueTarget);
			} else {
				returnValue[0] = BigInteger.valueOf(falseTarget);
			}
			return returnValue;
		}

		public void addItem(BigInteger value) {
			items.add(value);
		}
	}

	ArrayList<Monkey> monkeys;

	@Override
	public void run() {
		monkeys = new ArrayList<>();
		monkeys.add(new Monkey(new ArrayList<Integer>(Arrays.asList(72, 64, 51, 57, 93, 97, 68)), 7, 4, 17,
				Test11_2.Monkey.Operation.MULT, 19));
		monkeys.add(new Monkey(new ArrayList<Integer>(Arrays.asList(62)), 2, 3, 3, Operation.MULT, 11));
		monkeys.add(new Monkey(new ArrayList<Integer>(Arrays.asList(57, 94, 69, 79, 72)), 4, 0, 19, Operation.ADD, 6));
		monkeys.add(
				new Monkey(new ArrayList<Integer>(Arrays.asList(80, 64, 92, 93, 64, 56)), 0, 2, 7, Operation.ADD, 5));
		monkeys.add(new Monkey(new ArrayList<Integer>(Arrays.asList(70, 88, 95, 99, 78, 72, 65, 94)), 5, 7, 2,
				Operation.ADD, 7));
		monkeys.add(new Monkey(new ArrayList<Integer>(Arrays.asList(57, 95, 81, 61)), 6, 1, 5, Operation.SQUARE, 0));
		monkeys.add(new Monkey(new ArrayList<Integer>(Arrays.asList(79, 99)), 1, 3, 11, Operation.ADD, 2));
		monkeys.add(new Monkey(new ArrayList<Integer>(Arrays.asList(68, 98, 62)), 6, 5, 13, Operation.ADD, 3));

		/*monkeys = new ArrayList<>();
		monkeys.add(new Monkey(new ArrayList<Integer>(Arrays.asList(79, 98)), 3, 2, 23, Operation.MULT, 19));
		monkeys.add(new Monkey(new ArrayList<Integer>(Arrays.asList(54, 65, 75, 74)), 0, 2, 19, Operation.ADD, 6));
		monkeys.add(new Monkey(new ArrayList<Integer>(Arrays.asList(79, 60, 97)), 3, 1, 13, Operation.SQUARE, 6));
		monkeys.add(new Monkey(new ArrayList<Integer>(Arrays.asList(74)), 1, 0, 17, Operation.ADD, 3));*/

		int magic2 = 1;
		for (int ii = 0; ii < monkeys.size(); ii++) {
			magic2 *= monkeys.get(ii).testValue.intValue();
		}
		MAGICVALUE=BigInteger.valueOf(magic2);
		for (int ii = 0; ii < 10000; ii++) {
			playRound();
		}

		ArrayList<Long> inspections = new ArrayList<>();
		for (int jj = 0; jj < monkeys.size(); jj++) {
			inspections.add(monkeys.get(jj).inspectionCount);
		}
		Collections.sort(inspections);
		Collections.reverse(inspections);
		System.out.println((inspections.get(0)) * inspections.get(1));

	}

	public void playRound() {
		for (int ii = 0; ii < monkeys.size(); ii++) {
			playMonkeyRound(ii);
		}
	}

	private void playMonkeyRound(int ii) {
		Monkey monkey = monkeys.get(ii);

		BigInteger[] returnedValue = monkey.inspect();
		while (returnedValue[0].intValue() != -1) {
			monkeys.get(returnedValue[0].intValue()).addItem(returnedValue[1]);
			returnedValue = monkey.inspect();
		}
	}

}