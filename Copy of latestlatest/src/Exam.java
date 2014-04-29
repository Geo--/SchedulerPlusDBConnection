
import java.util.*;

public class Exam implements Comparable<Exam> {
	private Day day;
	private Hour startTime;
	private Hour endTime;
	private Room room;
	private Boolean scheduled;
	private int duration;
	private RoomType type;
	private List<Hour> examPeriod = new ArrayList<Hour>();
	private int examID;
	
 Exam(int id, int duration) {
		//When creating an exam all fields should be empty
		//if should have no state, then we should populate
		//its state once it has been assigned to a slot in the timetable.
		//thus to begin, the time table is not scheduled.
	 	scheduled = false;
	 	//duration should be measurred in whole hours. eg. 1, 2 or 3.
	 	this.duration = duration;
	 	examID = id;
	}
 
 public int getID() {
	 return examID;
 }
 
 Exam(int id, int duration, RoomType type) {
	 	scheduled = false;
	 	this.duration = duration;
	 	this.type = type;
	 	examID = id;
	}
 
 public List<Hour> getExamPeriod() {
	 return examPeriod;
 }
 
 public RoomType getRoomType() {
	 return type;
 }
 
 public Day getDay() {
	 return day;
 }
 
 public Hour getStartTime() {
	 return startTime;
 }
 
 public Hour getEndTime() {
	 return endTime;
 }
 
 public Room getRoom() {
	 return room;
 }

 public Boolean getScheduled() {
	 return scheduled;
 }
 
 public int getDuration() {
	 return duration;
 }
 
 public void setDay(Day day) {
	 this.day = day;
 }
 
 public void setStartTime(Hour startTime) {
	 this.startTime = startTime;
 }
 
 public void setEndTime(Hour endTime) {
	 this.endTime = endTime;
 }
 
 public void setRoom(Room room) {
	 this.room = room;
 }
 
 public void setScheduledTrue() {
	 scheduled = true;
 }
 
 public void setScheduledFalse() {
	 scheduled = false;
 }
	
 public String toString() {
	 return day.getDateString() + " " + startTime + " " +  endTime + " - " + getRoom().toString();
 }
 
@Override
 public int compareTo(Exam e) {
     //Compares RoomType

	if (type != null && e.type == null) {
		return -999999999;
	}
	if (e.type != null && type == null) {
		return 999999999;
	}
	 int dur = e.duration - duration;
     if (dur !=0) return dur;
     else return 0;
 }
/*
public boolean equals(Object other) {
	if (this == other) return true;
	if(!(other instanceof Exam)) return false;
	Exam e = (Exam) other;
	
	return this.duration == e.duration & this.type == e.type;
}
*/
}
