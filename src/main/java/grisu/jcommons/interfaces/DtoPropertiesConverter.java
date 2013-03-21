package grisu.jcommons.interfaces;

import grisu.model.info.dto.DtoProperties;

import java.util.Map;

import org.dozer.CustomConverter;

import com.google.common.collect.Maps;

public class DtoPropertiesConverter implements CustomConverter {

	@Override
	public Object convert(Object existingDestinationFieldValue,
			Object sourceFieldValue, Class<?> destinationClass,
			Class<?> sourceClass) {
		
		if ( sourceFieldValue == null ) {
			return null;
		}
		Map<Object, Object> props = (Map)sourceFieldValue;
		
		Map<String, String> newMap = Maps.newLinkedHashMap();
		for(Map.Entry entry : props.entrySet()) {
		   newMap.put(entry.getKey().toString(), entry.getValue().toString());
		}


		return DtoProperties.createProperties((Map<String, String>) sourceFieldValue);

	}

}
