package day11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Test11_Validation implements Runnable {

	public static void main(String[] args) {
		new Test11_Validation().run();
	}
	
	protected abstract static class Monkey {
		ArrayList<Integer> items;
		int falseTarget;
		int trueTarget;
		int inspectionCount;

		public Monkey(ArrayList<Integer> items, int falseTarget, int trueTarget) {
			super();
			this.items = items;
			this.falseTarget = falseTarget;
			this.trueTarget = trueTarget;
			inspectionCount = 0;
		}

		public abstract int computeNewWorry(int old);

		public abstract boolean test(int worry);

		public int[] inspect() {
			int[] returnValue = new int[2];
			if (items.isEmpty()) {
				returnValue[0] = -1;
				return returnValue;
			}
			inspectionCount++;
			int inspected = items.remove(0);
			inspected = computeNewWorry(inspected);
			inspected=Math.floorDiv(inspected, 3);
			returnValue[1] = inspected;
			if (test(inspected)) {
				returnValue[0] = trueTarget;
			} else {
				returnValue[0] = falseTarget;
			}
			return returnValue;
		}

		public void addItem(int value) {
			items.add(value);
		}
	}

	ArrayList<Monkey> monkeys;

	@Override
	public void run() {
		monkeys = new ArrayList<>();
		// 0
		monkeys.add(new Monkey(new ArrayList<Integer>(Arrays.asList(79, 98)), 3, 2) {

			@Override
			public boolean test(int worry) {
				return worry % 23 == 0;
			}

			@Override
			public int computeNewWorry(int old) {
				return old * 19;
			}
		});
		// 1
		monkeys.add(new Monkey(new ArrayList<Integer>(Arrays.asList(54, 65, 75, 74)), 0, 2) {

			@Override
			public boolean test(int worry) {
				return worry % 19 == 0;
			}

			@Override
			public int computeNewWorry(int old) {
				return old +6;
			}
		});
		// 2
		monkeys.add(new Monkey(new ArrayList<Integer>(Arrays.asList(79, 60, 97)), 3, 1) {

			@Override
			public boolean test(int worry) {
				return worry % 13 == 0;
			}

			@Override
			public int computeNewWorry(int old) {
				return old *old;
			}
		});
		// 3
		monkeys.add(new Monkey(new ArrayList<Integer>(Arrays.asList(74)), 1, 0) {

			@Override
			public boolean test(int worry) {
				return worry % 17 == 0;
			}

			@Override
			public int computeNewWorry(int old) {
				return old + 3;
			}
		});

		for (int ii = 0; ii < 20; ii++) {
			playRound();
		}
		ArrayList<Integer> inspections=new ArrayList<>();
		for(int ii=0; ii<monkeys.size(); ii++) {
			inspections.add(monkeys.get(ii).inspectionCount);
		}
		Collections.sort(inspections);
		Collections.reverse(inspections);
		System.out.println(inspections.get(0)*inspections.get(1));
	}

	public void playRound() {
		for(int ii=0; ii<monkeys.size(); ii++) {
			playMonkeyRound(ii);
		}
	}

	private void playMonkeyRound(int ii) {
		Monkey monkey=monkeys.get(ii);
		
		int[] returnedValue=monkey.inspect();
		while(returnedValue[0]!=-1) {
			monkeys.get(returnedValue[0]).addItem(returnedValue[1]);
			returnedValue=monkey.inspect();
		}
	}

}
