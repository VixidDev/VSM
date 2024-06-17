package dev.vixid.vsm.config.core.gui;

import io.github.notenoughupdates.moulconfig.GuiTextures;
import io.github.notenoughupdates.moulconfig.common.RenderContext;
import io.github.notenoughupdates.moulconfig.gui.GuiOptionEditor;
import io.github.notenoughupdates.moulconfig.gui.KeyboardEvent;
import io.github.notenoughupdates.moulconfig.gui.MouseEvent;
import io.github.notenoughupdates.moulconfig.processor.ProcessedOption;
import net.minecraft.client.util.InputUtil;

public class GuiOptionEditorKeybind extends GuiOptionEditor {
    private final String defaultKeycode;
    private boolean editingKeycode;

    private int width = -1;
    private final int height;


    public GuiOptionEditorKeybind(ProcessedOption option, String defaultKey) {
        super(option);
        this.defaultKeycode = defaultKey;
        this.height = getHeight();
    }

    @Override
    public void render(RenderContext context, int x, int y, int width) {
        super.render(context, x, y, width);

        if (this.width == -1) this.width = width;

        int buttonX = x + this.width / 6 - 24;
        int buttonY = y + this.height - 7 - 14;

        context.bindTexture(GuiTextures.BUTTON);
        context.drawTexturedRect(buttonX, buttonY, 48, 16);

        String keyName = getKeyName((String) option.get());
        String buttonText = editingKeycode ? "> " + keyName + " <" : keyName;

        context.drawStringCenteredScaledMaxWidth(
                buttonText,
                context.getMinecraft().getDefaultFontRenderer(),
                buttonX + 24,
                buttonY + 8,
                false,
                40,
                0xFF303030
        );

        context.bindTexture(GuiTextures.RESET);
        context.drawTexturedRect(buttonX + 51, buttonY + 3, 10, 11);
    }

    @Override
    public boolean mouseInput(int x, int y, int width, int mouseX, int mouseY, MouseEvent mouseEvent) {
        if (mouseEvent instanceof MouseEvent.Click click) {
            if (click.getMouseState()) {
                if (editingKeycode) {
                    editingKeycode = false;
                    option.set(InputUtil.Type.MOUSE.createFromCode(click.getMouseButton()).getTranslationKey());
                    return true;
                }

                if (isOverButton(x, y, mouseX, mouseY)) {
                    editingKeycode = true;
                    return true;
                }

                if (isOverResetButton(x, y, mouseX, mouseY)) {
                    option.set(defaultKeycode);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isOverButton(int x, int y, int mouseX, int mouseY) {
        int buttonX = x + width / 6 - 24;
        int buttonY = y + height - 7 - 14;

        return mouseX > buttonX && mouseX < buttonX + 48 &&
                mouseY > buttonY && mouseY < buttonY + 16;
    }

    private boolean isOverResetButton(int x, int y, int mouseX, int mouseY) {
        int buttonX = x + width / 6 - 24 + 51;
        int buttonY = y + height - 7 - 14 + 3;

        return mouseX > buttonX && mouseX < buttonX + 10 &&
                mouseY > buttonY && mouseY < buttonY + 11;
    }

    @Override
    public boolean keyboardInput(KeyboardEvent event) {
        if (event instanceof KeyboardEvent.KeyPressed key && key.getPressed()) {
            if (editingKeycode) {
                editingKeycode = false;
                if (key.getKeycode() == 256) {
                    option.set("key.keyboard.unknown");
                    return true;
                }
                String translationKey = InputUtil.fromKeyCode(key.getKeycode(), 0).getTranslationKey();
                option.set(translationKey);
                return true;
            }
        }
        return false;
    }

    private String getKeyName(String translationKey) {
        if (translationKey.equals("key.keyboard.unknown")) {
            return "NONE";
        } else {
            return InputUtil.fromTranslationKey(translationKey).getLocalizedText().getString();
        }
    }
}
