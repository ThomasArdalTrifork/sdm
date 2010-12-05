package com.trifork.sdm.replication;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
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
import com.google.inject.Module;
import com.trifork.sdm.replication.configuration.DateFormatModule;
import com.trifork.sdm.replication.configuration.TestServerModule;


public class TestRunner extends BlockJUnit4ClassRunner {

	private Injector injector;
	private Object instance;


	public TestRunner(Class<?> type) throws InitializationError, SecurityException, NoSuchMethodException,
			IllegalArgumentException, InstantiationException, IllegalAccessException,
			InvocationTargetException {

		super(type);

		Module testModule = (Module) type.getConstructor().newInstance();

		injector = Guice.createInjector(new TestServerModule(), new DateFormatModule(), testModule);
	}


	@Override
	protected Object createTest() throws Exception {

		instance = super.createTest();
		return instance;
	}


	@Override
	protected void validateTestMethods(List<Throwable> errors) {

		// Overridden to prevent errors when the test methods take parameters.
	}


	@Override
	protected void validatePublicVoidNoArgMethods(Class<? extends Annotation> annotation, boolean isStatic,
			List<Throwable> errors) {

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

			List<Object> parameters = new ArrayList<Object>();

			for (Class<?> type : method.getMethod().getParameterTypes()) {
				// Look up the type in Guice.
				// TODO: We should also take parameter annotations into account.
				Object param = injector.getInstance(type);
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
