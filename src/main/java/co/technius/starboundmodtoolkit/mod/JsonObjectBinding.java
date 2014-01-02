package co.technius.starboundmodtoolkit.mod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD })
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
