package traben.entity_texture_features.mixin.entity.renderer.feature;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.ParrotEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.ShoulderParrotFeatureRenderer;
import net.minecraft.client.render.entity.model.ParrotEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import traben.entity_texture_features.texture_handlers.ETFManager;
import traben.entity_texture_features.texture_handlers.ETFTexture;
import traben.entity_texture_features.utils.ETFEntityWrapper;
import traben.entity_texture_features.utils.ETFUtils2;

import java.util.Optional;

import static traben.entity_texture_features.ETFClientCommon.ETFConfigData;

@Mixin(ShoulderParrotFeatureRenderer.class)
public abstract class MixinShoulderParrotFeatureRenderer<T extends PlayerEntity> extends FeatureRenderer<T, PlayerEntityModel<T>> {

    @Shadow
    @Final
    private ParrotEntityModel model;
    private NbtCompound parrotNBT = null;
    private PlayerEntity player = null;
    private ETFTexture thisETFTexture = null;

    @SuppressWarnings("unused")
    public MixinShoulderParrotFeatureRenderer(FeatureRendererContext<T, PlayerEntityModel<T>> context) {
        super(context);
    }


    @Inject(method = "method_17958(Lnet/minecraft/client/util/math/MatrixStack;ZLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/nbt/NbtCompound;Lnet/minecraft/client/render/VertexConsumerProvider;IFFFFLnet/minecraft/entity/EntityType;)V",
            at = @At(value = "HEAD"))
    private <M extends Entity> void etf$getNBT(MatrixStack matrixStack, boolean bl, PlayerEntity playerEntity, NbtCompound nbtCompound, VertexConsumerProvider vertexConsumerProvider, int i, float f, float g, float h, float j, EntityType<M> type, CallbackInfo ci) {
        parrotNBT = nbtCompound;
        player = playerEntity;

    }

    @ModifyArg(method = "method_17958(Lnet/minecraft/client/util/math/MatrixStack;ZLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/nbt/NbtCompound;Lnet/minecraft/client/render/VertexConsumerProvider;IFFFFLnet/minecraft/entity/EntityType;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumerProvider;getBuffer(Lnet/minecraft/client/render/RenderLayer;)Lnet/minecraft/client/render/VertexConsumer;"))
    private RenderLayer etf$alterTexture(RenderLayer layer) {
        if (parrotNBT != null) {
            return RenderLayer.getEntityCutoutNoCull(etf$returnAlteredIdentifier());
        }
        //vanilla
        return layer;
    }

    private Identifier etf$returnAlteredIdentifier() {
        EntityType.getEntityFromNbt(parrotNBT, player.world);
        Optional<Entity> optionalEntity = EntityType.getEntityFromNbt(parrotNBT, player.world);
        if (optionalEntity.isPresent() && optionalEntity.get() instanceof ParrotEntity) {
            @SuppressWarnings("PatternVariableCanBeUsed") ParrotEntity parrot = (ParrotEntity) optionalEntity.get(); //  new ParrotEntity(EntityType.PARROT, player.world);
            //parrot.readCustomDataFromNbt(parrotNBT);
            // uuid not manually read from above code
            //UUID id = parrotNBT.getUuid("UUID");
            //System.out.println(id);
            //parrot.setUuid(id);

            thisETFTexture = ETFManager.getInstance().getETFTexture(ParrotEntityRenderer.getTexture(parrot.getVariant()), new ETFEntityWrapper(parrot), ETFManager.TextureSource.ENTITY, ETFConfigData.removePixelsUnderEmissiveMobs);
            return thisETFTexture.getTextureIdentifier(new ETFEntityWrapper(parrot), true);
        } else {
            ETFUtils2.logError("shoulder parrot error");
            return ParrotEntityRenderer.getTexture(ParrotEntity.Variant.byIndex(parrotNBT.getInt("Variant")));
        }
    }

    @Inject(method = "method_17958(Lnet/minecraft/client/util/math/MatrixStack;ZLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/nbt/NbtCompound;Lnet/minecraft/client/render/VertexConsumerProvider;IFFFFLnet/minecraft/entity/EntityType;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/ParrotEntityModel;poseOnShoulder(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFFI)V",
                    shift = At.Shift.AFTER))
    private <M extends Entity> void etf$applyEmissive(MatrixStack matrixStack, boolean bl, PlayerEntity playerEntity, NbtCompound nbtCompound, VertexConsumerProvider vertexConsumerProvider, int i, float f, float g, float h, float j, EntityType<M> type, CallbackInfo ci) {
        if (thisETFTexture != null) thisETFTexture.renderEmissive(matrixStack, vertexConsumerProvider, model);
    }

}


