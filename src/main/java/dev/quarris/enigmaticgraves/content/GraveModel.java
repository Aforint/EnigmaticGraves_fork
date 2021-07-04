package dev.quarris.enigmaticgraves.content;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3f;

public class GraveModel extends EntityModel<Entity> {
    private final ModelRenderer pot;
    private final ModelRenderer sapling_r1;
    private final ModelRenderer sapling_r2;
    private final ModelRenderer grave;

    public GraveModel() {
        this.textureWidth = 64;
        this.textureHeight = 64;

        this.pot = new ModelRenderer(this);
        this.pot.setRotationPoint(-1.0F, 23.0F, 0.0F);
        this.pot.setTextureOffset(23, 38).addBox(-3.0F, -5.0F, 4.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        this.pot.setTextureOffset(0, 4).addBox(-4.0F, -5.0F, 1.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        this.pot.setTextureOffset(0, 0).addBox(-3.0F, -2.0F, 1.0F, 3.0F, 1.0F, 3.0F, 0.0F, false);
        this.pot.setTextureOffset(16, 33).addBox(0.0F, -5.0F, 1.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        this.pot.setTextureOffset(39, 29).addBox(-3.0F, -5.0F, 0.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        this.pot.setTextureOffset(19, 42).addBox(-3.0F, -4.0F, 1.0F, 3.0F, 1.0F, 3.0F, 0.0F, false);

        this.sapling_r1 = new ModelRenderer(this);
        this.sapling_r1.setRotationPoint(-1.5F, -7.5F, 2.5F);
        this.pot.addChild(this.sapling_r1);
        this.setRotationAngle(this.sapling_r1, 0.0F, 0.7854F, 0.0F);
        this.sapling_r1.setTextureOffset(0, 33).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F, 0.0F, false);

        this.sapling_r2 = new ModelRenderer(this);
        this.sapling_r2.setRotationPoint(-1.5F, -7.5F, 2.5F);
        this.pot.addChild(this.sapling_r2);
        this.setRotationAngle(this.sapling_r2, 0.0F, -0.7854F, 0.0F);
        this.sapling_r2.setTextureOffset(0, 33).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F, 0.0F, false);

        this.grave = new ModelRenderer(this);
        this.grave.setRotationPoint(0.0F, 23.0F, 0.0F);
        this.grave.setTextureOffset(0, 0).addBox(-7.0F, 0.0F, -5.0F, 14.0F, 1.0F, 12.0F, 0.0F, false);
        this.grave.setTextureOffset(0, 13).addBox(-7.0F, -13.0F, -7.0F, 14.0F, 14.0F, 2.0F, 0.0F, false);
        this.grave.setTextureOffset(32, 13).addBox(-8.0F, -13.0F, -7.5F, 1.0F, 14.0F, 3.0F, 0.0F, false);
        this.grave.setTextureOffset(0, 29).addBox(-7.0F, -14.0F, -7.5F, 14.0F, 1.0F, 3.0F, 0.0F, false);
        this.grave.setTextureOffset(31, 30).addBox(7.0F, -13.0F, -7.5F, 1.0F, 14.0F, 3.0F, 0.0F, false);
        this.grave.setTextureOffset(0, 52).addBox(-6.0F, -1.0F, -5.0F, 12.0F, 1.0F, 11.0F, 0.0F, false);
    }

    @Override
    public void setRotationAngles(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        matrixStack.push();
        matrixStack.rotate(Vector3f.XP.rotationDegrees(180));
        matrixStack.translate(0, -1.5, 0);
        this.pot.render(matrixStack, buffer, packedLight, packedOverlay);
        this.grave.render(matrixStack, buffer, packedLight, packedOverlay);
        matrixStack.pop();
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}