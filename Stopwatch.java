package finalProject;

public class Stopwatch {
	private long start;
	public Stopwatch() {
		this.start = System.nanoTime();
	}
	public long elapsedNanos() {
		return System.nanoTime() - this.start;
	}
}