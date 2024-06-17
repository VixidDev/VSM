package dev.vixid.vsm.mixin;

import dev.vixid.vsm.events.MousePress;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MixinMouse {

    @Inject(method = "onMouseButton", at = @At("HEAD"))
    public void onMouseButtonPressed(long window, int button, int action, int mods, CallbackInfo ci) {
        if (action == 1) {
            MousePress.getPRESSED().invoker().onPressed(button, action, mods);
        } else if (action == 0) {
            MousePress.getRELEASED().invoker().onReleased(button, action, mods);
        }
    }
}
