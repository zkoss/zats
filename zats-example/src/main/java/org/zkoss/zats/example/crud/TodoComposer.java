
package org.zkoss.zats.example.crud;

import java.util.List;
import java.util.UUID;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;

/**
 *
 */
@SuppressWarnings("serial")
public class TodoComposer extends SelectorComposer {
	private TaskService taskdao = new TaskService(); 
	private Task selected = new Task();
	private List<Task> tasks;
	
	@Wire
	private Listbox listbox;
	@Wire
	private Textbox itemBox;
	@Wire
	private Intbox priorityBox;
	@Wire
	private Datebox dateBox;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		listbox.setItemRenderer(new TaskRenderer());
		refreshInputField();
		reloadListbox();
		
	}
	public Task getSelected() {
		return selected;
	}
	

	
	public List<Task> getAllTasks() {
		//fetch all tasks from database 
		tasks = taskdao.findAll();
		
		return tasks;
	}

	@Listen("onSelect = listbox")
	public void onSelect(SelectEvent event){
		
		selected = (Task)event.getSelectedObjects().iterator().next();
		refreshInputField();
	}
	
	@Listen("onClick = button#add")
	public void add(){	
		//insert into database
		taskdao.insert(new Task(UUID.randomUUID().toString(), itemBox.getValue(), priorityBox.getValue(), dateBox.getValue()));
		refreshInputField();
		reloadListbox();
	}

	@Listen("onClick = button#update")
	public void update(){
		//update database
		selected.setName(itemBox.getValue());
		selected.setPriority(priorityBox.getValue());
		selected.setDate(dateBox.getValue());
		selected = new Task();
		refreshInputField();
		reloadListbox();
	} 
	
	@Listen("onClick = button#delete")
	public void delete(){
		if (selected.getId() != null) {
//			tasks.get(listbox.getSelectedIndex())
			taskdao.delete(selected);
			selected = new Task();
			refreshInputField();
			reloadListbox();
		}
	}
	
	@Listen("onClick = button#reset")
	public void reset(){
		selected = new Task();
		refreshInputField();
	}
	
	private void refreshInputField(){
		itemBox.setValue(selected.getName());
		priorityBox.setValue(selected.getPriority());
		dateBox.setValue(selected.getDate());
	}
	
	private void reloadListbox(){
		listbox.setModel(new ListModelList<Task>(taskdao.findAll()));
	}
	

}
