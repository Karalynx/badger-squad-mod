package com.kara.badgersquad.mixin;

import lain.mods.cos.impl.ModConfigs;
import lain.mods.cos.impl.ModObjects;
import lain.mods.cos.impl.client.PlayerRenderHandler;
import lain.mods.cos.impl.client.gui.GuiCosArmorButton;
import lain.mods.cos.impl.client.gui.GuiCosArmorInventory;
import lain.mods.cos.impl.client.gui.GuiCosArmorToggleButton;
import lain.mods.cos.impl.client.gui.InventoryScreenAccess;
import lain.mods.cos.impl.network.packet.PacketOpenCosArmorInventory;
import lain.mods.cos.impl.network.packet.PacketOpenNormalInventory;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ScreenEvent;
import org.spongepowered.asm.mixin.Mixin;
import lain.mods.cos.impl.client.GuiHandler;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.client.gui.CuriosScreenV2;

@Mixin(GuiHandler.class)
public class CosmeticArmourGuiHandlerMixin {

    @Inject(method = "handleGuiInitPost", at = @At("TAIL"), cancellable = true, remap = false)
    private void handleGuiInitPostTail(ScreenEvent.Init.Post event, CallbackInfo ci) {
        if (event.getScreen() instanceof CuriosScreenV2 && !(event.getScreen() instanceof CreativeModeInventoryScreen)) {
            AbstractContainerScreen<?> screen = (AbstractContainerScreen<?>) event.getScreen();

            if (!ModConfigs.CosArmorGuiButton_Hidden.get()) {
                event.addListener(new GuiCosArmorButton(
                        screen.getGuiLeft() + ModConfigs.CosArmorGuiButton_Left.get()/* 65 */,
                        screen.getGuiTop() + ModConfigs.CosArmorGuiButton_Top.get()/* 67 */,
                        10, 10,
                        event.getScreen() instanceof GuiCosArmorInventory ?
                                Component.translatable("cos.gui.buttonnormal") :
                                Component.translatable("cos.gui.buttoncos"),
                        button -> {
                            if (screen instanceof GuiCosArmorInventory) {
                                InventoryScreen newGui = new InventoryScreen(screen.getMinecraft().player);
                                InventoryScreenAccess.setXMouse(newGui, ((GuiCosArmorInventory) screen).oldMouseX);
                                InventoryScreenAccess.setYMouse(newGui, ((GuiCosArmorInventory) screen).oldMouseY);
                                screen.getMinecraft().setScreen(newGui);
                                ModObjects.network.sendToServer(new PacketOpenNormalInventory());
                            }
                            else {
                                ModObjects.network.sendToServer(new PacketOpenCosArmorInventory());
                            }
                        },
                        null));
            }
            if (!ModConfigs.CosArmorToggleButton_Hidden.get()) {
                event.addListener(new GuiCosArmorToggleButton(
                        screen.getGuiLeft() + ModConfigs.CosArmorToggleButton_Left.get()/* 59 */,
                        screen.getGuiTop() + ModConfigs.CosArmorToggleButton_Top.get()/* 72 */,
                        5, 5,
                        Component.empty(),
                        PlayerRenderHandler.Disabled ? 1 : 0,
                        button -> {
                            PlayerRenderHandler.Disabled = !PlayerRenderHandler.Disabled;
                            ((GuiCosArmorToggleButton) button).state = PlayerRenderHandler.Disabled ? 1 : 0;
                        }
                    )
                );
            }
        }
        ci.cancel();
    }
}
