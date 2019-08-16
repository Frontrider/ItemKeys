package io.github.cottonmc.itemkeys;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class Main implements ClientModInitializer {
    public static String MODID = "item_keys";

    private FabricKeyBinding itemKey = FabricKeyBinding.Builder.create(
            new Identifier(MODID, "item_interaction"),
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_X,
            "Cotton Item Keys"
    ).build();


    @Override
    public void onInitializeClient() {
        KeyBindingRegistry.INSTANCE.register(itemKey);

        ClientTickCallback.EVENT.register(minecraftClient -> {
            if (itemKey.wasPressed()) {
                ClientPlayerEntity player = minecraftClient.player;
                PlayerInventory inventory = player.inventory;
                ItemStack mainHandStack = inventory.getMainHandStack();

                if (mainHandStack.getItem() instanceof KeypressControlledItem) {
                    ((KeypressControlledItem) mainHandStack.getItem()).onItemKeyPressed(mainHandStack, player);
                } else {
                    ItemStack offhandStack = inventory.offHand.get(0);

                    if (offhandStack.getItem() instanceof KeypressControlledItem) {
                        ((KeypressControlledItem) offhandStack.getItem()).onItemKeyPressed(mainHandStack, player);
                    }
                }
            }

            if (itemKey.isPressed()) {
                ClientPlayerEntity player = minecraftClient.player;
                PlayerInventory inventory = player.inventory;
                ItemStack mainHandStack = inventory.getMainHandStack();

                if (mainHandStack.getItem() instanceof KeyholdingControlledItem) {
                    ((KeyholdingControlledItem) mainHandStack.getItem()).onItemKeyHeld(mainHandStack, player);
                } else {
                    ItemStack offhandStack = inventory.offHand.get(0);

                    if (offhandStack.getItem() instanceof KeyholdingControlledItem) {
                        ((KeyholdingControlledItem) offhandStack.getItem()).onItemKeyHeld(mainHandStack, player);
                    }
                }
            }

        });
    }
}
