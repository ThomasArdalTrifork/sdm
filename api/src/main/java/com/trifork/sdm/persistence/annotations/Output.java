package com.trifork.sdm.persistence.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * This annotation shows that during streaming, this method should be called and
 * the return value streamed. Intended to mark which getters should be called.
 * 
 * @author Rune (rsl@trifork.com)
 * @author Thomas Børlum (thb@trifork.com)
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Output
{
	/**
	 * The name the annotated class or method that should be called in the
	 * output.
	 */
	public String name() default "";


	/**
	 * If specified on a class if denotes which versions of the schema that will
	 * be generated and are supported. If specified on a method it denotes which
	 * versions of the schema this element should be output in.
	 */
	public int[] supportedVersions() default {};
	
	/**
	 * The documentation string that will be written to the XML Schemas.
	 */
	public String documentation() default "";
}
