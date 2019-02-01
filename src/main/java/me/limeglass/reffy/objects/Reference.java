package me.limeglass.reffy.objects;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class Reference {

	private Map<String, Object> references = new HashMap<>();
	private String name;
	
	public Reference() {}
	
	public Reference(String name) {
		this.name = name;
	}
	
	public Optional<Object> getReference(String name) {
		return Optional.ofNullable(references.get(name));
	}
	
	public void add(String name, Object object) {
		references.put(name, object);
	}
	
	public void remove(String name) {
		references.remove(name);
	}
	
	public boolean hasReferences() {
		return references.isEmpty();
	}
	
	public Set<String> getNames() {
		return references.keySet();
	}
	
	public String getName() {
		return name;
	}
	
	public void clear() {
		references.clear();
	}
	
}
