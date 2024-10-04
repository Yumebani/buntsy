package net.sophiebun.buntsy.item.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import net.sophiebun.buntsy.entity.ModEntities;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.screen.CocoonBagMenu;
import net.sophiebun.buntsy.server.*;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.Nullable;

public class CocoonBag extends Item {

    public CocoonBag(Properties pProperties) {
        super(pProperties);
    }

    public static ItemStack getCocoonBagFromInv(Player player){
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();
        if (mainHand.is(ModItems.COCOON_BAG.get())){
            return mainHand;
        }
        else if (offHand.is(ModItems.COCOON_BAG.get())){
            return offHand;
        }
        else return ItemStack.EMPTY;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    public static int getUroId(ItemStack item){
        return item.getTag().getInt("buntsy.uro_id");
    }

    public static boolean hasUroId(ItemStack item){
        return item.getTag().contains("buntsy.uro_id");
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        if (!pLevel.isClientSide()){
            ItemStack mainHand = pPlayer.getItemBySlot(EquipmentSlot.MAINHAND);
            ItemStack offHand = pPlayer.getItemBySlot(EquipmentSlot.OFFHAND);
            ItemStack interactionItem = pPlayer.getItemInHand(pUsedHand);
            if ((!mainHand.hasTag() || !mainHand.getTag().contains("buntsy.uro_id")) && offHand.is(ModItems.URO.get())){

                if (mainHand.hasTag()){
                    ItemStackHandler contents = new ItemStackHandler(27);
                    contents.deserializeNBT(mainHand.getTag().getCompound("contents"));
                    SimpleContainer inventory = new SimpleContainer(27);
                    for (int i = 0; i < 27; i++){
                        inventory.setItem(i, contents.getStackInSlot(i));
                    }
                    Containers.dropContents(pLevel, pPlayer.getOnPos(), inventory);
                }

                CompoundTag tag = new CompoundTag();
                tag.putInt("buntsy.uro_id", offHand.getTag().getInt("buntsy.uro_id"));
                mainHand.setTag(tag);
                offHand.shrink(1);
            }
            else if (pPlayer.isCrouching() && interactionItem.hasTag() && interactionItem.getTag().contains("buntsy.uro_id")){
                CompoundTag uro = new CompoundTag();
                uro.putInt("buntsy.uro_id", interactionItem.getTag().getInt("buntsy.uro_id"));
                ItemStack uroItem = new ItemStack(ModItems.URO.get(), 1);
                uroItem.setTag(uro);
                pPlayer.addItem(uroItem);

                CompoundTag tag = new CompoundTag();
                interactionItem.setTag(tag);
            }
            else {
                NetworkHooks.openScreen((ServerPlayer) pPlayer, new SimpleMenuProvider(CocoonBagMenu::new, Component.translatable("item.buntsy.cocoon_bag")));
            }
        }

        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
