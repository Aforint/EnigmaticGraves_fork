package dev.quarris.enigmaticgraves.setup;

import dev.quarris.enigmaticgraves.command.RestoreGraveCommand;
import dev.quarris.enigmaticgraves.config.GraveConfigs;
import dev.quarris.enigmaticgraves.grave.GraveManager;
import dev.quarris.enigmaticgraves.grave.PlayerGraveEntry;
import dev.quarris.enigmaticgraves.utils.ModRef;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.Collectors;

@EventBusSubscriber(modid = ModRef.ID)
public class CommonEvents {

    // Once everything is collected, check to see if someone has cancelled the event, if it was cancelled then the player has not actually died, and we have to ignore any items we have collected.
    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void onPlayerDeathLast(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Player player) || event.getEntity().level().isClientSide || event.isCanceled())
            return;

        GraveManager.prepPlayerGrave(player);
    }

    @SubscribeEvent
    public static void playerDrops(LivingDropsEvent event){
        if (!(event.getEntity() instanceof Player player) || event.getEntity().level().isClientSide)
            return;

        GraveManager.populatePlayerGrave(player, event.getDrops());
        GraveManager.spawnPlayerGrave(player);
    }

    @SubscribeEvent
    public static void spawnGraveFinder(PlayerEvent.PlayerRespawnEvent event) {
        if (event.isEndConquered())
            return;

        if (!GraveConfigs.COMMON.spawnGraveFinder.get())
            return;

        ItemStack graveFinder = new ItemStack(Registry.GRAVE_FINDER_ITEM.get());
        LinkedList<PlayerGraveEntry> entries = GraveManager.getWorldGraveData(event.getEntity().level()).getGraveEntriesForPlayer(event.getEntity().getUUID());

        if (entries == null || entries.isEmpty())
            return;

        PlayerGraveEntry latestEntry = entries.getFirst();
        CompoundTag nbt = new CompoundTag();
        nbt.put("Pos", NbtUtils.writeBlockPos(latestEntry.gravePos));
        nbt.putUUID("GraveUUID", latestEntry.graveUUID);
        graveFinder.set(DataComponents.ENTITY_DATA, CustomData.of(nbt));
        event.getEntity().addItem(graveFinder);
    }

    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        RestoreGraveCommand.register(event.getDispatcher());
    }
}
