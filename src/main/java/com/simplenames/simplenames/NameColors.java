package com.simplenames.simplenames;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.ChatFormatting;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Color names and per-player storage. Minecraft-free and unit-tested.
 */
public final class NameColors {
    public static final String COLORS = Arrays.stream(ChatFormatting.values())
            .filter(ChatFormatting::isColor)
            .map(f -> f.getName().toLowerCase())
            .sorted()
            .collect(Collectors.joining(", "));

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type MAP_TYPE = new TypeToken<Map<String, String>>() {
    }.getType();

    private NameColors() {
    }

    public static ChatFormatting parse(String name) {
        if (name == null) {
            return null;
        }
        ChatFormatting formatting = ChatFormatting.getByName(name.toLowerCase());
        return formatting != null && formatting.isColor() ? formatting : null;
    }

    public static String teamName(UUID id) {
        return "sn_" + id.toString().replace("-", "").substring(0, 13);
    }

    public static Map<String, String> load(Path file) throws IOException {
        if (!Files.isRegularFile(file)) {
            return new HashMap<>();
        }
        try (Reader reader = Files.newBufferedReader(file)) {
            Map<String, String> loaded = GSON.fromJson(reader, MAP_TYPE);
            return loaded != null ? new HashMap<>(loaded) : new HashMap<>();
        }
    }

    public static void save(Path file, Map<String, String> colors) throws IOException {
        if (file.getParent() != null) {
            Files.createDirectories(file.getParent());
        }
        try (Writer writer = Files.newBufferedWriter(file)) {
            GSON.toJson(colors, writer);
        }
    }
}
