package day2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.TreeMap;

public class Test2_2 implements Runnable {

	private static final String path = "day2-input1.txt";

	enum Played {
		ROCK, PAPER, SCISSOR
	};

	enum Result {
		WIN, DRAW, LOSS
	};

	TreeMap<Played, Integer> playedScore;
	TreeMap<Character, Played> opponentCharMap;
	TreeMap<Character, Result> resultCharMap;
	TreeMap<Result, Integer> resultScore;

	public static void main(String[] args) {
		new Test2_2().run();
	}

	@Override
	public void run() {
		File input = new File(path);
		playedScore = new TreeMap<>();
		playedScore.put(Played.ROCK, 1);
		playedScore.put(Played.PAPER, 2);
		playedScore.put(Played.SCISSOR, 3);

		resultCharMap = new TreeMap<>();
		resultCharMap.put('X', Result.LOSS);
		resultCharMap.put('Y', Result.DRAW);
		resultCharMap.put('Z', Result.WIN);

		opponentCharMap = new TreeMap<>();
		opponentCharMap.put('A', Played.ROCK);
		opponentCharMap.put('B', Played.PAPER);
		opponentCharMap.put('C', Played.SCISSOR);

		resultScore = new TreeMap<>();
		resultScore.put(Result.WIN, 6);
		resultScore.put(Result.DRAW, 3);
		resultScore.put(Result.LOSS, 0);

		int score = 0;

		try (FileReader fr = new FileReader(input); BufferedReader br = new BufferedReader(fr)) {
			String read = br.readLine();
			Character resultChar;
			Character opponentChar;
			Result result;
			
			Played opponentPlayed;
			Played playerPlayed;
			int gameScore;
			while (read != null) {
				resultChar = read.charAt(2);
				opponentChar = read.charAt(0);
				result = resultCharMap.get(resultChar);
				opponentPlayed = opponentCharMap.get(opponentChar);
				playerPlayed = compute2Play(result, opponentPlayed);
				gameScore=resultScore.get(result)+playedScore.get(playerPlayed);
				score+=gameScore;
				System.out.println(read+">"+playerPlayed+":"+opponentPlayed+"="+result+"("+gameScore+")");
				read = br.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(score);
	}

	private Played compute2Play(Result result, Played opponent) {
		switch (result) {
		case LOSS:
			switch (opponent) {
			case PAPER:
				return Played.ROCK;
			case ROCK:
				return Played.SCISSOR;
			case SCISSOR:
				return Played.PAPER;
			default:
				throw new IllegalArgumentException("Unexpected value: " + opponent);
			}
		case DRAW:
			return opponent;
		case WIN:
			switch (opponent) {
			case PAPER:
				return Played.SCISSOR;
			case ROCK:
				return Played.PAPER;
			case SCISSOR:
				return Played.ROCK;
			default:
				throw new IllegalArgumentException("Unexpected value: " + opponent);
			}
		default:
			throw new IllegalArgumentException("Unexpected value: " + result);
		}
	}
}
