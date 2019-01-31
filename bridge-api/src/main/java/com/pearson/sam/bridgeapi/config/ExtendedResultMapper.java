package com.pearson.sam.bridgeapi.config;

import com.github.vanroy.springdata.jest.internal.MultiDocumentResult;
import com.github.vanroy.springdata.jest.mapper.DefaultJestResultsMapper;
import com.google.gson.JsonElement;

import io.searchbox.core.DocumentResult;

import java.util.LinkedList;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentProperty;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.context.MappingContext;

public class ExtendedResultMapper extends DefaultJestResultsMapper {

	protected MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> mappingContext;
	protected EntityMapper entityMapper;

	public ExtendedResultMapper(
			MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> mappingContext) {
		super(mappingContext);
		this.mappingContext = mappingContext;
	}

	public ExtendedResultMapper(EntityMapper entityMapper) {
		super(entityMapper);
		this.entityMapper = entityMapper;
	}

	@Override
	public <T> T mapResult(DocumentResult response, Class<T> clazz) {
		T result = super.mapResult(response, clazz);
		if (result != null) {
			setPersistentEntityVersion(result, response.getVersion(), clazz);
		}
		return result;
	}

	@Override
	public <T> LinkedList<T> mapResults(MultiDocumentResult responses, Class<T> clazz) {
		LinkedList<T> results = super.mapResults(responses, clazz);
		if (results != null) {
			for (int i = 0; i < results.size(); i++) {
				setPersistentEntityVersion(results.get(i), getVersionOfDocument(i, responses), clazz);
			}
		}
		return results;
	}

	private <T> void setPersistentEntityVersion(T result, Long version, Class<T> clazz) {
		if (mappingContext != null && clazz.isAnnotationPresent(Document.class)) {
			PersistentProperty<ElasticsearchPersistentProperty> versionProperty = mappingContext
					.getPersistentEntity(clazz).getVersionProperty();
			if (versionProperty != null && versionProperty.getType().isAssignableFrom(Long.class)) {
				java.lang.reflect.Method setter = versionProperty.getSetter();
				if (setter != null) {
					try {
						setter.invoke(result, version);
					} catch (Throwable t) {
						t.printStackTrace();
					}
				}
			}
		}
	}

	private Long getVersionOfDocument(int i, MultiDocumentResult responses) {
		for (JsonElement jsonElement : responses.getJsonObject().getAsJsonArray("docs")) {
			if (jsonElement.getAsJsonObject().get("found").getAsBoolean()) {
				return jsonElement.getAsJsonObject().get("_version").getAsLong();
			}
		}
		return 0L;
	}
}