
package org.zkoss.zk.zats.example.composer;

import java.util.List;

import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zk.zats.example.service.Task;
import org.zkoss.zk.zats.example.service.TaskService;
import org.zkoss.zul.Listbox;

/**
 *
 */
@SuppressWarnings("serial")
public class TodoComposer extends GenericForwardComposer {
	private TaskService taskdao = new TaskService(); 
	private Task selected = new Task();
	private List<Task> tasks;
	
	private Listbox listbox;
	
	public Task getSelected() {
		return selected;
	}
	

	
	public List<Task> getAllTasks() {
		//fetch all tasks from database 
		tasks = taskdao.findAll();
		
		return tasks;
	}

	public void onSelect$listbox(){
		selected = tasks.get(listbox.getSelectedIndex());
	}
	
	public void onClick$add(){	
		//insert into database
			validate(); 
			taskdao.insert(selected);
			selected = new Task();
	}

	public void onClick$update(){
		//update database
		selected = new Task();
	} 
	
	public void onClick$delete(){
		if (selected != null) {
			taskdao.delete(selected);
			selected = null;
		}
	}
	
	public void onClick$reset(){
		selected = new Task();
	}
	void validate(){
	}
}
