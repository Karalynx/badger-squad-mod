package com.kara.badgersquad.items;

import com.kara.badgersquad.BadgerSquad;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = BadgerSquad.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemOverwriteHandler {

    @SubscribeEvent
    public static void onRegister(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.ITEMS, helper -> {
            FoodProperties garlicFood = new FoodProperties.Builder()
                    .nutrition(1)
                    .saturationMod(0.25f)
                    .build();

            Item edibleGarlic = new Item(new Item.Properties().food(garlicFood));
            helper.register(
                ResourceLocation.fromNamespaceAndPath("vampirism", "item_garlic"),
                edibleGarlic
            );
        });
    }
}
