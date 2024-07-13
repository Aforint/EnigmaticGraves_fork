package dev.quarris.enigmaticgraves.grave.data;

import dev.quarris.enigmaticgraves.utils.ModRef;
import dev.quarris.enigmaticgraves.utils.PlayerInventoryExtensions;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;
import top.theillusivec4.curios.common.capability.CurioInventoryCapability;

import java.util.*;

public class CurioGraveData implements IGraveData {

    public static final ResourceLocation NAME = ModRef.res("curios");
    private ListTag data;

    public CurioGraveData(ListTag curios, Collection<ItemEntity> drops, Player player) {
        this.data = curios.copy();
        ICuriosItemHandler cur = new CurioInventoryCapability(player);
        cur.loadInventory(curios);

        // Remove the curios from the drops
        for (Map.Entry<String, ICurioStacksHandler> entry : cur.getCurios().entrySet()) {
            ICurioStacksHandler curioItems = entry.getValue();
            NonNullList<ItemStack> curioStacksList = NonNullList.withSize(curioItems.getSlots(), ItemStack.EMPTY);
            NonNullList<ItemStack> curioCosmeticStacksList = NonNullList.withSize(curioItems.getSlots(), ItemStack.EMPTY);
            Iterator<ItemEntity> ite = drops.iterator();
            Set<Integer> stackSlotsChecked = new HashSet<>();
            Set<Integer> cosmeticStacksSlotsChecked = new HashSet<>();

            loop:
            while (ite.hasNext()) {
                ItemStack drop = ite.next().getItem();
                for (int slot = 0; slot < curioItems.getSlots(); slot++) {
                    if (stackSlotsChecked.contains(slot))
                        continue;

                    ItemStack stack = curioItems.getStacks().getStackInSlot(slot);;
                    if (ItemStack.matches(stack, drop)) {
                        stackSlotsChecked.add(slot);
                        curioStacksList.set(slot, drop);
                        ite.remove();
                        curioItems.getStacks().setStackInSlot(slot, ItemStack.EMPTY);
                        continue loop;
                    }
                }
                for (int slot = 0; slot < curioItems.getSlots(); slot++) {
                    if (cosmeticStacksSlotsChecked.contains(slot))
                        continue;

                    ItemStack stack = curioItems.getCosmeticStacks().getStackInSlot(slot);
                    if (ItemStack.matches(stack, drop)) {
                        cosmeticStacksSlotsChecked.add(slot);
                        curioCosmeticStacksList.set(slot, drop);
                        ite.remove();
                        curioItems.getStacks().setStackInSlot(slot, ItemStack.EMPTY);
                        continue loop;
                    }
                }
            }
        }
    }

    public CurioGraveData(CompoundTag nbt) {
        HolderLookup.Provider provider = ServerLifecycleHooks.getCurrentServer().registryAccess();
        this.deserializeNBT(provider, nbt);
    }

    @Override
    public void restore(Player player) {
        if (this.data == null) return;

        Optional<ICuriosItemHandler> optional = CuriosApi.getCuriosInventory(player);
        optional.ifPresent(handler -> {
            handler.getCurios().values().forEach(curio -> {
                IDynamicStackHandler stacks = curio.getStacks();
                for (int slot = 0; slot < stacks.getSlots(); slot++) {
                    ItemStack stack = stacks.getStackInSlot(slot);
                    if (!stack.isEmpty()) {
                        PlayerInventoryExtensions.tryAddItemToPlayerInvElseDrop(player, -1, stack);
                    }
                }
            });
            handler.loadInventory(this.data);
        });
    }

    @Override
    public ResourceLocation getName() {
        return NAME;
    }

    @Override
    public CompoundTag write(HolderLookup.Provider provider, CompoundTag nbt) {
        if (this.data != null) {
            nbt.put("Data", this.data);
        }
        return nbt;
    }

    @Override
    public void read(HolderLookup.Provider provider, CompoundTag nbt) {
        if (nbt.contains("Data")) {
            this.data = (ListTag) nbt.get("Data");
        }
    }

}
