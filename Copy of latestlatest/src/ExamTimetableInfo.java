
import java.util.*;

public class ExamTimetableInfo {
	private int examDuration;
	private Date startDate;

	
	public ExamTimetableInfo(int examDuration, Date startDate) {
		this.examDuration = examDuration;
		this.startDate = startDate;
	}
	
	public int getExamDuration() {
		return examDuration;
	}
	
	public Date getStartDate() {
		return startDate;
	}

}
