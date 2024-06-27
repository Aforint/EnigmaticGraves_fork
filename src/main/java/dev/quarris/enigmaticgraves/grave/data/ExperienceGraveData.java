package dev.quarris.enigmaticgraves.grave.data;

import dev.quarris.enigmaticgraves.utils.ModRef;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class ExperienceGraveData implements IGraveData {

    public static final ResourceLocation NAME = ModRef.res("experience");

    private int xp;

    public ExperienceGraveData(int xp) {
        this.xp = xp;
    }

    public ExperienceGraveData(CompoundTag nbt) {
        HolderLookup.Provider provider = ServerLifecycleHooks.getCurrentServer().registryAccess();
        this.deserializeNBT(provider, nbt);
    }

    @Override
    public void restore(Player player) {
        player.giveExperiencePoints(this.xp);
    }

    @Override
    public CompoundTag write(HolderLookup.Provider provider, CompoundTag nbt) {
        nbt.putInt("XP", this.xp);
        return nbt;
    }

    @Override
    public void read(HolderLookup.Provider provider, CompoundTag nbt) {
        this.xp = nbt.getInt("XP");
    }

    @Override
    public ResourceLocation getName() {
        return NAME;
    }
}
