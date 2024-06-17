package dev.vixid.vsm.config;

import com.google.gson.annotations.Expose;
import com.mojang.brigadier.CommandDispatcher;
import dev.vixid.vsm.VSM;
import io.github.notenoughupdates.moulconfig.Config;
import io.github.notenoughupdates.moulconfig.Social;
import io.github.notenoughupdates.moulconfig.annotations.Category;
import io.github.notenoughupdates.moulconfig.common.MyResourceLocation;
import java.util.Collections;
import java.util.List;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class VSMConfig extends Config {

    public static final MyResourceLocation GITHUB = new MyResourceLocation("minecraft", "textures/block/amethyst_block.png");

    public void initialise() {
        ClientCommandRegistrationCallback.EVENT.register(this::configCommands);
    }

    private void configCommands(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(literal("vsm")
                .executes(command -> {
                    MinecraftClient.getInstance().send(() -> VSM.INSTANCE.getConfig().openConfigGui());
                    return 0;
                })
                .then(literal("saveconfig").executes(command -> {
                    VSM.INSTANCE.getConfig().getInstance().saveNow();
                    return 0;
                }))
        );
    }

    @Override
    public List<Social> getSocials() {
        return Collections.singletonList(Social.forLink("GitHub", GITHUB, "https://www.github.com/VixidDev"));
    }

    @Override
    public String getTitle() {
        return "§bVixid's Skyblock Mod Config";
    }

    @Override
    public void saveNow() {
        VSM.INSTANCE.getLogger().debug("Saved config");
        super.saveNow();
    }

    @Expose
    @Category(name = "Spotify", desc = "Settings for local Spotify control")
    public SpotifyConfig spotifyConfig = new SpotifyConfig();
}
