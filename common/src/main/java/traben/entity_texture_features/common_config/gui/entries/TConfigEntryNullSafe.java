package traben.entity_texture_features.common_config.gui.entries;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class TConfigEntryNullSafe<E extends Enum<E>> extends TConfigEntryValue<E> {
    public TConfigEntryNullSafe(final String translationKey, final String tooltip, final Supplier<E> getter, final Consumer<E> setter, final E defaultValue) {
        super(translationKey, tooltip, getter, setter, defaultValue);
    }

    @Override
    boolean hasChangedFromInitial() {
        return getValueFromWidget() != (getter.get());
    }

    @SuppressWarnings("unused")
    public abstract TConfigEntryNullSafe<E> allowNullValue();
}