

import java.util.*;

import org.junit.Test;

public class RoomTest {

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
		Exam exam = new Exam(1);
		Exam exama = new Exam(1);
		
		//Create the modules with the above date.
		Module module = new Module("HIS100", "History", exam, studiesHistory);
		Module sport = new Module("SPO109", "Sport", exama, studiesSport);
		List<Module> allModules = new ArrayList<Module>();
		//add all the modules to an allModules list.
		allModules.add(module);
		allModules.add(sport);
		
		
		Date date = new Date();
		//Create a exam timetable using the date, for 7 days with the modules created above.
		ExamTimetableInfo examScheduler = new ExamTimetableInfo(7, date);
		//This example only has one room.
		//create a room.
		Room room = new Room("A100", RoomType.COMPUTER_CLUSTER, 50);
		Room roomb = new Room("Aw00", RoomType.LAB, 20);
		//set the timetable for the room, given the information from the examScheduler.
		room.setTimetable(examScheduler.getExamDuration(), examScheduler.getStartDate());
		System.out.println(room.compareTo(roomb));
	}

}
