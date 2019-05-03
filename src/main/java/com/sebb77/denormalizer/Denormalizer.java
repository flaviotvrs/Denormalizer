package com.sebb77.denormalizer;

import com.google.common.collect.Table;

public interface Denormalizer {

	/**
	 * 
	 * @param obj
	 *            - There is no need that the class is a serializable class. Any
	 *            class will work.
	 * @return
	 * @throws Exception
	 */
	Table<Integer, String, Object> denormalize(Object obj) throws Exception;

}
