package models.common.interfaces;

import models.common.Task;

/**
 * @author marco
 *
 */
public interface IFlowable {

	// getFlowState();

	Task getTask();
	void setTask(Task task);
}
