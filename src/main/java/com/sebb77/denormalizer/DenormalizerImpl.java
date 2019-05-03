package com.sebb77.denormalizer;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class DenormalizerImpl implements Denormalizer {

	private static final String PREFIX_SEPARATOR = ".";

	@Override
	public Table<Integer, String, Object> denormalize(Object obj) throws Exception {
		long t = System.currentTimeMillis();
		Table<Integer, String, Object> denormalized = denormalize(TreeBasedTable.create(), new RowAccessControl(), "",
				obj);
		log.debug("Denormalization Total Time: {} ms", (System.currentTimeMillis() - t));
		return denormalized;
	}

	private Table<Integer, String, Object> denormalize(Table<Integer, String, Object> data, RowAccessControl row,
			String prefix, Object obj) throws Exception {

		ObjectType type = getType(obj.getClass());
		log.debug("Object type: {} was defined as {}", obj.getClass().getSimpleName(), type);

		if (type == ObjectType.MAP) {
			Map<?, ?> map = (Map<?, ?>) obj;
			for (Entry<?, ?> entry : map.entrySet()) {
				String name = prefix + PREFIX_SEPARATOR + entry.getKey();
				log.debug("Prefix: {} Value: {}", name, entry.getValue());
				data.putAll(denormalize(data, row, name, entry.getValue()));
			}
		} else if (type == ObjectType.LIST) {
			List<?> list = (List<?>) obj;
			for (Object entry : list) {
				log.debug("Prefix: {} Value: {}", prefix, entry);
				data.putAll(denormalize(data, row, prefix, entry));
				row.newRow();
			}
		} else if (type == ObjectType.CLASS) {

			Field[] fields = obj.getClass().getDeclaredFields();
			for (Field f : fields) {

				if (f.getName().equals("serialVersionUID")) {
					continue;
				}

				f.setAccessible(true);

				Object val = obj == null ? null : f.get(obj);
				if (val == null) {
					continue;
				}

				String name = prefix + PREFIX_SEPARATOR + f.getName();
				data.putAll(denormalize(data, row, name, val));
			}
		} else {
			data.put(row.currentRow(), prefix, obj);
		}

		return data;
	}

	private static ObjectType getType(Class<?> clazz) {

		ObjectType type;

		if (clazz == null) {
			type = ObjectType.UNKNOWN;
		} else if (Map.class.isAssignableFrom(clazz)) {
			type = ObjectType.MAP;
		} else if (List.class.isAssignableFrom(clazz)) {
			type = ObjectType.LIST;
		} else if (String.class.isAssignableFrom(clazz) || char.class.isAssignableFrom(clazz)) {
			type = ObjectType.STRING;
		} else if (Long.class.isAssignableFrom(clazz) || Integer.class.isAssignableFrom(clazz)
				|| byte.class.isAssignableFrom(clazz) || short.class.isAssignableFrom(clazz)
				|| int.class.isAssignableFrom(clazz) || long.class.isAssignableFrom(clazz)) {
			type = ObjectType.INTEGER;
		} else if (BigDecimal.class.isAssignableFrom(clazz) || Float.class.isAssignableFrom(clazz)
				|| float.class.isAssignableFrom(clazz) || Double.class.isAssignableFrom(clazz)
				|| double.class.isAssignableFrom(clazz)) {
			type = ObjectType.DECIMAL;
		} else if (Boolean.class.isAssignableFrom(clazz) || boolean.class.isAssignableFrom(clazz)) {
			type = ObjectType.BOOLEAN;
		} else if (Date.class.isAssignableFrom(clazz)) {
			type = ObjectType.DATE;
		} else if (clazz.isEnum()) {
			type = ObjectType.ENUM;
		} else {
			type = ObjectType.CLASS;
		}

		return type;
	}
}
