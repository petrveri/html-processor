package org.petrveri.util.htmlprocessor;

import org.apache.commons.lang3.StringUtils;
import org.petrveri.util.htmlprocessor.exceptions.ApplicationException;
import org.petrveri.util.htmlprocessor.parsing.FileProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Petro Veriienko
 */
@Component
public class HtmlProcessorApplication {

	@Inject
	private FileProcessor fileProcessor;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(HtmlProcessorConfig.class);
		HtmlProcessorApplication app = context.getBean(HtmlProcessorApplication.class);
		app.run(args);
	}

	void run(String[] args) {
		String inputPath = args.length > 0 ? args[0] : null;
		String outputPath = args.length > 1 ? args[1] : null;
		logger.info("The application is run with arguments: [{}] and [{}].",
				StringUtils.isBlank(inputPath) ? "BLANK" : inputPath,
				StringUtils.isBlank(outputPath) ? "BLANK" : outputPath);
		try {
			fileProcessor.processFiles(inputPath, outputPath);
		} catch (ApplicationException e) {
			logger.error("{}", handleException(e));
		}
	}

	String handleException(ApplicationException e) {
		StringBuilder stringBuilder = new StringBuilder();
		if (e.getCustomMessage() != null) {
			stringBuilder.append("Custom message: ");
			stringBuilder.append(e.getCustomMessage());
		}
		Throwable throwable = e.getThrowable();
		if (throwable != null) {
			if (stringBuilder.length() > 0) {
				stringBuilder.append("\n\n");
			}
			StringWriter swr = new StringWriter();
			throwable.printStackTrace(new PrintWriter(swr));
			stringBuilder.append(swr.toString());
		}
		return stringBuilder.toString();
	}
}