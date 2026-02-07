package com.kara.badgersquad.mixin;

import lain.mods.cos.impl.client.gui.GuiCosArmorInventory;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.client.event.ScreenEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.client.CuriosClientConfig;
import top.theillusivec4.curios.client.gui.CuriosButton;
import top.theillusivec4.curios.client.gui.CuriosScreen;
import top.theillusivec4.curios.client.gui.GuiEventHandler;

@Mixin(GuiEventHandler.class)
public class CuriosGuiEventHandlerMixin {
    @Inject(method = "onInventoryGuiInit", at = @At("TAIL"), cancellable = true, remap = false)
    private void onInventoryGuiInitEnd(ScreenEvent.Init.Post evt, CallbackInfo ci) {
        if ((Boolean)CuriosClientConfig.CLIENT.enableButton.get()) {
            if (evt.getScreen() instanceof GuiCosArmorInventory) {
                AbstractContainerScreen<?> gui = (AbstractContainerScreen) evt.getScreen();
                Tuple<Integer, Integer> offsets = CuriosScreen.getButtonOffset(false);
                int x = (Integer) offsets.getA();
                int y = (Integer) offsets.getB();
                int size = 14;
                int textureOffsetX = 50;
                int yOffset = 83;
                evt.addListener(CuriosButtonAccessor.invokeInit(
                    gui,
                    gui.getGuiLeft() + x,
                    gui.getGuiTop() + y + yOffset,
                    size,
                    size,
                    textureOffsetX,
                    0,
                    size,
                    CuriosScreenAccessor.getCurioInventory()
                ));
                ci.cancel();
            }
        }
    }
}

@Mixin(CuriosButton.class)
interface CuriosButtonAccessor {
    @Invoker("<init>")
    static CuriosButton invokeInit(AbstractContainerScreen<?> gui, int x, int y, int width, int height, int texX, int texY, int size, ResourceLocation texture) {
        throw new AssertionError();
    }
}

@Mixin(CuriosScreen.class)
interface CuriosScreenAccessor {
    @Accessor("CURIO_INVENTORY")
    static ResourceLocation getCurioInventory() {
        throw new AssertionError();
    }
}


