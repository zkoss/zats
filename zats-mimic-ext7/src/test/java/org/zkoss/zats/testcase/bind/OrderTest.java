package org.zkoss.zats.testcase.bind;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.Zats;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.operation.RenderAgent;
import org.zkoss.zats.mimic.operation.SelectAgent;
import org.zkoss.zats.testapp.bind.order.Order;
import org.zkoss.zats.testapp.bind.order.OrderVM;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;

/**
 * test case for bugs from number 500-999
 * @author dennis
 *
 */
public class OrderTest{

	
	@BeforeClass
	public static void init()
	{
		Zats.init("./src/test/resources");
	}

	@AfterClass
	public static void end()
	{
		Zats.end();
	}

	@After
	public void after()
	{
		Zats.cleanup();
	}
	
	
	@Test
	public void testLoad(){
		DesktopAgent desktop = Zats.newClient().connect("/~./bind/order.zul");
		ComponentAgent window = desktop.query("#main");
		ComponentAgent orderList = desktop.query("#main #orderList");
		Assert.assertNotNull(orderList);
		
		//check header
		List<ComponentAgent> headers = orderList.queryAll("listheader");
		Assert.assertEquals(5, headers.size());
		
		//assert header label 
		Assert.assertEquals("Id", headers.get(0).as(Listheader.class).getLabel());
		Assert.assertEquals("Quantity", headers.get(1).as(Listheader.class).getLabel());
		Assert.assertEquals("Price", headers.get(2).as(Listheader.class).getLabel());
		Assert.assertEquals("Creation Date", headers.get(3).as(Listheader.class).getLabel());
		Assert.assertEquals("Shipping Date", headers.get(4).as(Listheader.class).getLabel());
		
		
		ComponentAgent newBtn = window.query("#newBtn");
		ComponentAgent saveBtn = window.query("#saveBtn");
		ComponentAgent deleteBtn1 = window.query("#deleteBtn1");
		
		Assert.assertFalse(newBtn.as(Button.class).isDisabled());
		Assert.assertTrue(saveBtn.as(Button.class).isDisabled());
		Assert.assertTrue(deleteBtn1.as(Button.class).isDisabled());
		
		
		ComponentAgent editor = window.query("#editor");
		Assert.assertFalse(editor.as(Groupbox.class).isVisible());
	}
	
