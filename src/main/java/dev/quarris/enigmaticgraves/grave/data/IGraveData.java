package dev.quarris.enigmaticgraves.grave.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.INBTSerializable;

public interface IGraveData extends INBTSerializable<CompoundTag> {

    void restore(Player player);
    ResourceLocation getName();

    CompoundTag write(HolderLookup.Provider provider, CompoundTag nbt);
    void read(HolderLookup.Provider provider, CompoundTag nbt);

    @Override
    default CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("Name", this.getName().toString());
        return this.write(provider, nbt);
    }

    @Override
    default void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        this.read(provider, nbt);
    }
}
