package dev.vixid.vsm.mixins.transformers;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.notenoughupdates.moulconfig.Config;
import io.github.notenoughupdates.moulconfig.managed.ManagedConfig;
import io.github.notenoughupdates.moulconfig.managed.ManagedConfigBuilder;
import io.github.notenoughupdates.moulconfig.processor.ConfigProcessorDriver;
import io.github.notenoughupdates.moulconfig.processor.MoulConfigProcessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ManagedConfig.class, remap = false)
public class MixinManagedConfig<T extends Config> {

    @Inject(method = "buildProcessor", at = @At(value = "INVOKE", target = "Lio/github/notenoughupdates/moulconfig/processor/ConfigProcessorDriver;processConfig(Lio/github/notenoughupdates/moulconfig/Config;)V", shift = At.Shift.BEFORE))
    private void buildProcessor(ManagedConfigBuilder<T> builder, CallbackInfoReturnable<MoulConfigProcessor<T>> cir, @Local ConfigProcessorDriver driver) {
        driver.warnForPrivateFields = false;
    }

}
