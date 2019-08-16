package hu.frontrider.itemkeys;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * if an item implements this interface, than it will be called every tick as long as the the cotton item key is held down.
 * */
public interface KeyholdingControlledItem {

    void onItemKeyHeld(ItemStack itemStack, ClientPlayerEntity playerEntity);
}
