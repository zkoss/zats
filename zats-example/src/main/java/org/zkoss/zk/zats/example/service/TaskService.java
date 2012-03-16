package org.zkoss.zk.zats.example.service;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class TaskService {

	static private List<Task> taskList = new LinkedList<Task>();
	
	public List findAll(){
		return taskList;
	}
	
	public boolean delete(Task task){

		Iterator<Task> taskIt = taskList.iterator();
		while (taskIt.hasNext()){
			Task t = taskIt.next();
			if (t == task){
				taskIt.remove();
			}
		}
		
		return true;
	}
	
	public boolean insert(Task task){
		taskList.add(task);
		return true;
	}
	
	public void update(Task task){
		
	}

}
