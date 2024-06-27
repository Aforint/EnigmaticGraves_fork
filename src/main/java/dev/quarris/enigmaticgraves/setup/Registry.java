package dev.quarris.enigmaticgraves.setup;

import dev.quarris.enigmaticgraves.command.GraveEntryArgument;
import dev.quarris.enigmaticgraves.content.GraveEntity;
import dev.quarris.enigmaticgraves.content.GraveFinderItem;
import dev.quarris.enigmaticgraves.utils.ModRef;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Registry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, ModRef.ID);
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ModRef.ID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, ModRef.ID);
    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPES = DeferredRegister.create(BuiltInRegistries.COMMAND_ARGUMENT_TYPE, ModRef.ID);

    public static final DeferredHolder<Item, GraveFinderItem> GRAVE_FINDER_ITEM = ITEMS.register("grave_finder", GraveFinderItem::new);
    public static final DeferredHolder<EntityType<?>, EntityType<GraveEntity>> GRAVE_ENTITY_TYPE = ENTITIES
        .register("grave", () ->
            EntityType.Builder.<GraveEntity>of(GraveEntity::new, MobCategory.MISC)
                .sized(14 / 16f, 14 / 16f)
                .fireImmune()
                .canSpawnFarFromPlayer()
                .build("grave"));

    public static final DeferredHolder<ArgumentTypeInfo<?, ?>, ArgumentTypeInfo<?, ?>> GRAVE_ENTRY_ARGUMENT = COMMAND_ARGUMENT_TYPES.register("grave_entry", () -> ArgumentTypeInfos.registerByClass(GraveEntryArgument.class, SingletonArgumentInfo.contextFree(GraveEntryArgument::new)));


    public static void init(IEventBus bus) {
        ITEMS.register(bus);
        TILE_ENTITIES.register(bus);
        ENTITIES.register(bus);
        COMMAND_ARGUMENT_TYPES.register(bus);
    }
}
