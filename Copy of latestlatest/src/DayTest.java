
import java.util.*;

import org.junit.Test;

public class DayTest {

	@Test
	public void test() {
		Date d = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		
		Day day = new Day(d);
		System.out.println(day);
	}

}
