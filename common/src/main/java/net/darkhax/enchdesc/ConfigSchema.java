package net.darkhax.enchdesc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.Expose;
import com.mojang.serialization.JsonOps;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;

public class ConfigSchema {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(Style.class, new Serializer()).create();

    @Expose
    public boolean enableMod = true;

    @Expose
    public boolean onlyDisplayOnBooks = false;

    @Expose
    public boolean onlyDisplayInEnchantingTable = false;

    @Expose
    public boolean requireKeybindPress = false;

    @Expose
    public int indentSize = 0;

    @Expose
    public Style style = Style.EMPTY.withColor(ChatFormatting.DARK_GRAY);

    public static ConfigSchema load(File configFile) {

        ConfigSchema config = new ConfigSchema();

        // Attempt to load existing config file
        if (configFile.exists()) {

            try (FileReader reader = new FileReader(configFile)) {

                config = GSON.fromJson(reader, ConfigSchema.class);
                Constants.LOG.info("Loaded config file.");
            }

            catch (Exception e) {

                Constants.LOG.error("Could not read config file {}. Defaults will be used.", configFile.getAbsolutePath());
                Constants.LOG.catching(e);
            }
        }

        else {

            Constants.LOG.info("Creating a new config file at {}.", configFile.getAbsolutePath());
            configFile.getParentFile().mkdirs();
        }

        try (FileWriter writer = new FileWriter(configFile)) {

            GSON.toJson(config, writer);
            Constants.LOG.info("Saved config file.");
        }

        catch (Exception e) {

            Constants.LOG.error("Could not write config file '{}'!", configFile.getAbsolutePath());
            Constants.LOG.catching(e);
        }

        return config;
    }

    private static class Serializer implements JsonSerializer<Style>, JsonDeserializer<Style> {

        @Override
        public Style deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Style.FORMATTING_CODEC.decode(JsonOps.INSTANCE, json).getOrThrow(false, error -> Constants.LOG.error("Error parsing style config: {}", error)).getFirst();
        }

        @Override
        public JsonElement serialize(Style src, Type typeOfSrc, JsonSerializationContext context) {
            return Style.FORMATTING_CODEC.encodeStart(JsonOps.INSTANCE, src).getOrThrow(false, error -> Constants.LOG.error("Error writing style config: {}", error));
        }
    }
}