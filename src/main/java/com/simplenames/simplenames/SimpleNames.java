package com.simplenames.simplenames;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod(SimpleNames.MOD_ID)
public class SimpleNames {
    public static final String MOD_ID = "simplenames";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SimpleNames() {
        if (FMLEnvironment.dist == Dist.DEDICATED_SERVER) {
            MinecraftForge.EVENT_BUS.register(NameEvents.class);
        }
    }
}
