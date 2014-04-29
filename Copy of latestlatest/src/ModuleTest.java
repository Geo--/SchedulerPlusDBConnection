


import java.util.*;

import org.junit.Test;

public class ModuleTest {

	@Test
	public void test() {
		//Create students, and populate them into a List of there studies.
		Student studenta = new Student("Paul Barber", "1000");
		Student studentb = new Student("Chris Cooper", "A1000");
		Student studentc = new Student("Paul, Barber", "B1000");
		List<Student> studiesHistory = new ArrayList<Student> ();
		studiesHistory.add(studenta);
		studiesHistory.add(studentb);
		studiesHistory.add(studentc);
		List<Student> studiesSport = new ArrayList<Student>();
		studiesSport.add(studentb);
		
		//Create exams for the modules.
		Exam exam = new Exam(2);
		Exam exama = new Exam(1);
		
		//Create the modules with the above date.
		Module module = new Module("HIS100", "History", exam, studiesHistory);
		Module sport = new Module("SPO109", "Sport", exama, studiesSport);
		List<Module> allModules = new ArrayList<Module>();
		//add all the modules to an allModules list.
		allModules.add(module);
		allModules.add(sport);
		
		//Set the Modules List in the Student Class.
		System.out.println(module.getStudentsEnrolled());
		System.out.println(sport.getStudentsEnrolled());
		
		System.out.println(module.compareTo(sport));
		
		
	}

}
