package io.github.cottonmc.itemkeys;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * Same as KeypressControlledItem, except it will run on the server.
 * @see KeypressControlledItem
 * */
public interface ServerKeyPressControlledItem {
    void onItemKeyPressedServer(ItemStack itemStack, PlayerEntity playerEntity);
}
