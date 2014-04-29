



import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SchedulerMain
{

	public static void main(String [ ] args) throws SQLException
	{
		
		DBConnection conn = new DBConnection();
		
		ExamTimetableInfo info = conn.loadExamPeriod();
		
		List<List<Module>> allMods = conn.loadModules();
		
		
		ArrayList<Room> allRooms = conn.loadRooms();
		
		for(Room r: allRooms)
		{
			r.setTimetable(info.getExamDuration(), info.getStartDate());
		}
		
		//Scheduler simply takes in all rooms.
		Scheduler schedule = new Scheduler (allRooms);
		
		System.out.println(schedule.getTimeHours());
		
		//schedule the exams.
		schedule.schedule(allMods);
		
		
		for(List<Module> modules : allMods) {
			for(Module m :modules) {
			try{
			System.out.println(m + ": " + m.getExam() + "\n");
			}
			catch (NullPointerException e) {
				System.out.println(m + " exam wasn't scheduled. \n");
			}
			}
		}
		System.out.println(schedule.getTimeHours());
		
		
		conn.saveExams(allMods);
			
		
		
		
}	
}
