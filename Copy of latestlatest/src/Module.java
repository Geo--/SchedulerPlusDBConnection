
import java.util.*;

public class Module implements Comparable<Module> {
	private String moduleID;
	private String subject;
	private Exam exam;
	private List<Student> studentsEnrolled = new ArrayList<Student>();
	private Boolean blackMark;
	
	public Module(String moduleID, String subject, Exam exam, List<Student> studentsEnrolled) {
		this.moduleID = moduleID;
		this.subject = subject;
		this.exam = exam;
		this.studentsEnrolled = studentsEnrolled;
		blackMark = false;
	}
	
	public Boolean getBlackMark() {
		return blackMark;
	}
	
	public void setBlackMark() {
		blackMark = true;
	}
	
	public void unMark() {
		blackMark = false;
	}
	
	//check to see if the module has an exam at the specified time
	public Boolean hasExam(Day day, Hour hour) {
		if(exam.getScheduled() == false)
			return false;
		else {
			if(exam.getDay().getDate().equals(day.getDate()) && exam.getExamPeriod().contains(hour))
				return true;
		}
		return false;
	}

	public Exam getExam() {
		return exam;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public String getModuleID() {
		return moduleID;
	}
	
	public List<Student> getStudentsEnrolled() {
		return studentsEnrolled;
	}
	
	public String toString() {
		return moduleID + " " + subject;
	}
	
	@Override
	public int compareTo(Module m) {
		     //Compares Exam
		     int eno = exam.compareTo(m.exam);
		     if (eno != 0) return eno;
		     else return 0;
	}
	
	public boolean equals(Object o) { 
		if (o == this)
			return true;
		if (!(o instanceof Room))
			return false;
		Module r = (Module)o;

		return r.moduleID == moduleID;
	}
}
