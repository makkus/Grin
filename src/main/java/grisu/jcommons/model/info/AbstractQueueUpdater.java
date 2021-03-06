package grisu.jcommons.model.info;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

abstract public class AbstractQueueUpdater implements QueueUpdater {

	private class UpdateQueueTimerTask extends TimerTask {

		public UpdateQueueTimerTask() {
		}

		@Override
		public void run() {
			updateQueues();
		}
	}

	public static Logger myLogger = LoggerFactory.getLogger(AbstractQueueUpdater.class);

	private final Set<Queue> queues = Sets.newHashSet();

	private final Timer timer = new Timer();
	private TimerTask updateTask;

	private int delay_in_seconds = 120;

	// public AbstractQueueUpdater() {
	//
	// startInfoUpdate(delay_in_seconds);
	// }

	/* (non-Javadoc)
	 * @see grisu.jcommons.model.info.QueueUpdater#addQueue(grisu.jcommons.model.info.Queue)
	 */
	public void addQueue(Queue q) {
		this.queues.add(q);
	}

	/* (non-Javadoc)
	 * @see grisu.jcommons.model.info.QueueUpdater#removeQueue(grisu.jcommons.model.info.Queue)
	 */
	public void removeQueue(Queue q) {
		this.queues.remove(q);
	}

	/* (non-Javadoc)
	 * @see grisu.jcommons.model.info.QueueUpdater#startInfoUpdate(int)
	 */
	public void startInfoUpdate(int delay_in_seconds) {
		this.delay_in_seconds = delay_in_seconds;
		if (updateTask != null) {
			updateTask.cancel();
		}

		updateTask = new UpdateQueueTimerTask();
		timer.scheduleAtFixedRate(updateTask, 1, delay_in_seconds * 1000);
	}

	/* (non-Javadoc)
	 * @see grisu.jcommons.model.info.QueueUpdater#stopInfoUpdate()
	 */
	public void stopInfoUpdate() {
		if (updateTask != null) {
			updateTask.cancel();
			updateTask = null;
		}
	}

	abstract public void updateQueue(Queue q);

	private synchronized void updateQueues() {

		for (Queue q : queues) {
			updateQueue(q);
		}

	}

}
