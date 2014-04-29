


import org.junit.Test;

public class StudentTest {

	@Test
	public void test() {
		Student a = new Student("Paul Barber", "A100");
		Student b = new Student("Paul Barber", "A100");
		System.out.println(a.equals(b));
	}

}
