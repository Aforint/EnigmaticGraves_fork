package dev.quarris.enigmaticgraves.compat;

import com.google.common.collect.Multimap;
import dev.quarris.enigmaticgraves.grave.data.CurioGraveData;
import dev.quarris.enigmaticgraves.grave.data.IGraveData;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.common.capability.CurioInventoryCapability;

import java.util.*;
import java.util.function.Predicate;

public class CurioCompat {

    public static final Map<UUID, ListTag> CACHED_CURIOS = new HashMap<>();

    public static void cacheCurios(Player player) {
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            ListTag cache = handler.saveInventory(false);
            CACHED_CURIOS.put(player.getUUID(), cache);
        });
    }

    public static IGraveData generateCurioGraveData(Player player, Collection<ItemEntity> drops) {
        if (CACHED_CURIOS.containsKey(player.getUUID())) {
            IGraveData data = new CurioGraveData(CACHED_CURIOS.get(player.getUUID()), drops, player);
            CACHED_CURIOS.remove(player.getUUID());
            return data;
        }
        return null;
    }
}
