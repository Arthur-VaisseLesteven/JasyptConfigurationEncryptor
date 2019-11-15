package com.avale.model;

/**
 * Entity whose change are inventoried some way, and thus can be reverted.
 */
public interface RevertableChanges {
	/**
	 * Revert the last performed change, if any.
	 */
	void undoLastChange();

	/**
	 * Reapply the last reverted change if, and only if, there were no change since the last revert.
	 */
	void redoLastRevertedChange();
}
