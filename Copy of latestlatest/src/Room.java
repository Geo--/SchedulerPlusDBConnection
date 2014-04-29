
import java.util.*;

public class Room implements Comparable <Room>{
	private String roomNumber;
	private RoomType type;
	private int capacity;
	//each room has it's own array of days
	//to act as a timetable for each room.
	private List<Day> timetable = new ArrayList<Day>();
	private Boolean blackMarked;
	private int roomID;
	
	public Room(int roomID, String roomNumber, RoomType type, int capacity) {
		this.roomNumber = roomNumber;
		this.type = type;
		this.capacity = capacity;
		blackMarked = false;
		this.roomID = roomID;
	}
	
	public int getID() {
		return roomID;
	}
	
	public void blackMark() {
		blackMarked = true;
	}
	
	public void unMark() {
		blackMarked = false;
	}
	
	public Boolean getMark() {
		return blackMarked;
	}
	
	public String toString() {
		return roomNumber;
	}
	
	public void setTimetable(int examDuration, Date startDate) {
		for(int i = 0; i < examDuration; i++) {
			timetable.add(new Day(startDate));
			//increment the date.
			Calendar c = Calendar.getInstance();
			c.setTime(startDate);
			int day = c.get(Calendar.DATE);
			day++;
			c.set(Calendar.DATE, day);
			startDate = c.getTime();
		}
	}
	
	public List<Day> getTimetable() {
		return timetable;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public void setCapacity(int i) {
		capacity = i;
	}
	
	public RoomType getType() {
		return type;
	}
	
	public String getRoomNumber() {
		return roomNumber;
	}
	
	//check to see if this room has space in its timetable for this exam.
	public Boolean hasSpace(Exam exam) {
		for(Day d: timetable) {
			for(Hour h: d.getHours()) {
				if(h.getAvailable() == true && h.getHour() + exam.getDuration() <= d.getFinishTime()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public Boolean isAvailableAtTime(int duration, Hour hour, Day day) {
		int track = 0;
		for(Day d: timetable) {
			if(d.equals(day)) {
			for(Hour h : d.getHours()) {
				int startTime = hour.getHour();
				for (int i = 0 ;i < duration ;i++) {
				if(h.equals(d.getHour(startTime)) && h.getAvailable()==true) {
					track ++;
					}
				startTime++;
					}
				}
			}
		}
		if(track == duration)return true;
		else return false;
	}
	
	@Override 
	public boolean equals(Object o) { 
		if (o == this)
			return true;
		if (!(o instanceof Room))
			return false;
		Room r = (Room)o;

		return r.roomNumber == roomNumber
				&& r.type  == type
					&& r.capacity == capacity;
	}
	
	@Override
	 public int compareTo(Room r) {
	     int cap = capacity - r.capacity;
	     if (cap !=0) return cap;
	     else return 0;
	 }

}
