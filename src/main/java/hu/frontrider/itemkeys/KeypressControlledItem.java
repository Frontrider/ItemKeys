package hu.frontrider.itemkeys;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * if an item implements this interface, than it will be called when the cotton item key is pressed down.
 * */
public interface KeypressControlledItem {

    void onItemKeyPressed(ItemStack itemStack, ClientPlayerEntity playerEntity);
}
