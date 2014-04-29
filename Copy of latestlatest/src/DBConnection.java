
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;


/**
 * @author Geo
 * class to manage connection to the SQL database
 * load and save data
 */
public class DBConnection 
{
	private String dbName;
	private Connection con;
	private String userName = "t8005t6";
	private String password = "8Hit-Car";
	private String dbms = "mysql";
	private String serverName = "homepages.cs.ncl.ac.uk";
	private String portNumber = "3306";
	private java.util.Date startDate;
	private int days;
	
	
	/**
	 * Establish the database connection
	 * @throws SQLException
	 */
	public DBConnection() throws SQLException
	{
		dbName = "t8005t6";
		con = getConnection();
	}
	
	/**
	 * Establish the database connection, connecting using login details
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException 
	{
		System.out.println("connecting to database..");
	    Connection conn = null;
	    Properties connectionProps = new Properties();
	    connectionProps.put("user", this.userName);
	    connectionProps.put("password", this.password);
	   
	   
	    conn = DriverManager.getConnection("jdbc:" + this.dbms + "://" + this.serverName +
	                   ":" + this.portNumber + "/",
	                   connectionProps);
	    
	    System.out.println("Connected to database");
	    return conn;
	}
	
	
	/**
	 * Retrieve details of the exam period
	 * @return ExamTimetableInfo object which contains details of the examination period
	 * @throws SQLException
	 */
	public ExamTimetableInfo loadExamPeriod() throws SQLException
	{		
		int year;
		int day;
		int month;
		Statement stmt = null;
		String query = "select startDate, days from " + dbName +".ExamPeriod";
		System.out.println("loading exam period details from database..");
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next())
			{
				String[] strDate = rs.getString("startDate").split("-");
				year = Integer.parseInt(strDate[0]);
				month = Integer.parseInt(strDate[1]);
				day = Integer.parseInt(strDate[2]);
				Calendar cal = Calendar.getInstance();
				cal.set(year, month, day);
				startDate = cal.getTime();
				days = rs.getInt("days");
			}
			//ExamTimetableInfo(int examDuration, Date startDate)
			ExamTimetableInfo examPeriod = new ExamTimetableInfo(days, startDate);
			return examPeriod;
			
		}
		catch (SQLException e)
		{
			System.out.println("failed to load");
		}
		finally
		{
			if (stmt != null)
			{
				stmt.close();
				
			}
		}
		return null;
		
	}
	
	
	/**
	 * Load all the Room details currently stored in the database
	 * @return all the rooms in the database
	 * @throws SQLException
	 */
	public ArrayList<Room> loadRooms() throws SQLException
		{
			ArrayList<Room> dbRooms = new ArrayList<Room>();
			Statement stmt = null;
			//wait for correct sql statement
			String query = "SELECT * FROM " + dbName +".Room";
			try
			{
			
				stmt = con.createStatement();
				ResultSet roomQuery = stmt.executeQuery(query);
				RoomType type;
				while (roomQuery.next())
				{
					//System.out.println(roomQuery.getString("type"));	
					if (roomQuery.getString("type").equals("LAB"))
					{
						type = RoomType.LAB;
					}
					else if (roomQuery.getString("type").equals("PC"))
					{
						type = RoomType.COMPUTER_CLUSTER;
					}
					else
					{
						type = RoomType.LECTURE_THEATRE;
					}
					dbRooms.add(new Room(roomQuery.getInt("roomID"), roomQuery.getString("roomName"), type, roomQuery.getInt("capacity")));
				}
			}
			catch (SQLException e)
			{
				System.out.println("failed to load");
			}
			finally
			{
				if (stmt != null)
				{
					stmt.close();
				}
			}
			
			return dbRooms;			
		}

		
		/**
		 * load a list of all the Modules including students and exam details
		 * @return List of modules
		 * @throws SQLException
		 */
		public List<List<Module>> loadModules() throws SQLException
		{
			String examquery = "SELECT * FROM " + dbName +".Module JOIN " + dbName +".Exam_Module ON " 
					+ dbName +".Module.moduleID = " + dbName +".Exam_Module.moduleID JOIN "
					+ dbName +".Exam ON " + dbName +".Exam_Module.examID = " + dbName +".Exam.examID WHERE clashID IS NULL";
			Statement stmt2 = con.createStatement();
			Statement stmt8 = con.createStatement();
			ArrayList<List<Module>> dbModules = new ArrayList<List<Module>>();
			//get list of all the clash types
			String clashQuery = "SELECT idClash FROM " + dbName +".Clash";
			String clashExams = "SELECT * FROM " + dbName +".Module JOIN " + dbName +".Exam_Module ON " 
					+ dbName +".Module.moduleID = " + dbName +".Exam_Module.moduleID JOIN "
					+ dbName +".Exam ON " + dbName +".Exam_Module.examID = " + dbName +".Exam.examID JOIN "+ dbName +".Clash ON "+ dbName +".Clash.idClash = "+ dbName +".Exam.clashID WHERE "+ dbName +".Clash.idClash = ";
			ResultSet clashQ = stmt8.executeQuery(clashQuery);
			
			//get the lists of clashed modules
			while (clashQ.next())
			{
				//for all clash groups create a list
				List<Module> clash = new ArrayList<Module>();
				//get the exams with the same clash id
				ResultSet rs4 = stmt2.executeQuery(clashExams+"'"+clashQ.getInt("idClash")+"'");
				//System.out.println(clashExams+"'"+clashQ.getInt("idClash")+"'");
				RoomType type = null;
					
				
				while (rs4.next())
				{
					//change to enum?
					if (rs4.getString("type") != null)
					{
						if (rs4.getString("type").equals("PC"))
						{
							type = RoomType.COMPUTER_CLUSTER;
						}
						if (rs4.getString("type").equals("LAB"))
						{
							type = RoomType.LAB;
						}
						else
						{
							type = null;
						}
					}
					Exam e = new Exam(rs4.getInt("examID"), rs4.getInt("duration"), type);
				
				List<Student> studentsEnrolled = loadStudents(rs4.getString("moduleID"));
				Module module = new Module(rs4.getString("moduleID"), rs4.getString("moduleName"), e, studentsEnrolled);
				
				//add the clashed module
				clash.add(module);
				}
			
				dbModules.add(clash);
				
			}
			//get the lists of single modules
			
				
			System.out.println("loading exams from database..");
			try 
			{
				ResultSet rs2 = stmt2.executeQuery(examquery);
				RoomType type = null;
				while (rs2.next())
				{
					if (rs2.getString("type") != null)
					{
						if (rs2.getString("type").equals("PC"))
						{
							type = RoomType.COMPUTER_CLUSTER;
						}
						if (rs2.getString("type").equals("LAB"))
						{
							type = RoomType.LAB;
						}
						else
						{
							type = null;
						}
					}
					Exam e = new Exam(rs2.getInt("examID"), rs2.getInt("duration"), type);
					
					//use the result of the last query to get a list of students using the module id
					List<Student> studentsEnrolled = loadStudents(rs2.getString("moduleID"));
					Module module = new Module(rs2.getString("moduleID"), rs2.getString("moduleName"), e, studentsEnrolled);
					List<Module> single = new ArrayList<Module>();
					single.add(module);
					dbModules.add(single);
				}
				}
				catch (SQLException e)
				{
					System.out.println("failed to load modules");
				}
				finally
				{
					if (stmt2 != null)
					{
						stmt2.close();
					}
			}
			return dbModules;
		}
		
			
		/**
		 * Get a list of all the students studying a particular module
		 * @param moduleID the module ID of the students requested
		 * @return list of students
		 * @throws SQLException
		 */
		private List<Student> loadStudents(String moduleID) throws SQLException
		{
			List<Student> studentsEnrolled = new ArrayList<Student>();
			String studentsEnrolledQuery = "SELECT * FROM " + dbName +".Student_Module JOIN " + dbName +".Student ON " + dbName +".Student_Module.studentID = " + dbName +".Student.studentID WHERE " + dbName +".Student_Module.moduleID = ";
			//execute studentsEnrolledQuery with rs2.getString ("moduleID") added to string to populate the student list
			String query = studentsEnrolledQuery + "\""+moduleID+"\"";
			Statement stmt = null;
			stmt = con.createStatement();
			ResultSet rs3 = stmt.executeQuery(query);
			while (rs3.next())
			{
				studentsEnrolled.add(new Student(rs3.getString("studentName"), rs3.getString("studentID")));
			}
			return studentsEnrolled;
		}

		
		/**
		 * Save the updated exam details to the database
		 * @param modules The modules which have been scheduled
		 * @throws SQLException
		 */
		public void saveExams(List<List<Module>> modules) throws SQLException
		{
			Statement stmt = null;
			String updateExam;
			String updateRoom;
			System.out.println("saving exams to database..");
			try 
			{
					stmt = con.createStatement();
					//before inserting data we need to clean up old data
					String deleteRoomModule = "DELETE FROM " + dbName + ".Room_Module;";
					stmt.execute(deleteRoomModule);
				for(List<Module> am : modules)
				{	
					for(Module m: am)
					{
						Exam e = m.getExam();
						String t = e.getStartTime().SQLDate();
						updateExam = "UPDATE "+dbName+".Exam"
							+ " SET date = '"+ e.getDay().getsqlDate() + "',"
							+ " time = '"+ t + "'"
							+ " WHERE examID = " + e.getID();
						stmt.execute(updateExam);
											
						updateRoom = "INSERT INTO " +dbName+".Room_Module (roomID, moduleID)"
							+ " VALUES (" + e.getRoom().getID() + ", "+"'"+m.getModuleID()+"'" + 
							");";
					stmt.execute(updateRoom);
					}			
				}
					System.out.println("Exams Saved");
			}
			catch (SQLException e2)
			{
				System.out.println("failed to save exam");
			}
			finally
			{
				if (stmt != null)
				{
					stmt.close();
				}
				if (con != null) 
				{
					con.close();
				}
			}			
		}
		
		//load invigilators
		
		public ArrayList<Invigilator> loadInvigilators() throws SQLException
		{
			ArrayList<Invigilator> dbInvigilators = new ArrayList<Invigilator>();
			Statement stmt = null;
			//wait for correct sql statement
			String query = "SELECT * FROM " + dbName +".Supervisor";
			try
			{
			
				stmt = con.createStatement();
				ResultSet invigilatorQuery = stmt.executeQuery(query);
				while (invigilatorQuery.next())
				{
					dbInvigilators.add(new Invigilator(invigilatorQuery.getInt("supervisorID"), invigilatorQuery.getString("supervisorName")));
				}
			}
			catch (SQLException e)
			{
				System.out.println("failed to load Invigilator data");
			}
			finally
			{
				if (stmt != null)
				{
					stmt.close();
				}
			}
			
			return dbInvigilators;			
		}
		//save invigilators
		
		public void saveInvigilators(ArrayList<Invigilator> invigilators) throws SQLException
		{
			Statement stmt = null;
			String updateInvigilators;
			System.out.println("saving invigilators to database..");
			try 
			{
					stmt = con.createStatement();
					//before inserting data we need to clean up old data
					String deleteRoomModule = "DELETE FROM " + dbName + ".Room_Module;";
					stmt.execute(deleteRoomModule);
					for(Invigilator i: invigilators)
					{
						if(i.getExam() != null)
						{
							int id = i.getiID();
							int eid = i.getExam().getID();
							updateInvigilators = "INSERT INTO "+dbName+".Room_Supervisor (examID, supervisorID)"
									+" VALUES ("+eid + ", "+"'"+id+"'" + ");";
								
							stmt.execute(updateInvigilators);
						
						}						
						System.out.println("Invigilator data Saved");
				}
			}
			catch (SQLException e2)
			{
				System.out.println("failed to save Invigilator data");
			}
			finally
			{
				if (stmt != null)
				{
					stmt.close();
				}
				if (con != null) 
				{
					con.close();
				}
			}			
		}
		
}
