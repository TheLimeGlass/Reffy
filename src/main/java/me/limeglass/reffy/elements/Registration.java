package me.limeglass.reffy.elements;

import java.io.StreamCorruptedException;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.yggdrasil.Fields;
import me.limeglass.reffy.managers.ReferenceManager;
import me.limeglass.reffy.objects.Reference;

public class Registration {

	static {
		Classes.registerClass(new ClassInfo<>(Reference.class, "reference")
				.user("references?")
				.name("Reference")
				.defaultExpression(new EventValueExpression<>(Reference.class))
				.parser(new Parser<Reference>() {
					@Override
					@Nullable
					public Reference parse(String input, ParseContext context) {
						return ReferenceManager.get(input).orElseGet(() -> {
							Reference reference = new Reference(input);
							ReferenceManager.add(reference);
							return reference;
						});
					}
					
					@Override
					public boolean canParse(ParseContext context) {
						return context != ParseContext.CONFIG;
					}
					
					@Override
					public String toString(Reference reference, int flags) {
						return reference.getName();
					}
					
					@Override
					public String toVariableNameString(Reference reference) {
						return reference.getName();
					}
					
					@Override
					public String getVariableNamePattern() {
						return "\\S+";
					}
				}).serializer(new Serializer<Reference>() {
					@Override
					public Fields serialize(Reference reference) {
						Fields fields = new Fields();
						boolean references = reference.hasReferences();
						fields.putPrimitive("references", references);
						fields.putPrimitive("name", reference.getName());
						if (references) {
							for (String name : reference.getNames()) {
								fields.putObject("name:" + name, reference.getReference(name));
							}
						}
						return fields;
					}
					
					@Override
					public void deserialize(Reference o, Fields f) throws StreamCorruptedException {
						assert false;
					}
					
					@Override
					public Reference deserialize(Fields fields) throws StreamCorruptedException {
						String name = fields.getAndRemovePrimitive("name", String.class);
						Reference reference = ReferenceManager.get(name).orElse(new Reference());
						if (fields.getAndRemovePrimitive("references", boolean.class)) {
							fields.forEach(field -> {
								String ID = field.getID();
								if (ID.startsWith("name:")) {
									ID = ID.replaceFirst("name:", "");
									try {
										reference.add(ID, field.getObject());
									} catch (StreamCorruptedException e) {
										e.printStackTrace();
									}
								}
							});
						}
						return reference;
					}

					@Override
					public boolean mustSyncDeserialization() {
						return false;
					}

					@Override
					protected boolean canBeInstantiated() {
						return true;
					}
				}));
	}
	
}
