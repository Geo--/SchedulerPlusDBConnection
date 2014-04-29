
import java.util.*;

public class Scheduler {
	private List<Room> allRooms = new ArrayList<Room>();
	private List<List<Module>> allModules = new ArrayList<List<Module>>();
	private int timeHours;
	
	
	//The scheduler needs the information for all the modules of the exams it will be scheduling,
	//as well as all the rooms, which can be used to schedule the exams in.
	public Scheduler(List<Room> allRooms) {
		this.allRooms = allRooms;
		for(Room room: allRooms) {
			for(Day day: room.getTimetable()) {
				timeHours += day.getFinishTime() - day.getStartTime();
			}
		}
	}
	
	public void schedule(List<List<Module>> allModules) {

		this.allModules = allModules;
		List<Module> modules = allModules.get(0);
		if(canBeBooked(modules)) {
			scheduleList(modules);
		}
	}
	
	public int getTimeHours() {
		return timeHours;
	}
	
	//The hard conditions of the room type/capacity and availability of the hours are seemingly working.
	//To further implement the Scheduler we must now check whether the students hard condition.  That is,
	//A student can not be in the same place at the same time.  The information of what a student is studying
	//is saved within the Module instance in the Students Enrolled list.  Therefore we will need to find/ develop
	//methods which will check whether a student is already sitting an exam at the time we propose to schedule THIS
	//exam.  (Hopefully this makes some sense...)  I've marked where I believe will be a good place to do the check
	//in the book() method code.  You may find a better alternative.
	
	//Backtrack Algorithm.
	//This method is called externally from another class, passing in the first exam you wish to schedule
	//the exam which you pass in, should exist in the Module list belonging to the instance of this scheduler.
	private Boolean scheduleList(List<Module> modules) {
		Exam exam = modules.get(0).getExam();
		//the book exam method (see below) will book the exam in the first available slot.
		book(exam, modules);
		bookRemaindingExamsInList(modules, exam.getDay(), exam.getStartTime());
		//if all the exams are booked in the allModules list then we have solved the scheduling problem.
		if(allListsBooked(allModules)) return true;
		//Find the nextExam which needs scheduling.  There should be time available in the timetable to book the exam
		//the exam should not already be scheduled, and there should be at least one room which has capacity for
		//this exam.  If these conditions are satisfied, call the scheduling method again using the nextExam.
		//The process is recursive.
		for(List<Module> nextModules: allModules) {
			if(allExamsBooked(nextModules) == false && canBeBooked(nextModules)) {
				if(scheduleList(nextModules)) {
					return true;
					}
			}
		}
		//We will arrive at this point if after trying to book all exams, all the needs can not be satisfied.
		//We will remove the data from exam, returning false to the recursive call, which in turn will back track
		//removing data and rescheduling the exams until all the requirements are satisfied.
		for(Module module: modules) {
			module.unMark();
			module.getExam().getRoom().unMark();
			timeHours += module.getExam().getDuration();
		Iterator<Hour> hours = module.getExam().getExamPeriod().iterator();
		while(hours.hasNext()) {
		Hour h = hours.next();
		h.setAvailableTrue();
		hours.remove();
		}
		module.getExam().setScheduledFalse();
		module.getExam().setDay(null);
		module.getExam().setStartTime(null);
		module.getExam().setEndTime(null);
		}
		return false;
		}
	
