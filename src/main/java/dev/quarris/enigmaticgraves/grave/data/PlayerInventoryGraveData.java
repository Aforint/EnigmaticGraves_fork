package dev.quarris.enigmaticgraves.grave.data;

import dev.quarris.enigmaticgraves.utils.ModRef;
import dev.quarris.enigmaticgraves.utils.PlayerInventoryExtensions;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerInventoryGraveData implements IGraveData {

    public static final ResourceLocation NAME = ModRef.res("player_inventory");
    private ListTag data;
    private List<ItemStack> remainingItems = new ArrayList<>();

    public PlayerInventoryGraveData(Inventory inventory, Collection<ItemEntity> drops) {
        Inventory graveInv = new Inventory(inventory.player);
        graveInv.replaceWith(inventory);

        // Compare the inventory with the player drops
        // If an item from the inventory is not in drops,
        // that means that the item should not be put in the grave
        loop:
        for (int slot = 0; slot < graveInv.getContainerSize(); slot++) {
            ItemStack stack = graveInv.getItem(slot);

            Iterator<ItemEntity> ite = drops.iterator();
            while (ite.hasNext()) {
                ItemStack drop = ite.next().getItem();
                if (ItemStack.matches(stack, drop)) {
                    ite.remove();
                    continue loop;
                }
            }
            graveInv.removeItemNoUpdate(slot);
        }

        for (int slot = 0; slot < graveInv.armor.size(); slot++) {
            ItemStack stack = graveInv.armor.get(slot);
            if (EnchantmentHelper.has(stack, EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP)) {
                if (!PlayerInventoryExtensions.addItemToPlayerInventory(graveInv, -1, stack)) {
                    this.remainingItems.add(stack);
                }
                graveInv.armor.set(slot, ItemStack.EMPTY);
            }
        }

        this.data = graveInv.save(new ListTag());
    }

    public PlayerInventoryGraveData(CompoundTag nbt) {
        HolderLookup.Provider provider = ServerLifecycleHooks.getCurrentServer().registryAccess();
        this.deserializeNBT(provider, nbt);
    }

    public void addRemaining(Collection<ItemEntity> remaining) {

        this.remainingItems.addAll(remaining.stream().map(ItemEntity::getItem).toList());
    }

    @Override
    public void restore(Player player) {
        Inventory highPriority = new Inventory(player);
        highPriority.load(this.data);

        Inventory lowPriority = new Inventory(player);
        lowPriority.replaceWith(player.getInventory());

        player.getInventory().replaceWith(highPriority);

        for (int slot = 0; slot < lowPriority.getContainerSize(); slot++) {
            ItemStack item = lowPriority.getItem(slot);
            if (item.isEmpty())
                continue;

            PlayerInventoryExtensions.tryAddItemToPlayerInvElseDrop(player, slot, item);
        }

        for (ItemStack remainingStack : this.remainingItems) {
            if (!player.getInventory().add(remainingStack)) {
                player.spawnAtLocation(remainingStack);
            }
        }
    }

    @Override
    public CompoundTag write(HolderLookup.Provider provider, CompoundTag nbt) {
        nbt.put("Data", this.data);
        if (this.remainingItems != null) {
            nbt.putInt("RemainingSize", this.remainingItems.size());
            NonNullList<ItemStack> items = NonNullList.of(ItemStack.EMPTY, this.remainingItems.toArray(new ItemStack[this.remainingItems.size()]));
            nbt.put("Remaining", ContainerHelper.saveAllItems(new CompoundTag(), items, provider));
        }
        return nbt;
    }

    @Override
    public void read(HolderLookup.Provider provider, CompoundTag nbt) {
        this.data = nbt.getList("Data", Tag.TAG_COMPOUND);
        if (nbt.contains("Remaining")) {
            int size = nbt.getInt("RemainingSize");
            NonNullList<ItemStack> items = NonNullList.withSize(size, ItemStack.EMPTY);
            ContainerHelper.loadAllItems(nbt.getCompound("Remaining"), items, provider);
            this.remainingItems.addAll(items);
        }
    }

    @Override
    public ResourceLocation getName() {
        return NAME;
    }
}
