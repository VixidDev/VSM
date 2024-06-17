package dev.vixid.vsm.config.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.notenoughupdates.moulconfig.ChromaColour;
import io.github.notenoughupdates.moulconfig.LegacyStringChromaColourTypeAdapter;
import io.github.notenoughupdates.moulconfig.managed.DataMapper;
import io.github.notenoughupdates.moulconfig.observer.PropertyTypeAdapterFactory;
import java.lang.reflect.InvocationTargetException;
import org.jetbrains.annotations.NotNull;

public class VSMGsonMapper<T> implements DataMapper<T> {

    public Class<T> clazz;

    public Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(new PropertyTypeAdapterFactory())
            .registerTypeAdapter(ChromaColour.class, new LegacyStringChromaColourTypeAdapter(true))
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    public VSMGsonMapper(Class<T> clazz) {
        this.clazz = clazz;
    }

    @NotNull
    @Override
    public String serialize(T value) {
        return gson.toJson(value);
    }

    @Override
    public T createDefault() {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T deserialize(@NotNull String string) {
        return gson.fromJson(string, clazz);
    }
}
