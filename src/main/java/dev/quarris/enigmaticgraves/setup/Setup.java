package dev.quarris.enigmaticgraves.setup;

import dev.quarris.enigmaticgraves.compat.CompatManager;
import dev.quarris.enigmaticgraves.content.GraveEntityRenderer;
import dev.quarris.enigmaticgraves.content.GraveModel;
import dev.quarris.enigmaticgraves.utils.ModRef;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

@Mod.EventBusSubscriber(modid = ModRef.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Setup {

    @SubscribeEvent
    public static void registerEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(Registry.GRAVE_ENTITY_TYPE.get(), GraveEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerGraveModelLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(GraveEntityRenderer.MODEL_RES, GraveModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {

    }

    @SubscribeEvent
    public static void interModComms(InterModEnqueueEvent event) {
        CompatManager.interModComms();
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        if (event.includeClient()) {
            event.getGenerator().addProvider(true, new ItemModelProvider(event.getGenerator().getPackOutput(), ModRef.ID, event.getExistingFileHelper()) {
                @Override
                protected void registerModels() {
                    this.singleTexture(Registry.GRAVE_FINDER_ITEM.getId().getPath(), new ResourceLocation("item/generated"), "layer0", new ResourceLocation(ModRef.ID, "grave_finder"));
                }
            });
            event.getGenerator().addProvider(true, new LanguageProvider(event.getGenerator().getPackOutput(), ModRef.ID, "en_us") {
                @Override
                protected void addTranslations() {
                    this.addItem(Registry.GRAVE_FINDER_ITEM, "Grave Finder");
                    this.add("enigmaticgraves.grave.not_found", "No bound grave");
                    this.addEntityType(Registry.GRAVE_ENTITY_TYPE, "Grave");
                    this.add("info.grave.remove_grave", "No Grave Location. Right click me (in creative) on a grave to remove it!");
                    this.add("grave.locate", "The grave is at %s");
                    this.add("info.grave.not_found", "Grave position not found!");
                    this.add("config.jade.plugin_enigmaticgraves.grave", "Enigmatic Graves Plugin");
                }
            });
        }
    }

}
