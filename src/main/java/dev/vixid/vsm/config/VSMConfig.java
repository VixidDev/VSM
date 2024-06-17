package dev.vixid.vsm.config;

import com.google.gson.annotations.Expose;
import dev.vixid.vsm.config.features.SpotifyConfig;
import io.github.notenoughupdates.moulconfig.Config;
import io.github.notenoughupdates.moulconfig.Social;
import io.github.notenoughupdates.moulconfig.annotations.Category;
import java.util.Collections;
import java.util.List;
import net.minecraft.util.ResourceLocation;

public class VSMConfig extends Config {
    public static final ResourceLocation GITHUB = new ResourceLocation("textures/gui/options_background.png");

    @Override
    public List<Social> getSocials() {
        return Collections.singletonList(
                Social.forLink("GitHub", GITHUB, "https://github.com/VixidDev")
        );
    }

    @Override
    public String getTitle() {
        return "§bVixid's Skyblock Mod Config";
    }

    @Override
    public void saveNow() {
        ConfigManager.INSTANCE.saveConfig();
    }

    @Expose
    @Category(name = "Spotify", desc = "Settings for local Spotify control.")
    public SpotifyConfig spotifyConfig = new SpotifyConfig();
}
