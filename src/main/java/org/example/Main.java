package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
	private static final Logger logger = LogManager.getLogger(Main.class);
	public static void main(String[] args) throws InterruptedException {
		//TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
		// to see how IntelliJ IDEA suggests fixing it.
		for (int i = 1; i >= 0; i++) {
			//TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
			// for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
			try {
				throw new RuntimeException("ex for iter " + i);
			} catch (Exception e) {
				logger.error("An error occurred for iter {}", i, e);
			}

			Thread.sleep(10000);
		}
	}
}