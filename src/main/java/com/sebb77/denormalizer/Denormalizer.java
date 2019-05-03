package com.sebb77.denormalizer;

import com.google.common.collect.Table;

public interface Denormalizer {

	Table<Integer, String, Object> denormalize(Object obj) throws Exception;

}
