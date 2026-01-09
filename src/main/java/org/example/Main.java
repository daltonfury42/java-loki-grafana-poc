package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import java.util.List;
import java.util.Random;

public class Main {
	private static final Logger logger = LogManager.getLogger(Main.class);
	private static final Random random = new Random();

	public static void main(String[] args) throws InterruptedException {
		int numThreads = 5 + random.nextInt(6); // 5-10 threads
		Thread[] threads = new Thread[numThreads];

		for (int t = 0; t < numThreads; t++) {
			threads[t] = new Thread(() -> {
				ThreadContext.put("sinkId", String.valueOf(random.nextInt(100000)));
				while (true) {
					ThreadContext.put("runId", String.valueOf(random.nextInt(100000)));
					if (random.nextDouble() < 0.8) {
						String dataId = "data-" + (1000 + random.nextInt(9000));
						logger.info("Reading data for id: {}", dataId);
						try {
							String data = fakeReadData(dataId);
							logger.debug("Successfully read data: {}", data);
							if (random.nextDouble() < 0.3) {
								logger.warn("Data for id {} looks suspicious: {}", dataId, data);
							}
						} catch (Exception e) {
							logger.error("Failed to read data for id {}", dataId, e);
						}
					} else {
						logger.info("Skipping data read for this iteration");
					}
					if (random.nextDouble() < 0.2) {
						logger.info("Performing additional processing step");
						try {
							fakeProcess();
							logger.info("Processing completed successfully");
						} catch (Exception e) {
							logger.error("Processing failed", e);
						}
					}
					try {
						Thread.sleep(500 + random.nextInt(1200)); // 0.5-1.7s
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						return;
					}
				}
			});
			threads[t].start();
		}
		for (Thread thread : threads) {
			thread.join();
		}
	}

	private static void setThreadContext(int i) {
		if (i % 5 == 0) {
			ThreadContext.clearAll();
		}
	}

	private static void deepenStack(int depth, Runnable thrower) {
		if (depth <= 0) {
			thrower.run();
		} else {
			deepenStack(depth - 1, thrower);
		}
	}

	private static String fakeReadData(String dataId) throws Exception {
		if (random.nextDouble() < 0.15) {
			final Exception[] ex = new Exception[1];
			int stackDepth = 20 + random.nextInt(11); // 20-30 frames
			deepenStack(stackDepth, () -> {
				ex[0] = new Exception("Simulated data read failure for " + dataId);
			});
			throw ex[0];
		}
		return "{\"id\":\"" + dataId + "\",\"value\":" + (random.nextInt(1000)) + "}";
	}

	private static void fakeProcess() throws Exception {
		if (random.nextDouble() < 0.1) {
			final Exception[] ex = new Exception[1];
			int stackDepth = 20 + random.nextInt(11); // 20-30 frames
			deepenStack(stackDepth, () -> {
				ex[0] = new Exception("Simulated processing error");
			});
			throw ex[0];
		}
		// Simulate processing time
		Thread.sleep(100 + random.nextInt(400));
	}
}