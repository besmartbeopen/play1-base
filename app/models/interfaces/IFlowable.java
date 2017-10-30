package models.interfaces;

import models.Task;

/**
 * @author marco
 *
 */
public interface IFlowable {

	// getFlowState();

	Task getTask();
	void setTask(Task task);
}
