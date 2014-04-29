

public class Student {
	private String name;
	private String studentID;

	public Student(String name, String studentID) {
		this.name = name;
		this.studentID = studentID;
	}
	
	public String getName(){
		return name;
	}
	
	public String getSiD() {
		return studentID;
	}
	
	@Override 
	public boolean equals(Object o) { 
		if (o == this)
			return true;
		if (!(o instanceof Student))
			return false;
		Student s = (Student )o;

		return s.name == name
				&& s.studentID  == studentID;
	}
	
	public String toString() {
		return name + " " + studentID;
	}

}
