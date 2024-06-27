package dev.quarris.enigmaticgraves.content;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class GraveFinderItem extends Item {

    public GraveFinderItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext pContext, List<Component> tooltip, TooltipFlag flagIn) {
        CustomData data = stack.get(DataComponents.ENTITY_DATA);
        if (data == null) {
            tooltip.add(Component.translatable("info.grave.remove_grave"));
            return;
        }
        CompoundTag nbt = data.copyTag();
        if (nbt.contains("Pos")) {
            BlockPos bp = NbtUtils.readBlockPos(nbt,"Pos").get();
            tooltip.add(Component.literal("X: " + bp.getX() + ", Y: " + bp.getY() + ", Z: " + bp.getZ()));
        } else {
            tooltip.add(Component.translatable("info.grave.not_found"));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!player.isShiftKeyDown() || !player.isCreative()) {
            return InteractionResultHolder.pass(stack);
        }

        if (!stack.has(DataComponents.ENTITY_DATA) || !stack.get(DataComponents.ENTITY_DATA).copyTag().contains("Pos")) {
            return InteractionResultHolder.pass(stack);
        }

        player.startUsingItem(hand);
        if (level instanceof ServerLevel) {
            BlockPos pos = NbtUtils.readBlockPos(stack.get(DataComponents.ENTITY_DATA).copyTag(), "Pos").get();
            Component result = ComponentUtils.wrapInSquareBrackets(Component.translatable("chat.coordinates", pos.getX(), pos.getY(), pos.getZ()))
                .withStyle((style) -> style.withColor(ChatFormatting.GREEN)
                    .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp @s " + pos.getX() + " " + pos.getY() + " " + pos.getZ()))
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.coordinates.tooltip"))));
            player.displayClientMessage(Component.translatable("grave.locate", result), false);
            player.swing(hand, true);

            return InteractionResultHolder.success(stack);
        }

        return InteractionResultHolder.consume(stack);
    }
}
