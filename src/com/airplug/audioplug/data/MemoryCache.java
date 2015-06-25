package com.airplug.audioplug.data;

import java.lang.ref.Reference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public abstract class MemoryCache<K, V> {
	private final Map<K, Reference<V>> map_ = Collections.synchronizedMap(new HashMap<K, Reference<V>>());
	
	public V get(K key) {
		V result = null;
		Reference<V> ref = map_.get(key);
		if (ref != null) {
			result = ref.get();
		}
		return result;
	}
	
	public boolean put(K key, V value) {
		map_.put(key, createRef(value));
		return true;
	}
	
	public void revmoe(K key) {
		map_.remove(key);
	}
	
	public void clear() {
		map_.clear();
	}

	
	protected abstract Reference<V> createRef(V value);
}