	@Test
	public void testVM(){
		DesktopAgent desktop = Zats.newClient().connect("/~./bind/order.zul");
		ComponentAgent window = desktop.query("#main");
		ComponentAgent orderList = desktop.query("#main #orderList");
		List<ComponentAgent> orderListItems = desktop.queryAll("#main #orderList > listitem");
		Assert.assertNotNull(orderList);
		
		Order selected = null;
		OrderVM vm = orderList.as(OrderVM.class);
		
		Assert.assertNull(vm.getSelected());
		
		orderListItems.get(0).as(SelectAgent.class).select();
		selected = vm.getSelected();
		Assert.assertNotNull(selected);
		
		Assert.assertEquals("00001", selected.getId());
		Assert.assertEquals(selected.getPrice()*selected.getQuantity(), selected.getTotalPrice(),0.1);
		
		
		orderListItems.get(1).as(SelectAgent.class).select();
		selected = vm.getSelected();
		Assert.assertNotNull(selected);
		
		Assert.assertEquals("00002", selected.getId());
		Assert.assertEquals(selected.getPrice()*selected.getQuantity(), selected.getTotalPrice(),0.1);
		
		orderListItems.get(3).as(SelectAgent.class).select();
		selected = vm.getSelected();
		Assert.assertNotNull(selected);
		
		Assert.assertEquals("00004", selected.getId());
		Assert.assertEquals(selected.getPrice()*selected.getQuantity(), selected.getTotalPrice(),0.1);
	}
	
	
	@Test
	public void testNew(){
		DesktopAgent desktop = Zats.newClient().connect("/~./bind/order.zul");
		ComponentAgent window = desktop.query("#main");
		ComponentAgent orderList = window.query("#orderList");
		
		ComponentAgent newBtn = window.query("#newBtn");
		ComponentAgent saveBtn = window.query("#saveBtn");
		ComponentAgent deleteBtn1 = window.query("#deleteBtn1");
		
		ComponentAgent editor = window.query("#editor");
		Assert.assertFalse(editor.as(Groupbox.class).isVisible());
		Assert.assertTrue(saveBtn.as(Button.class).isDisabled());
		Assert.assertTrue(deleteBtn1.as(Button.class).isDisabled());
		
		
		List<ComponentAgent> items = orderList.queryAll("listitem");
		int size = items.size();
		//click new 
		newBtn.click();
		
		Assert.assertTrue(editor.as(Groupbox.class).isVisible());
		Assert.assertFalse(saveBtn.as(Button.class).isDisabled());
		Assert.assertFalse(deleteBtn1.as(Button.class).isDisabled());
		
		//fill data
		editor.query("#desc").type("a test object");
		editor.query("#quantity").type("300");
		editor.query("#price").type("33.33");
		editor.query("#creationDate").type("2012/03/20");
		editor.query("#shippingDate").type("2012/04/20");
		
		//click save
		saveBtn.click();
		
		//render the new item 
		orderList.as(RenderAgent.class).render(size,size);//render last
		
		//check the last item has the new data
		items = orderList.queryAll("listitem");
		
		Assert.assertEquals(size+1, items.size());
		
		ComponentAgent lastItem = items.get(items.size()-1);
		List<ComponentAgent> fields = lastItem.queryAll("listcell");
		Assert.assertEquals(5, lastItem.getChildren().size());
		Assert.assertEquals(5, fields.size());
		Assert.assertEquals("300", fields.get(1).as(Listcell.class).getLabel());
		Assert.assertEquals("33.33", fields.get(2).as(Listcell.class).getLabel());
		Assert.assertEquals("2012/03/20", fields.get(3).as(Listcell.class).getLabel());
		Assert.assertEquals("2012/04/20", fields.get(4).as(Listcell.class).getLabel());
	}
	
	
	@Test
	public void testLoad2(){
		DesktopAgent desktop = Zats.newClient().connect("/~./bind/order2.zul");//the grid
		ComponentAgent window = desktop.query("#main");
		ComponentAgent orderList = desktop.query("#main #orderList");
		Assert.assertNotNull(orderList);
		
		//check header
		List<ComponentAgent> headers = orderList.queryAll("column");
		Assert.assertEquals(5, headers.size());
		
		//assert header label 
		Assert.assertEquals("Id", headers.get(0).as(Column.class).getLabel());
		Assert.assertEquals("Quantity", headers.get(1).as(Column.class).getLabel());
		Assert.assertEquals("Price", headers.get(2).as(Column.class).getLabel());
		Assert.assertEquals("Creation Date", headers.get(3).as(Column.class).getLabel());
		Assert.assertEquals("Shipping Date", headers.get(4).as(Column.class).getLabel());
		
		
		ComponentAgent newBtn = window.query("#newBtn");
		ComponentAgent saveBtn = window.query("#saveBtn");
		
		Assert.assertFalse(newBtn.as(Button.class).isDisabled());
		Assert.assertTrue(saveBtn.as(Button.class).isDisabled());
		
		
		ComponentAgent editor = window.query("#editor");
		Assert.assertFalse(editor.as(Groupbox.class).isVisible());
	}
	
	
	@Test
	public void testNew2(){
		DesktopAgent desktop = Zats.newClient().connect("/~./bind/order2.zul");
		ComponentAgent window = desktop.query("#main");
		ComponentAgent orderList = window.query("#orderList");
		
		ComponentAgent newBtn = window.query("#newBtn");
		ComponentAgent saveBtn = window.query("#saveBtn");
		
		ComponentAgent editor = window.query("#editor");
		Assert.assertFalse(editor.as(Groupbox.class).isVisible());
		Assert.assertTrue(saveBtn.as(Button.class).isDisabled());
		
		
		List<ComponentAgent> items = orderList.queryAll("row");
		int size = items.size();
		//click new 
		newBtn.click();
		
		Assert.assertTrue(editor.as(Groupbox.class).isVisible());
		Assert.assertFalse(saveBtn.as(Button.class).isDisabled());
		
		//fill data
		editor.query("#desc").type("a test object");
		editor.query("#quantity").type("300");
		editor.query("#price").type("33.33");
		editor.query("#creationDate").type("2012/03/20");
		editor.query("#shippingDate").type("2012/04/20");
		
		//click save
		saveBtn.click();
		
		//render the new item 
		orderList.as(RenderAgent.class).render(size,size);//render last
		
		//check the last item has the new data
		items = orderList.queryAll("row");
		
		Assert.assertEquals(size+1, items.size());
		
		ComponentAgent lastItem = items.get(items.size()-1);
		List<ComponentAgent> fields = lastItem.queryAll("label");
		Assert.assertEquals(5, lastItem.getChildren().size());
		Assert.assertEquals(5, fields.size());
		Assert.assertEquals("300", fields.get(1).as(Label.class).getValue());
		Assert.assertEquals("33.33", fields.get(2).as(Label.class).getValue());
		Assert.assertEquals("2012/03/20", fields.get(3).as(Label.class).getValue());
		Assert.assertEquals("2012/04/20", fields.get(4).as(Label.class).getValue());
	}
	
}
