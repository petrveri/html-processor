package org.petrveri.util.htmlprocessor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.petrveri.util.htmlprocessor.exceptions.ApplicationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.inject.Inject;
import java.io.IOException;

/**
 * @author Petro Veriienko
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HtmlProcessorApplicationTestConfig.class}, loader = AnnotationConfigContextLoader.class)
class HtmlProcessorApplicationTest {

	@Inject
	private HtmlProcessorApplication app;

	@Test
	void testHandleExceptionCustomMessage() {
		String customMessage = "some custom message";
		Assertions.assertEquals("Custom message: " + customMessage,
				app.handleException(new ApplicationException(customMessage)));
	}

	@Test
	void testHandleExceptionThrowable() {
		ApplicationException appExc = new ApplicationException(new IOException());
		String actualResult = app.handleException(appExc);
		Assertions.assertTrue(actualResult.contains("java.io.IOException"));
	}

	@Test
	void testHandleExceptionThrowableCustomMessage() {
		String aMessage = "some IO Exception message";
		Throwable throwable = new IOException();
		ApplicationException appExc = new ApplicationException(throwable, aMessage);
		String actualResult = app.handleException(appExc);
		Assertions.assertTrue(actualResult.startsWith("Custom message: " + aMessage));
		Assertions.assertTrue(actualResult.contains("java.io.IOException"));
	}
}
