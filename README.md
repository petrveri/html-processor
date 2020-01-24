Character (HTML) File Processor
================================================================
## Purpose
The application is developed to process character (html) UTF-8 formatted files on a local disk to remove multiple entries of predefined fragments. E.g. html tags.
You can specify several application properties in app.properties file, such as:
	file.line.separator
	default.output.directory.path
	default.input.directory.path
	custom.html.tags
	custom.html.tag.X.begin
	custom.html.tag.X.end
There are several predefined html tags to process.

## Usage:
java -jar html-processor.jar
java -Dlogback.loglevel=WARN -Dlogback.root.loglevel=OFF -jar html-processor.jar
	such way you override default log level values for the application code logging and root level code logging.
java -jar html-processor.jar <argument1> <argument2>
	<argument1> can have values:
		file path
		file name
		file mask *.html
		empty
		directory path
		directory name
	<argument2> can have values:
		directory path
		diectory name
		empty