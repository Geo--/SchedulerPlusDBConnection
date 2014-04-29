

public class Invigilator 
{

	private int iID;
	private String name;
	private Exam exam;
	
	public Invigilator(int int1, String string) 
	{
		iID = int1;
		name = string;
		exam = null;
	}
	
	public void setExam(Exam e)
	{
		exam = e;
	}
	
	public Exam getExam()
	{
		return exam;
	}

	public int getiID() {
		return iID;
	}

	

	public String getName() {
		return name;
	}

	public String toString()
	{
		return getName() + getiID();
	}
}
