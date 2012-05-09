/* GenericSortAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		2012/5/7 Created by Hawk

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.operation;

import java.lang.reflect.Method;
import java.util.Map;

import org.zkoss.zats.ZatsException;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.au.EventDataManager;
import org.zkoss.zats.mimic.operation.SortAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SortEvent;
import org.zkoss.zul.Column;

/**
 * Sorting operation for Listheader, Column, and Treecol.
 * 
 * @author Hawk
 *
 */
public class GenericSortAgentBuilder implements OperationAgentBuilder<ComponentAgent,SortAgent> {
	
	public static final String ASCENDING = "ascending";
	public static final String DESCENDING = "descending";
	
	public SortAgent getOperation(ComponentAgent target) {
		return new SortAgentImpl(target);
	}
	
	public Class<SortAgent> getOperationClass() {
		return SortAgent.class;
	}
	
	private class SortAgentImpl extends AgentDelegator<ComponentAgent> implements SortAgent {
		
		public SortAgentImpl(ComponentAgent target) {
			super(target);
		}

		/**
		 * Send AU data based on the component's current sorting direction.
		 * For Column, it always send AU in spite of current sorted order.
		 * For Treecol & Listheader, it only sends AU when desired order is different from current sorted order. 
		 * Their sorted order's switching is pre-defined, natural --> ascending, ascending <--> descending.
		 * Please refer Listheader.onSort().
		 */
		public void sort(boolean ascending) {
			String desktopId = target.getDesktop().getId();
			String cmd = Events.ON_SORT;
			Component header= ((Component)target.getDelegatee());
			
			Map<String, Object> data = null;
			//When desired sorting direction equals Column's current direction, it still send AU.
			if (header instanceof Column){
				data = EventDataManager.build(new SortEvent(cmd, header, ascending));
				((ClientCtrl)target.getClient()).postUpdate(desktopId, cmd, header.getUuid(), data, null);
			}else{
				try{
					//When desired sorting direction equals component's current direction, do *not* send AU.
					Method getSortDirection = header.getClass().getMethod("getSortDirection");
					String currentDirection = getSortDirection.invoke(header).toString();
					
					//if header is sorted, switch sorting direction between ascending and descending
					if (currentDirection.equals(ASCENDING)){
						if (!ascending){
							data = EventDataManager.build(new SortEvent(cmd, header, false));
							((ClientCtrl)target.getClient()).postUpdate(desktopId, cmd, header.getUuid(), data, null);
						}
					}else if (currentDirection.equals(DESCENDING)){
						if (ascending){
							data = EventDataManager.build(new SortEvent(cmd, header, true));
							((ClientCtrl)target.getClient()).postUpdate(desktopId, cmd, header.getUuid(), data, null);
						}
						return;
					}else { //natural, not sorted yet
						if(ascending){
							data = EventDataManager.build(new SortEvent(cmd, header, ascending));
							((ClientCtrl)target.getClient()).postUpdate(desktopId, cmd, header.getUuid(), data, null);
						}else{
							data = EventDataManager.build(new SortEvent(cmd, header, true));
							((ClientCtrl)target.getClient()).postUpdate(desktopId, cmd, header.getUuid(), data, null);
							data = EventDataManager.build(new SortEvent(cmd, header, ascending));
							((ClientCtrl)target.getClient()).postUpdate(desktopId, cmd, header.getUuid(), data, null);
						}
					}
				}catch(Exception e){
					throw new ZatsException("", e);
				}
				
			}
		}
	}

}
