package hu.frontrider.itemkeys;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import org.lwjgl.glfw.GLFW;

public class Main implements ClientModInitializer, ModInitializer {
    public static String MODID = "item_keys";
    private Identifier keybindingIdentifier = new Identifier(MODID, "item_interaction");
    private Identifier serverKeyPressIdentifier = new Identifier(MODID, "item_interaction_server_keypress");


    private FabricKeyBinding itemKey = FabricKeyBinding.Builder.create(
            keybindingIdentifier,
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

                //if either the main or offhand item is keypress controlled, than we call the methods
                if (mainHandStack.getItem() instanceof KeypressControlledItem) {
                    ((KeypressControlledItem) mainHandStack.getItem()).onItemKeyPressed(mainHandStack, player);
                } else {
                    ItemStack offhandStack = inventory.offHand.get(0);

                    if (offhandStack.getItem() instanceof KeypressControlledItem) {
                        ((KeypressControlledItem) offhandStack.getItem()).onItemKeyPressed(mainHandStack, player);
                    }
                }
                //if either the main or offhand item is keypress controlled on the server, than we send the packet to tell the server about it.
                if (mainHandStack.getItem() instanceof ServerKeyPressControlledItem) {
                    ClientSidePacketRegistry.INSTANCE.sendToServer(serverKeyPressIdentifier, new PacketByteBuf(Unpooled.buffer()));
                } else {
                    ItemStack offhandStack = inventory.offHand.get(0);
                    if (offhandStack.getItem() instanceof ServerKeyPressControlledItem) {
                        ClientSidePacketRegistry.INSTANCE.sendToServer(serverKeyPressIdentifier, new PacketByteBuf(Unpooled.buffer()));
                    }
                }
            }

            if (itemKey.isPressed()) {
                ClientPlayerEntity player = minecraftClient.player;
                PlayerInventory inventory = player.inventory;
                ItemStack mainHandStack = inventory.getMainHandStack();

                //if either the main or offhand item is keyholding controlled, than we call the methods every tick.
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

    @Override
    public void onInitialize() {
        //if we recieve the keypress control packet, than we check which held item should be called.
        ServerSidePacketRegistry.INSTANCE.register(
                serverKeyPressIdentifier,
                (packetContext, packetByteBuf) -> {
                    PlayerEntity player = packetContext.getPlayer();
                    PlayerInventory inventory = player.inventory;
                    ItemStack mainHandStack = inventory.getMainHandStack();

                    if (mainHandStack.getItem() instanceof ServerKeyPressControlledItem) {
                        ((ServerKeyPressControlledItem) mainHandStack.getItem()).onItemKeyPressedServer(mainHandStack, player);
                    } else {
                        ItemStack offhandStack = inventory.offHand.get(0);

                        if (offhandStack.getItem() instanceof ServerKeyPressControlledItem) {
                            ((ServerKeyPressControlledItem) offhandStack.getItem()).onItemKeyPressedServer(mainHandStack, player);
                        }
                    }
                });
    }
}
