package grisu.jcommons.model.info;

public class DefaultQueueUpdater extends AbstractQueueUpdater {

	@Override
	public void updateQueue(Queue q) {
		myLogger.debug("Updating dynamic properties of queue: " + q.getName());
	}
}
