package grisu.jcommons.model.info;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.google.common.collect.Sets;

public class DefaultQueueUpdater implements QueueUpdater {

	private class UpdateQueueTimerTask extends TimerTask {

		public UpdateQueueTimerTask() {
		}

		@Override
		public void run() {
			updateQueues();
		}
	}

	private final Set<Queue> queues = Sets.newHashSet();

	private final Timer timer = new Timer();
	private TimerTask updateTask;

	private int delay_in_seconds = 120;

	public DefaultQueueUpdater() {

		startInfoUpdate(delay_in_seconds);
	}

	public void addQueue(Queue q) {
		this.queues.add(q);
	}

	public void removeQueue(Queue q) {
		this.queues.remove(q);
	}

	public void startInfoUpdate(int delay_in_seconds) {
		this.delay_in_seconds = delay_in_seconds;
		if (updateTask != null) {
			updateTask.cancel();
		}

		updateTask = new UpdateQueueTimerTask();
		timer.scheduleAtFixedRate(updateTask, 1, delay_in_seconds);
	}

	public void stopInfoUpdate() {
		if (updateTask != null) {
			updateTask.cancel();
			updateTask = null;
		}
	}

	private void updateQueue(Queue q) {
		System.out.println("Updating queue: " + q.getName());
	}

	private synchronized void updateQueues() {

		for (Queue q : queues) {
			updateQueue(q);
		}

	}

}
