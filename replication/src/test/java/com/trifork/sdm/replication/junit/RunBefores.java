package com.trifork.sdm.replication.junit;

import java.util.ArrayList;
import java.util.List;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import com.google.inject.Injector;


public class RunBefores extends Statement {

	private final Statement fNext;
	private final Object fTarget;
	private final List<FrameworkMethod> fBefores;
	private final Injector injector;


	public RunBefores(Statement next, List<FrameworkMethod> befores, Object target, Injector injector) {

		fNext = next;
		fBefores = befores;
		fTarget = target;
		this.injector = injector;
	}


	@Override
	public void evaluate() throws Throwable {

		for (FrameworkMethod method : fBefores) {
			List<Object> parameters = new ArrayList<Object>();

			for (Class<?> type : method.getMethod().getParameterTypes()) {
				// Look up the type in Guice.
				// TODO: We should also take parameter annotations into account.
				
				try {
					Object param = injector.getInstance(type);
					parameters.add(param);
				}
				catch (Throwable t) {
					t.printStackTrace();
					System.exit(-1);
				}					
			}

			if (parameters.isEmpty())
				method.invokeExplosively(fTarget);
			else
				method.invokeExplosively(fTarget, parameters.toArray());

		}

		fNext.evaluate();
	}
}