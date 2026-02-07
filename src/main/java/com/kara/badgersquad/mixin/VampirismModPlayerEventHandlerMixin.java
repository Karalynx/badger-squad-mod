package com.kara.badgersquad.mixin;

import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.entity.factions.FactionPlayerHandler;
import de.teamlapen.vampirism.entity.player.ModPlayerEventHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Mixin(ModPlayerEventHandler.class)
public abstract class VampirismModPlayerEventHandlerMixin {

    @Unique
    private static final Set<String> GARLIC_FOODS = new HashSet<>(Arrays.asList(
        "vampirism:garlic_bread", "pamhc2crops:garlicseeditem", "pamhc2foodextended:spicymustardporkitem",
        "pamhc2foodextended:okracreoleitem", "pamhc2foodextended:zestyzucchiniitem", "pamhc2foodextended:bulgogiitem",
        "pamhc2foodextended:honeysoyribsitem", "pamhc2foodextended:kungpaochickenitem", "pamhc2foodextended:hoisinsauceitem",
        "pamhc2foodextended:grandmasmacaronicasseroleitem", "pamhc2foodextended:porklomeinitem", "pamhc2foodextended:fishfingersandcustarditem",
        "pamhc2foodextended:fiestacornsaladitem", "pamhc2foodextended:szechuaneggplantitem", "pamhc2foodextended:springrollitem",
        "pamhc2foodextended:pizzasoupitem", "pamhc2foodextended:garlicsteakitem", "pamhc2foodextended:kimchiitem",
        "pamhc2foodextended:chickencelerycasseroleitem", "pamhc2foodextended:garlicmashedpotatoesitem", "pamhc2foodextended:bolognaitem",
        "pamhc2foodextended:salsaitem", "pamhc2foodextended:molasseschickenitem", "pamhc2foodextended:pepperstirfryitem",
        "pamhc2foodextended:hummusitem", "pamhc2foodextended:spicygreensitem", "pamhc2foodextended:enchiladaitem",
        "pamhc2foodextended:avocadotoastitem", "pamhc2foodextended:lasagnaitem", "pamhc2foodextended:mushroomlasagnaitem",
        "pamhc2foodextended:babaganoushitem", "pamhc2foodextended:chorizoitem", "pamhc2foodextended:potstickersitem",
        "pamhc2foodextended:meatpieitem", "pamhc2foodextended:garlicchickenitem", "pamhc2foodextended:beancornmealitem",
        "pamhc2foodextended:dhalitem", "pamhc2foodextended:ovenroastedcaulifloweritem", "pamhc2foodextended:ceasarsaladitem",
        "vampirism:item_garlic", "pamhc2foodextended:charsiuitem", "pamhc2foodextended:bolognasandwichitem",
        "pamhc2foodextended:friedbolognasandwichitem", "pamhc2foodextended:chipsandsalsaitem", "pamhc2foodextended:deluxenachoesitem",
        "pamhc2foodextended:chimichangaitem", "pamhc2crops:roastedgarlicitem", "pamhc2foodextended:garlicbreaditem",
        "pamhc2crops:garlicitem"
    ));

    @Inject(method = "checkItemUsePerm", at = @At("HEAD"), cancellable = true, remap = false)
    private void checkItemUsePermStart(@NotNull ItemStack stack, @NotNull Player player, CallbackInfoReturnable<Boolean> cir) {
        if (!stack.isEmpty()) {
            if (!player.isAlive()) {
                cir.setReturnValue(false);
                cir.cancel();
                return;
            }

            LazyOptional<FactionPlayerHandler> handler = FactionPlayerHandler.getOpt(player);
            ResourceLocation item = ForgeRegistries.ITEMS.getKey(stack.getItem());
            if (null == item || !GARLIC_FOODS.contains(item.toString())) {
                return;
            }

            if (handler.map(h -> h.isInFaction(VReference.VAMPIRE_FACTION)).orElse(false)) {
                boolean message = !player.getCommandSenderWorld().isClientSide;
                if (message) {
                    player.displayClientMessage(
                        Component.translatable("text.vampirism.can_not_be_used_faction"),
                        true
                    );
                }
                cir.setReturnValue(false);
                cir.cancel();
                return;
            }

            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
