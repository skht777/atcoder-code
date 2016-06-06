/**
 * 
 */
package com.skht777.atcoder;

/**
 * @author skht777
 *
 */
public class Pair<V> extends javafx.util.Pair<String, V> {

	private static final long serialVersionUID = 1L;

	public Pair(String key, V value) {
		super(key, value);
	}
	
	@Override
	public String toString() {
		return getKey();
	}

}
