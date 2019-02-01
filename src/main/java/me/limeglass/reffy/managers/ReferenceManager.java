package me.limeglass.reffy.managers;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import me.limeglass.reffy.objects.Reference;

public class ReferenceManager {

	private static Set<Reference> references = new HashSet<>();

	public static Optional<Reference> get(String name) {
		return references.parallelStream()
				.filter(reference -> reference.getName().equals(name))
				.findFirst();
	}
	
	public static boolean add(Reference reference) {
		return references.add(reference);
	}
	
	public static void remove(String name) {
		Optional<Reference> reference = get(name);
		if (reference.isPresent())
			references.remove(reference.get());
	}
	
}
