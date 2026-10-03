package com.simplenames.simplenames;

import net.minecraft.ChatFormatting;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class NameEvents {
    private static final Map<String, String> COLORS = new HashMap<>();
    private static Path file;

    private NameEvents() {
    }

    static void remember(UUID id, String color) {
        COLORS.put(id.toString(), color);
        save();
    }

    static void forget(UUID id) {
        COLORS.remove(id.toString());
        save();
    }

    private static void save() {
        if (file == null) {
            return;
        }
        try {
            NameColors.save(file, COLORS);
        } catch (IOException e) {
            SimpleNames.LOGGER.error("Failed to save SimpleNames data", e);
        }
    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        file = server.getWorldPath(LevelResource.ROOT).resolve("serverconfig").resolve("simplenames.json");
        COLORS.clear();
        try {
            COLORS.putAll(NameColors.load(file));
        } catch (IOException e) {
            SimpleNames.LOGGER.error("Failed to load SimpleNames data from {}", file, e);
        }
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        save();
        file = null;
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        NameCommands.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        String color = COLORS.get(player.getUUID().toString());
        if (color == null) {
            return;
        }
        ChatFormatting formatting = NameColors.parse(color);
        if (formatting != null) {
            NameCommands.apply(player, formatting);
        }
    }
}
