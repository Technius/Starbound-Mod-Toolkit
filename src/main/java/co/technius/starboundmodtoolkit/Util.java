package co.technius.starboundmodtoolkit;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.technius.starboundmodtoolkit.mod.AssetIndexer;
import co.technius.starboundmodtoolkit.utilui.MessageDialog;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;

public class Util 
{
	public static final String newLine = System.getProperty("line.separator");
	private static final Pattern jsonEscPat = Pattern.compile("([\"\\\\])");
	private static final String block = "  ";
	
	public static String toPrettyJson(String name, JsonValue v, int blockc)
	{
		if(v.isObject())return toPrettyJson(name, v.asObject(), blockc);
		else if(v.isString())return toPrettyJson(name, v.asString(), blockc);
		else if(v.isNumber())return toPrettyJson(name, v.asDouble(), blockc);
		else if(v.isBoolean())return toPrettyJson(name, v.asBoolean(), blockc);
		else if(v.isArray())return toPrettyJson(name, v.asArray(), blockc);
		return "";
	}
	
	public static String toPrettyJson(String name, JsonObject object, int blockc)
	{
		StringBuilder sb = new StringBuilder().append(nameFormat(name)).append("{");
		boolean first = true;
		String repeat = repeat(block, blockc + 1);
		if(object.size() != 0)sb.append(newLine);
		Iterator<Member> it = object.iterator();
		while(it.hasNext())
		{
			Member m = it.next();
			if(first)first = false;
			else sb.append(",").append(newLine);
			sb.append(repeat).append(toPrettyJson(m.getName(), 
				m.getValue(), blockc + 1));
		}
		if(object.size() != 0)sb.append(newLine).append(repeat(block, blockc));
		else sb.append(" ");
		sb.append("}");
		return sb.toString();
	}
	
	public static String toPrettyJson(String name, String string, int blockc)
	{
		return nameFormat(name) + "\"" + jsonEscPat.matcher(string).replaceAll("\\\\$1") + "\"";
	}
	
	public static String toPrettyJson(String name, double number, int blockc)
	{
		if(number == Math.floor(number))
			return nameFormat(name) + Integer.toString((int)number);
		return nameFormat(name) + Double.toString(number);
	}
	
	public static String toPrettyJson(String name, boolean val, int blockc)
	{
		return nameFormat(name) + Boolean.toString(val);
	}
	
	public static String toPrettyJson(String name, JsonArray array, int blockc)
	{
		StringBuffer sb = new StringBuffer().append(nameFormat(name)).append("[");
		boolean first = true;
		if(array.size() != 0)sb.append(" ").append(newLine);
		for(JsonValue v: array)
		{
			if(first)first = false;
			else sb.append(",").append(newLine);
			sb.append(repeat(block, blockc + 1)).append(toPrettyJson(null, v, blockc + 1));
		}
		if(array.size() != 0)sb.append(newLine).append(repeat(block, blockc));
		else sb.append(" ");
		sb.append("]");
		return sb.toString();
	}
	
	public static String toPrettyJson(String name, JsonObject object)
	{
		return toPrettyJson(name, object, 0);
	}
	
	public static String toPrettyJson(String name, JsonValue v)
	{
		return toPrettyJson(name, v, 0);
	}
	
	public static String toPrettyJson(String name, String string)
	{
		return toPrettyJson(name, string, 0);
	}
	
	public static String toPrettyJson(String name, double number)
	{
		return toPrettyJson(name, number, 0);
	}
	
	public static String toPrettyJson(String name, JsonArray array)
	{
		return toPrettyJson(name, array, 0);
	}
	
	public static String toPrettyJson(String name, boolean val)
	{
		return toPrettyJson(name, val, 0);
	}
	
	public static Object getValue(JsonValue val)
	{
		if(val == null)return null;
		else if(val.isBoolean())return val.asBoolean();
		else if(val.isString())return val.asString();
		else if(val.isNumber())
		{
			double number = val.asDouble();
			if(number == Math.floor(number))
				return (int)number;
			return number;
		}
		return null;
	}
	
	public static JsonValue getValue(Object o)
	{
		if(o instanceof Integer)return JsonValue.valueOf(((Integer) o).intValue());
		else if(o instanceof String)return JsonValue.valueOf((String) o);
		return null;
	}
	
	private static String repeat(String s, int times)
	{
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < times; i ++)sb.append(s);
		return sb.toString();
	}
	
	private static String nameFormat(String name)
	{
		return (name != null ? "\"" + jsonEscPat.matcher(name).replaceAll("\\\\$1") + "\" : " : "");
	}
	
	public static AssetIndexer findAssets(Path path) throws IOException
	{
		return findAssets(path, new AssetIndexer());
	}
	
	public static AssetIndexer findAssets(Path path, AssetIndexer c) throws IOException
	{
		c.src = path;
		Files.walkFileTree(path, c);
		return c;
	}
	
	public static Path findModInfo(Path src) throws IOException
	{
		ModInfoFinder f = new ModInfoFinder();
		Files.walkFileTree(src, f);
		return f.result;
	}
	
	public static void writePrettyJson(JsonObject object, PrintWriter pw)
	{
		String s = toPrettyJson(null, object);
		pw.print(s);
	}
	
	public static void writeJsonToFile(JsonObject object, Path path) throws IOException
	{
		Files.createDirectories(path.getParent());
		OutputStream out = Files.newOutputStream(path);
		PrintWriter pw = new PrintWriter(out);
		Util.writePrettyJson(object, pw);
		pw.flush();
		pw.close();
		out.close();
	}
	
	public static JsonObject readJsonFromFile(Path path) throws IOException
	{
		InputStreamReader r = new InputStreamReader(Files.newInputStream(path));
		JsonObject o = JsonObject.readFrom(r);
		r.close();
		return o;
	}
	
	public static void handleError(Throwable t, String message, String log)
	{
		ModToolkit.log.log(Level.SEVERE, log, t);
		MessageDialog.showErrorDialog(ModToolkit.getInstance().stage, message +
			":" + diagLogException(t), "Starbound Mod Toolkit - Error", t);
	}
	
	public static String diagLogException(Throwable t)
	{
		return Util.newLine + "    " + t.getClass().getName() + ": " + t.getMessage() +
			Util.newLine + "Please report this error so it can be fixed!";
	}
	
	public static String standardizePath(String s)
	{
		return s.replaceAll("[/\\\\\\.]", Matcher.quoteReplacement(File.separator));
	}
	
	static class ModInfoFinder extends SimpleFileVisitor<Path>
	{
		Path result = null;
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
		{
			if(file.getFileName().toString().endsWith(".modinfo"))
			{
				result = file;
				return FileVisitResult.TERMINATE;
			}
			return FileVisitResult.CONTINUE;
		}
	}
}
