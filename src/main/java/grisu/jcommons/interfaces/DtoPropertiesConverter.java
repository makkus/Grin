package grisu.jcommons.interfaces;

import java.util.Map;

import grisu.model.info.dto.DtoProperties;

import org.dozer.CustomConverter;

public class DtoPropertiesConverter implements CustomConverter {

	@Override
	public Object convert(Object existingDestinationFieldValue,
			Object sourceFieldValue, Class<?> destinationClass,
			Class<?> sourceClass) {
		
		if ( sourceFieldValue == null ) {
			return null;
		}

		return DtoProperties.createProperties((Map<String, String>) sourceFieldValue);

	}

}
