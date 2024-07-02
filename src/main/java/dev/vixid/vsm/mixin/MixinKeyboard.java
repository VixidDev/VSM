package dev.vixid.vsm.mixin;

import dev.vixid.vsm.events.KeyPress;
import dev.vixid.vsm.features.spotify.ControlUtils;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class MixinKeyboard {

    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (ControlUtils.INSTANCE.getIgnoreKeyCallback()) {
            ControlUtils.INSTANCE.setIgnoreKeyCallback(false);
            ci.cancel();
            return;
        }
        if (action == 1) {
            KeyPress.Companion.getPRESSED().invoker().onPressed(key, scancode, action, modifiers);
        } else if (action == 0) {
            KeyPress.Companion.getRELEASED().invoker().onReleased(key, scancode, action, modifiers);
        }
    }
}
