package co.technius.starboundmodtoolkit.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import co.technius.starboundmodtoolkit.Util;

public class LoggerOutputStream extends ByteArrayOutputStream
{
	private Logger logger;
	private Level level;

	public LoggerOutputStream(Logger logger, Level level) 
	{
		this.logger = logger;
		this.level = level;
	}

	public void flush() throws IOException
	{
		String record;
		synchronized (this) 
		{
			record = toString();
			super.reset();
			if (record.length() == 0 || record.equals(Util.newLine))
				return;
			logger.log(level, record);
		}
	}
}