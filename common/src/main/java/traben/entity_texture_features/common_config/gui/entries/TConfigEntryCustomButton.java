package traben.entity_texture_features.common_config.gui.entries;

import com.demonwav.mcdev.annotations.Translatable;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;

public class TConfigEntryCustomButton extends TConfigEntry {


    private final Button button;

    public TConfigEntryCustomButton(@Translatable final String text, @Translatable final String tooltip, Button.OnPress action) {
        super(text, tooltip);
        this.button = Button.builder(getText(), action).bounds(0, 0, 0, 0).tooltip(getTooltip()).build();
    }

    @SuppressWarnings("unused")
    public TConfigEntryCustomButton(@Translatable final String text, Button.OnPress button) {
        this(text, null, button);
    }

    @Override
    public AbstractWidget getWidget(final int x, final int y, final int width, final int height) {
        button.setRectangle(width, height, x, y);
        return button;
    }

    @Override
    boolean saveValuesToConfig() {
        return false;
    }

    @Override
    void setValuesToDefault() {
    }

    @Override
    void resetValuesToInitial() {
    }

    @Override
    boolean hasChangedFromInitial() {
        return false;
    }
}