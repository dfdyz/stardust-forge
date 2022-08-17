package stardust.stardust.client.render;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import stardust.stardust.client.render.gecko.renderer.block.ConsoleBlockRenderer;
import stardust.stardust.client.render.gecko.renderer.block.RailGun4MediumRenderer;
import stardust.stardust.client.render.gecko.renderer.block.Thruster1BlockRenderer;
import stardust.stardust.client.render.gecko.renderer.entity.CarrierDemoRenderer;
import stardust.stardust.client.render.gecko.renderer.entity.HEProjectileRenderer;
import stardust.stardust.client.render.gecko.renderer.entity.SDProjectileRenderer;
import stardust.stardust.registry.EntityTypeRegistry;
import stardust.stardust.registry.TileEntityTypeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RenderTypeRegistry {
    @SubscribeEvent
    public static void onRenderTypeSetup(final FMLClientSetupEvent event) {

        ClientRegistry.bindTileEntityRenderer(TileEntityTypeRegistry.RAIL_GUN_4_MEDIUM_TILE_ENTITY.get(), RailGun4MediumRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityTypeRegistry.THRUSTER_1_TILE_ENTITY.get(), Thruster1BlockRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityTypeRegistry.CONSOLE_TILE_ENTITY.get(), ConsoleBlockRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.HE_PROJECTILE_ENTITY.get(), HEProjectileRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.SD_PROJECTILE_ENTITY.get(), SDProjectileRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.CARRIER_DEMO_ENTITY.get(), CarrierDemoRenderer::new);
    }
}
