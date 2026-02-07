package com.kara.badgersquad.mixin;

import com.github.alexthe666.iceandfire.util.WorldUtil;
import com.github.alexthe666.iceandfire.world.IafWorldData;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldUtil.class)
public class IceAndFireWorldUtilMixin {
    @Inject(
        method = "canGenerate(ILnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;Ljava/lang/String;Lcom/github/alexthe666/iceandfire/world/IafWorldData$FeatureType;Z)Z",
        at = @At("HEAD"),
        cancellable = true,
        remap = false
    )
    private static void canGenerateStart(int configChance, final WorldGenLevel level, final RandomSource random,
                                         final BlockPos origin, final String id, final IafWorldData.FeatureType type,
                                         boolean checkFluid, CallbackInfoReturnable<Boolean> cir) {


        if (checkFluid && !level.getFluidState(origin).isEmpty()) {
            cir.setReturnValue(false);
            cir.cancel();
            return;
        }

        if (random.nextInt(configChance) != 0 || !IafWorldRegistry.isFarEnoughFromSpawn(level, origin) || !IafWorldRegistry.isFarEnoughFromDangerousGen(level, origin, id, type)) {
            cir.setReturnValue(false);
            cir.cancel();
            return;
        }

        boolean is_empty = level.getLevel().structureManager().getAllStructuresAt(origin).isEmpty();

        cir.setReturnValue(is_empty);
        cir.cancel();
    }
}