	//Check to see if all exams are booked(within a clashing list)
	private Boolean allExamsBooked(List<Module> allModules) {
		int i = 0;
		for(Module m: allModules) {
			if(m.getExam().getScheduled() == true) {
				i++;
			}
		}
		if(i == allModules.size()) {
			return true;
		}
		else return false;
	}
	//check to see if all lists of listed modules are booked.
	private Boolean allListsBooked(List<List<Module>> allLists) {
		int i = 0;
		for(List<Module> modules : allLists) {
			if(allExamsBooked(modules))
				i++;
		}
		if(i == allLists.size())
			return true;
		return false;
	}
	
	
	//Book the exam.  Each exam must pass all conditions to be booked.
	private Boolean book(Exam exam, List<Module> clashingModules) {
		//Firstly check the rooms, the room must be of the same type as needed by the exam, or the exam type 
		//must be null.
		 for(Room room : allRooms) {
			 if(exam.getRoomType() == null || exam.getRoomType() == room.getType()) {
				 //the room must have the capacity to fit the size of the exam cohort.
				 if(room.getCapacity() >= examSize(exam)) {
					 //the room specifics are now satisfied.
					 
					 //We must now check to see whether the suitable room has time available in it's
					 //timetable to fit this exam in.
				 for(Day day : room.getTimetable()) {
					 //each room has a list of days...
					 for(Hour start : day.getHours()) {
						 //which in turn has a list of hours...
						 if(start.getAvailable() == true) {
							 //if the hour is available, then the start time plus the duration must fit 
							 //in the remaining time available of that day.
							 if(start.getHour() + exam.getDuration() <= day.getFinishTime()) {
							 int startInt = start.getHour();
							//Check to see if any students of this exam are already sitting
							//an exam at this time, if the result is false the time will be changed.
							 if(checkForOtherStudents(exam, startInt, day)) {
							 //add each hour of the exam which is being booked, to the list
							 //in the exams instance.
								 room.blackMark();
								 for(Module m: clashingModules) {
									 Exam e = m.getExam();
									 if(e.equals(exam))
										 m.setBlackMark();
								 	}
							if (allExamsCanBeBooked(clashingModules, startInt, day)) {
								for(int i = 0 ; i < exam.getDuration() ; i++) {
									Hour h = day.getHour(startInt);
									h.setAvailableFalse();
									exam.getExamPeriod().add(h);
									startInt++;
										}
										//Set the other data concerning the exam time.
										//including  the start and end time, the day which it occurs
										//the room which it is located and that it has now been scheduled.
										//decrement the toalTime.
										int endHour = start.getHour() + exam.getDuration();
										exam.setStartTime(start);
										exam.setEndTime(day.getHour(endHour));
										exam.setScheduledTrue();
										exam.setDay(day);
										exam.setRoom(room);
										timeHours -= exam.getDuration();
										return true;
										}
									}
								}
							}
						}
					}
				}
			}
		}
	 //if all the conditions can't be met we return false.
	 return false;
	 }
	
	private Module getModule(Exam exam) {
		for(List<Module> modules : allModules) {
			for(Module m: modules) {
				if (m.getExam().equals(exam))
					return m;
					}
			}
		//should never return null.
		return null;
	}

	//Returns the number of students sitting an exam.
	private int examSize(Exam exam) {
		Module mod = getModule(exam);
				return mod.getStudentsEnrolled().size();
	}
	
