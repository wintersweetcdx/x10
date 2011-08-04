define(`Now',
`async(here) clocked$1 finish async(here) $2')
// Automatically generated by the command
// m4 ClockTest2.m4 > ClockTest2.x10
// Do not edit
import harness.x10Test;

/**
 * Test for 'now'.  Very likely to fail if now is not translated
 * properly (but depends theoretically on the scheduler).
 */
public class ClockTest2 extends x10Test {

	int val = 0;
	static final int N = 10;

	public boolean run() {
		final clock c = clock.factory.clock();
		for (int i = 0; i < N; i++) {
			Now((c), {
				async(here) {
					atomic {
						val++;
					}
				}
			})
			next;
			int temp;
			atomic { temp = val; }
			if (temp != i+1) return false;
		}
		if (c.dropped())
			return false;
		c.drop();
		if (!c.dropped())
			return false;

		return true;
	}

	public static void main(String[] args) {
		new ClockTest2().executeAsync();
	}
}
