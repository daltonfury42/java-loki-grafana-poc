package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
	final static List<String> runs = List.of("sink-1234", "sink-5678", "sink-91011", "sink-1213", "sink-1415", "sink-1617");
	private static final Logger logger = LogManager.getLogger(Main.class);
	
	public static void main(String[] args) throws InterruptedException {
		//TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
		// to see how IntelliJ IDEA suggests fixing it.
		for (int i = 1; i >= 0; i++) {
			setThreadContext(i);

			// add some random log messages
			logger.info("This is info log number {}", i);
			logger.warn("This is warn log number {}", i);
			logger.debug("This is debug log number {}", i);

			logger.info("Some more complex log message with parameters: {}, {}, {}", "param1", 42, 3.14);
			//TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
			// for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
			try {
				throw new RuntimeException("ex for iter " + i);
			} catch (Exception e) {
				logger.error("An error occurred for iter {}", i, e);
			}

			Thread.sleep(1000);
		}
	}

	private static void setThreadContext(int i) {

		if (i % 5 == 0) {
			ThreadContext.clearAll();
			ThreadContext.put("sinkId", runs.get(((int) (Math.random() * 100)) % runs.size()));
			ThreadContext.put("runId", "2593" + i);
		}
	}
}