	//This method takes the exam, the start time and day
	//firstly it checks for any other exams at this time
	//and adds the students of those exams into a clashing students
	//collection.  It then checks to see if any of those students are in 
	//this exam.  If they are, the method will fail.
	private Boolean checkForOtherStudents(Exam exam, int startTime, Day day) {
		Hour h = day.getHour(startTime);
		List <Student> clashing = new ArrayList<Student>();
		for(List<Module> modules : allModules) {
		for(Module m: modules) {
			if (m.hasExam(day, h)) {
				clashing.addAll(m.getStudentsEnrolled());
			}
		}
		}
		for(Student student : getModule(exam).getStudentsEnrolled()) {
			if(clashing.contains(student)) {
				return false;
			}
		}
		return true;
	}
	 private Boolean allExamsCanBeBooked(List<Module>nextModules, int startInt, Day day){
		 int hours = 0;
		 Hour hour = day.getHour(startInt);
		 for(Module module : nextModules) {
			 if(module.getBlackMark() == false) {
				 for(Room room : allRooms) {
					 if(room.getMark() == false) {
						 if(room.getType() == module.getExam().getRoomType() || module.getExam().getRoomType() == null) {
							 if(room.getCapacity() >= module.getStudentsEnrolled().size()) {
								 for(Day d: room.getTimetable()) {
									 if(day.getDate().equals(d.getDate())) {
										 if(hour.getHour() + module.getExam().getDuration() <= day.getFinishTime()) {
											 for(int i = startInt; i < startInt + module.getExam().getDuration(); i++) {
												 Hour h = d.getHour(startInt);
												 if(h.getAvailable() == true) {
													 hours++;
													 if(hours == module.getExam().getDuration()) {
														 room.blackMark();
														 module.setBlackMark();
														 break;
											 	}
											 }
										 }
									 }
								 }
							 }
						 }
					 }
				 }
			 }
		 }
		 }
		 for(Room rooms : allRooms) {
			 rooms.unMark();
		 }
		 if(allModulesMarked(nextModules)) {
			 for(Module module : nextModules) {
				 module.unMark();
			 }
			 return true;
		 }
		 
		 else {
			 for(Module module : nextModules) 
				 module.unMark();
				 return false;
		 	}
		 }
	 
	 private Boolean allModulesMarked(List<Module> nextModules) {
		 int track = 0;
		 for(Module m : nextModules) {
			 if(m.getBlackMark() == true)
				 track++;
		 }
		 if(track == nextModules.size())
			 return true;
		 else return false;
	 }
	 
	 private void bookRemaindingExamsInList(List<Module> modules, Day day, Hour hour) {
		 for(Module module : modules) {
			 Exam exam = module.getExam();
			 if (exam.getScheduled() == false) {
				 for(Room room : allRooms) {
					 if(room.getType() == exam.getRoomType() 
							 || exam.getRoomType() == null) {
						 if(room.getCapacity() >= examSize(exam)) {
							 for(Day d : room.getTimetable()) {
								 if(d.getDate().equals(day.getDate())) {
									 for(Hour h : d.getHours()) {
										 if(h.equals(hour)) {
											 if (h.getAvailable() == true) {
												 if(h.getHour() + exam.getDuration() <= d.getFinishTime()) {
													 int startInt = h.getHour();
													 if(checkForOtherStudents(exam, startInt, day)) {
														 int hours = h.getHour();
														 for(int i = 0 ; i < exam.getDuration() ; i++) {
																Hour start = d.getHour(hours);
																start.setAvailableFalse();
																exam.getExamPeriod().add(start);
																hours++;
																	}
																	int endHour = h.getHour() + exam.getDuration();
																	exam.setStartTime(h);
																	exam.setEndTime(d.getHour(endHour));
																	exam.setScheduledTrue();
																	exam.setDay(d);
																	exam.setRoom(room);
																	timeHours -= exam.getDuration();
													 				}
													 	}
													 }
												 }
											 }
										 }
									 }
								 }
							 }
						 }
					 }
				 }
			 }

	private Boolean canBeBooked(List<Module> modules) {
		Exam exam = modules.get(0).getExam();
		for (Room room : allRooms) {
			if (room.getMark() == false) {
				if (exam.getRoomType() == null
						|| exam.getRoomType() == room.getType()) {
					if (room.getCapacity() >= examSize(exam)) {
						for (Day day : room.getTimetable()) {
							for (Hour start : day.getHours()) {
								if (start.getAvailable() == true) {
									if (start.getHour() + exam.getDuration() <= day
											.getFinishTime()) {
										int startInt = start.getHour();
										if (checkForOtherStudents(exam,
												startInt, day)) {
											room.blackMark();
											 for(Module m: modules) {
												 Exam e = m.getExam();
												 if(e.equals(exam))
													 m.setBlackMark();
											 	}
											if (allExamsCanBeBooked(modules,
													startInt, day))
												return true;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

}
