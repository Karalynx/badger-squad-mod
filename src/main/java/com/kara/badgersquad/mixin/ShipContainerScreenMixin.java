package com.kara.badgersquad.mixin;

import com.talhanation.smallships.client.gui.screens.inventory.ShipContainerScreen;
import com.talhanation.smallships.world.inventory.ShipContainerMenu;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShipContainerScreen.class)
public abstract class ShipContainerScreenMixin extends AbstractContainerScreen<ShipContainerMenu> {

    @Shadow @Final @Mutable
    private static ResourceLocation RESOURCE_LOCATION;
    static {
        RESOURCE_LOCATION = ResourceLocation.fromNamespaceAndPath("badgersquad", "textures/gui/ship_inventory.png");
    }

    @Shadow @Final private int pageCount;

    @Shadow @Final private int pageIndex;


    public ShipContainerScreenMixin(ShipContainerMenu p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void onInitStart(CallbackInfo ci) {
        ShipContainerScreen self = (ShipContainerScreen) (Object) this;

        this.leftPos = 40 + (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        if (minecraft != null && minecraft.player != null) {
            Button backward = this.addRenderableWidget(new Button.Builder(Component.literal("<"),
                (b) -> this.menu.clickMenuButton(this.minecraft.player, -1))
                .pos(self.getGuiLeft() + 115, self.getGuiTop() + 4)
                .size(12, 12)
                .build());

            Button forward = this.addRenderableWidget(new Button.Builder(Component.literal(">"),
                (b) -> this.menu.clickMenuButton(this.minecraft.player, 1))
                .pos(self.getGuiLeft() + 157, self.getGuiTop() + 4)
                .size(12, 12)
                .build());

            backward.active = this.pageCount > 1 && this.pageIndex + 1 > 1;
            forward.active = this.pageCount > 1 && this.pageIndex + 1 < this.pageCount;
        }

        ci.cancel();
    }

    @ModifyArg(
        method = "renderLabels",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I",
            ordinal = 12
        ),
        index = 3
    )
    private int modifyDrawStringY(int originalY) {
        return 7;
    }
}

