package com.platon.mtool.common.resolver;

/** Created by liyf. */
public interface Resolver<T extends PlatonParameterize> {

  T resolv(String input);
}
