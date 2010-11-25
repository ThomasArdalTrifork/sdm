package com.trifork.sdm.persistence;

import static com.trifork.sdm.models.AbstractEntity.getIdMethod;
import static com.trifork.sdm.models.AbstractEntity.getOutputFieldName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trifork.sdm.models.Entity;
import com.trifork.sdm.persistence.annotations.Output;

public class Dataset<T extends Entity>
{
	private Map<Object, List<T>> entities = new HashMap<Object, List<T>>();
	private Class<? extends Entity> type;


	public Dataset(List<T> entities, Class<? extends Entity> T)
	{
		this.type = T;

		for (T entity : entities)
		{
			List<T> ents = new ArrayList<T>();
			ents.add(entity);
			this.entities.put(entity.getEntityId(), ents);
		}
	}


	public Dataset(Class<? extends Entity> type)
	{
		this.type = type;
	}


	public Collection<T> getEntities()
	{
		Collection<T> allEnts = new ArrayList<T>();

		for (List<T> ents : entities.values())
		{
			allEnts.addAll(ents);
		}

		return allEnts;
	}


	public T getEntityById(Object id)
	{
		List<T> ents = entities.get(id);

		if (ents == null) return null;

		if (ents.size() == 1) return ents.get(0);

		throw new RuntimeException("Multiple entities exist with entityid " + id);
	}


	public List<T> getEntitiesById(Object id)
	{
		return entities.get(id);
	}


	public Class<? extends Entity> getType()
	{
		return type;
	}


	/**
	 * @return the name that this entity type (class) should be displayed with
	 *         when output
	 */
	public String getEntityTypeDisplayName()
	{
		Output output = type.getAnnotation(Output.class);
		if (output != null && !"".equals(output.name())) return output.name();
		return type.getSimpleName();
	}


	public static String getEntityTypeDisplayName(Class<? extends Entity> type)
	{
		Output output = type.getAnnotation(Output.class);
		if (output != null && !"".equals(output.name())) return output.name();
		return type.getSimpleName();
	}


	public void removeEntities(List<T> entities)
	{
		for (T entity : entities)
		{
			this.entities.remove(entity.getEntityId());
		}
	}


	public void addEntity(T entity)
	{
		Object id = entity.getEntityId();
		List<T> ents = entities.get(id);
		if (ents == null)
		{
			ents = new ArrayList<T>();
			entities.put(id, ents);
		}
		ents.add(entity);
	}


	public String getIdOutputName()
	{
		return getOutputFieldName(getIdMethod(type));
	}


	// TODO: What is this method for?
	public static String getIdOutputName(Class<? extends Entity> type)
	{
		return getOutputFieldName(getIdMethod(type));
	}
}
