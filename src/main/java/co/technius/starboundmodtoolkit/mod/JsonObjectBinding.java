package co.technius.starboundmodtoolkit.mod;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.RUNTIME)
public @interface JsonObjectBinding
{
	String[] base() default {};
	String key();
	boolean required() default false;
	Type type();
	String[] keyBinding() default {};
	Type[] valueBinding() default {};
	
	enum Type
	{
		STRING, INTEGER, DOUBLE, BOOLEAN, OBJECT, ARRAY;
	}
}
