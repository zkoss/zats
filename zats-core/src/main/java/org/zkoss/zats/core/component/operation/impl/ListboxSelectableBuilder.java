package org.zkoss.zats.core.component.operation.impl;

import java.util.HashMap;
import java.util.Map;
import org.zkoss.zats.core.component.ComponentNode;
import org.zkoss.zats.core.component.operation.Selectable;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Listbox;

public class ListboxSelectableBuilder implements OperationBuilder<Selectable>
{
	public Selectable getOperation(final ComponentNode target)
	{
		return new Selectable()
		{
			public Selectable select(int index)
			{
				Map<String, Object> data = new HashMap<String, Object>();
				
				String uuid = target.cast(Listbox.class).getItemAtIndex(index).getUuid();
				String[] selections = {uuid};
				//items: array of UUID of selected items
				//it contains more than 1 item in multiple selections
				data.put("items",selections);
				//reference: UUID of last selected item
				data.put("reference",uuid);
				
				//do not know the following is needed or not
//				data.put("which", 1); 
//				data.put("pageX", 0);
//				data.put("pageY", 0);
//				data.put("x", 0);
//				data.put("y", 0);
				
				OperationManager.post(target, Events.ON_SELECT, data);
				return this;
			}
		};
	}
}
