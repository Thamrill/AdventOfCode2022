package day2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.TreeMap;

public class Test2 implements Runnable {

	private static final String path = "day2-input1.txt";

	enum Played {
		ROCK, PAPER, SCISSOR
	};

	enum Result {
		WIN, DRAW, LOSS
	};

	TreeMap<Played, Integer> playedScore;
	TreeMap<Character, Played> playerCharMap;
	TreeMap<Character, Played> opponentCharMap;
	TreeMap<Result, Integer> resultScore;

	public static void main(String[] args) {
		new Test2().run();
	}

	@Override
	public void run() {
		File input = new File(path);
		playedScore = new TreeMap<>();
		playedScore.put(Played.ROCK, 1);
		playedScore.put(Played.PAPER, 2);
		playedScore.put(Played.SCISSOR, 3);

		playerCharMap = new TreeMap<>();
		playerCharMap.put('X', Played.ROCK);
		playerCharMap.put('Y', Played.PAPER);
		playerCharMap.put('Z', Played.SCISSOR);

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
			Character playerChar;
			Character opponentChar;
			Played playerPlayed;
			Played opponentPlayed;
			Result result;
			int gameScore;
			while (read != null) {
				playerChar = read.charAt(2);
				opponentChar = read.charAt(0);

				playerPlayed = playerCharMap.get(playerChar);
				opponentPlayed = opponentCharMap.get(opponentChar);
				result = computeResult(playerPlayed, opponentPlayed);
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

	private Result computeResult(Played player, Played opponent) {
		switch (player) {
		case PAPER:
			switch (opponent) {
			case PAPER:
				return Result.DRAW;
			case ROCK:
				return Result.WIN;
			case SCISSOR:
				return Result.LOSS;
			default:
				throw new IllegalArgumentException("Unexpected value: " + opponent);
			}
		case ROCK:
			switch (opponent) {
			case PAPER:
				return Result.LOSS;
			case ROCK:
				return Result.DRAW;
			case SCISSOR:
				return Result.WIN;
			default:
				throw new IllegalArgumentException("Unexpected value: " + opponent);
			}
		case SCISSOR:
			switch (opponent) {
			case PAPER:
				return Result.WIN;
			case ROCK:
				return Result.LOSS;
			case SCISSOR:
				return Result.DRAW;
			default:
				throw new IllegalArgumentException("Unexpected value: " + opponent);
			}
		default:
			throw new IllegalArgumentException("Unexpected value: " + player);
		}
	}
}
