

public class Hour {
	private int time;
	private Boolean available;


	public Hour(int time) {
		this.time = time;
		available = true;
	}
	
	public int getHour() {
		return time;
	}
	
	public Boolean getAvailable() {
		return available;
	}
	
	public void setHour(int time) {
		this.time = time;
	}
	
	public void setAvailableTrue() {
		available = true;
	}
	
	public void setAvailableFalse() {
		available = false;
	}
	
	public String toString() {
		String period = "am";
		String morning = "0";
		if(time > 12)
			period = "pm";
		if(time >= 10)
			morning = "";
		return morning + time + ":00" + " " + period;
	}
	
	public boolean equals(Object o) { 
		if (o == this)
			return true;
		if (!(o instanceof Hour))
			return false;
		Hour r = (Hour)o;

		return r.time == time;
	}
	
	public String SQLDate()
	{
		String period = "am";
		String morning = "0";
		if(time > 12)
			period = "pm";
		if(time >= 10)
			morning = "";
		return morning + time + ":00:00";
	}
	
}
