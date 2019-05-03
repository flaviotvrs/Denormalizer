package com.sebb77.denormalizer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DenormalizerFactory {

	public static Denormalizer getInstance() {
		return new DenormalizerImpl();
	}

}
