package com.kara.badgersquad;

import com.kara.badgersquad.client.CustomVampirismScreenEventHandler;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(BadgerSquad.MODID)
public class BadgerSquad {
    public static final String MODID = "badgersquad";

    public BadgerSquad(FMLJavaModLoadingContext ctx) {
        ctx.getModEventBus().addListener(this::onClientSetup);
        ctx.getModEventBus().addListener(this::onCommonSetup);
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        if (ModList.get().isLoaded("vampirism")) {
            MinecraftForge.EVENT_BUS.register(new CustomVampirismScreenEventHandler());
        }
    }

    private void onCommonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModList modList = ModList.get();
            
            if (modList.isLoaded("alexsmobs")) {
                var banana = ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath(
                    "pamhc2trees",
                    "bananaitem")
                );
                ComposterBlock.COMPOSTABLES.removeFloat(banana);
            }
            if (modList.isLoaded("vampirism")) {
                var garlic = ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath(
                    "pamhc2crops",
                    "garlicitem")
                );
                ComposterBlock.COMPOSTABLES.removeFloat(garlic);
            }
        });
    }
}