package org.zkoss.zats.core.component.operation;


public interface OperationHandler
{
	// void setOutput(Queue<JSON> commandQueue); 
	<T extends Operation> T as(Class<T> c);
}
