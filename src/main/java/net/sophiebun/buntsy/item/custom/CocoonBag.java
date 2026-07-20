package net.sophiebun.buntsy.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sophiebun.buntsy.components.ModDataComponents;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.screen.CocoonBagMenu;

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
        return item.get(ModDataComponents.URO_ID);
    }

    public static boolean hasUroId(ItemStack item){
        return item.has(ModDataComponents.URO_ID);
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
            if (!mainHand.has(ModDataComponents.URO_ID) && offHand.is(ModItems.URO.get())){

                mainHand.set(ModDataComponents.URO_ID, offHand.get(ModDataComponents.URO_ID));
                offHand.shrink(1);
            }
            else if (pPlayer.isCrouching() && interactionItem.has(ModDataComponents.URO_ID)){
                ItemStack uroItem = new ItemStack(ModItems.URO.get(), 1);
                uroItem.set(ModDataComponents.URO_ID, interactionItem.get(ModDataComponents.URO_ID));
                pPlayer.addItem(uroItem);

                interactionItem.remove(ModDataComponents.URO_ID);
            }
            else if (interactionItem.has(ModDataComponents.URO_ID)) {
                pPlayer.openMenu(new SimpleMenuProvider(CocoonBagMenu::new, Component.translatable("item.buntsy.cocoon_bag")));
            }
        }

        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
