package me.limeglass.reffy.elements.expressions;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import me.limeglass.reffy.objects.Reference;

public class ExprReference extends SimpleExpression<Object> {

	static {
		Skript.registerExpression(ExprReference.class, Object.class, ExpressionType.COMBINED, "%reference% of reference %references%", "reference %references%'[s] %reference%");
	}
	
	private Expression<Reference> name, references;
	
	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Object> getReturnType() {
		return Object.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		if (matchedPattern == 0) {
			name = (Expression<Reference>) exprs[0];
			references = (Expression<Reference>) exprs[1];
		} else {
			name = (Expression<Reference>) exprs[1];
			references = (Expression<Reference>) exprs[0];
		}
		return true;
	}

	@Override
	@Nullable
	protected Object[] get(Event event) {
		Set<Object> objects = new HashSet<>();
		String name = this.name.getSingle(event).getName();
		for (Reference reference : references.getAll(event)) {
			Optional<Object> optional = reference.getReference(name);
			if (optional.isPresent())
				objects.add(optional.get());
		}
		return objects.toArray(new Object[objects.size()]);
	}
	
	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return name.toString(event, debug) + " of " + references.toString(event, debug);
	}
	
	@Nullable
	@Override
	public Class<?>[] acceptChange(ChangeMode mode) {
		return CollectionUtils.array(Object.class);
	}
	
	@Override
	public void change(Event event, @Nullable Object[] delta, ChangeMode mode) {
		Reference[] adjustments = references.getAll(event);
		if (adjustments == null)
			return;
		String name = this.name.getSingle(event).getName();
		switch (mode){
			case ADD:
			case SET:
				for (Reference reference : adjustments) {
					reference.add(name, delta[0]);
				}
				break;
			case RESET:
			case DELETE:
				for (Reference reference : adjustments) {
					reference.clear();
				}
				break;
			case REMOVE:
			case REMOVE_ALL:
				for (Reference reference : adjustments) {
					reference.remove(name);
				}
				break;
			default:
				break;
		}
	}

}
