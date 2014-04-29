


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbTest 
{
	public static void main(String[]args) throws SQLException
	{
		DBConnection conn = new DBConnection();
		// better way of getting this data?
		ExamTimetableInfo p = conn.loadExamPeriod();
		System.out.println("number of days: "+p.getExamDuration());
		ArrayList<Invigilator> inv = conn.loadInvigilators();
		System.out.println(inv);
		ArrayList<Room> rooms = conn.loadRooms();
		System.out.println(rooms);
		
		List<List<Module>> modules = conn.loadModules();
		
		System.out.println(modules);
		
	}
}
