package com.trifork.sdm.replication.junit;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.trifork.sdm.replication.ReplicationTest;
import com.trifork.sdm.replication.configuration.ServerModule;


public class TestRunner extends BlockJUnit4ClassRunner {

	private Injector injector;
	private ReplicationTest instance;

	{
		ClassLoader currentThreadClassLoader = Thread.currentThread().getContextClassLoader();

		// Add the conf dir to the classpath
		// Chain the current thread classloader

		URLClassLoader urlClassLoader = null;

		try {
			urlClassLoader = new URLClassLoader(new URL[] { new File("test-classes").toURL() }, currentThreadClassLoader);
		}
		catch (MalformedURLException e) {

			e.printStackTrace();
		}
		
		Thread.currentThread().setContextClassLoader(urlClassLoader);

		// This should work now!
		Thread.currentThread().getContextClassLoader().getResourceAsStream("context.xml");
	}


	public TestRunner(Class<?> type) throws InitializationError, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {

		super(type);
		
		injector = Guice.createInjector(new ServerModule());
	}


	@Override
	protected Object createTest() throws Exception {

		instance = (ReplicationTest) super.createTest();

		injector = injector.createChildInjector(instance.getConfiguration());
		injector = injector.createChildInjector(instance);

		return instance;
	}


	@Override
	protected void validateTestMethods(List<Throwable> errors) {

		// Overridden to prevent errors when the test methods take parameters.
	}


	@Override
	protected void validatePublicVoidNoArgMethods(Class<? extends Annotation> annotation, boolean isStatic, List<Throwable> errors) {

		// Overridden to prevent errors when the test methods take parameters.
	}


	@Override
	protected Statement withBeforeClasses(Statement statement) {

		List<FrameworkMethod> befores = getTestClass().getAnnotatedMethods(BeforeClass.class);
		return befores.isEmpty() ? statement : new RunBefores(statement, befores, instance, injector);
	}


	@Override
	protected void runChild(FrameworkMethod method, RunNotifier notifier) {

		if (instance == null) try {
			createTest();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}

		EachTestNotifier eachNotifier = makeNotifier(method, notifier);

		if (method.getAnnotation(Ignore.class) != null) {
			eachNotifier.fireTestIgnored();
			return;
		}

		eachNotifier.fireTestStarted();

		try {
			// Look up the type in Guice.

			List<Object> parameters = new ArrayList<Object>();

			Class<?>[] paramTypes = method.getMethod().getParameterTypes();
			Annotation[][] paramAnnotations = method.getMethod().getParameterAnnotations();

			for (int i = 0; i < paramTypes.length; i++) {

				Class<?> type = paramTypes[i];
				
				Object param;
				
				// TODO: At the moment we only support one annotation on params.
				if (paramAnnotations[i].length > 0) {
					Annotation annotation = paramAnnotations[i][0];
					param = injector.getInstance(Key.get(type, annotation));
				}
				else {
					param = injector.getInstance(type);
				}
				
				parameters.add(param);
			}

			if (parameters.isEmpty())
				method.invokeExplosively(instance);
			else
				method.invokeExplosively(instance, parameters.toArray());
		}
		catch (Throwable e) {
			eachNotifier.addFailure(e);
		}
		finally {
			eachNotifier.fireTestFinished();
		}
	}


	private EachTestNotifier makeNotifier(FrameworkMethod method, RunNotifier notifier) {

		Description description = describeChild(method);
		return new EachTestNotifier(notifier, description);
	}


	@Override
	protected String testName(FrameworkMethod method) {

		String testName = method.getName().replace('_', ' ').concat(".");
		return testName.substring(0, 1).toUpperCase() + testName.substring(1);
	}
}
