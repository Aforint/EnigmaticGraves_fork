package dev.quarris.enigmaticgraves;

import dev.quarris.enigmaticgraves.config.GraveConfigs;
import dev.quarris.enigmaticgraves.grave.GraveManager;
import dev.quarris.enigmaticgraves.setup.Registry;
import dev.quarris.enigmaticgraves.utils.ModRef;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(ModRef.ID)
public class EnigmaticGraves {

    public EnigmaticGraves(IEventBus modEventBus, ModContainer modContainer) {
        Registry.init(modEventBus);
        GraveManager.init();
        modContainer.registerConfig(ModConfig.Type.COMMON, GraveConfigs.SPEC);
    }
}
