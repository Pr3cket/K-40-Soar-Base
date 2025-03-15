package com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.model;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.WorldRenderer;

public class ModelBoxBends extends ModelBox {
    public final float originalResX;
    public final float originalResY;
    public final float originalResZ;
    public final float txOffsetX;
    public final float txOffsetY;
    public final PositionTextureVertex[] vertices;
    public final TexturedQuad[] quads;
    public float offsetX;
    public float offsetY;
    public float offsetZ;
    public float resX;
    public float resY;
    public float resZ;
    public float addSize = 0.0F;

    public ModelBoxBends(ModelRenderer p_i1171_1_, int p_i1171_2_, int p_i1171_3_, float p_i1171_4_, float p_i1171_5_
            , float p_i1171_6_, int p_i1171_7_, int p_i1171_8_, int p_i1171_9_, float p_i1171_10_) {
        super(p_i1171_1_, p_i1171_9_, p_i1171_9_, p_i1171_10_, p_i1171_10_, p_i1171_10_, p_i1171_9_, p_i1171_9_,
                p_i1171_9_, p_i1171_10_);
        this.offsetX = p_i1171_4_;
        this.offsetY = p_i1171_5_;
        this.offsetZ = p_i1171_6_;
        this.resX = this.originalResX = (float) p_i1171_7_;
        this.resY = this.originalResY = (float) p_i1171_8_;
        this.resZ = this.originalResZ = (float) p_i1171_9_;
        this.txOffsetX = (float) p_i1171_2_;
        this.txOffsetY = (float) p_i1171_3_;
        this.vertices = new PositionTextureVertex[8];
        this.quads = new TexturedQuad[6];
        float f4 = p_i1171_4_ + (float) p_i1171_7_;
        float f5 = p_i1171_5_ + (float) p_i1171_8_;
        float f6 = p_i1171_6_ + (float) p_i1171_9_;
        p_i1171_4_ -= p_i1171_10_;
        p_i1171_5_ -= p_i1171_10_;
        p_i1171_6_ -= p_i1171_10_;
        f4 += p_i1171_10_;
        f5 += p_i1171_10_;
        f6 += p_i1171_10_;
        this.addSize = p_i1171_10_;
        if (p_i1171_1_.mirror) {
            float f7 = f4;
            f4 = p_i1171_4_;
            p_i1171_4_ = f7;
        }

        PositionTextureVertex positiontexturevertex7 = new PositionTextureVertex(p_i1171_4_, p_i1171_5_, p_i1171_6_,
                0.0F, 0.0F);
        PositionTextureVertex positiontexturevertex = new PositionTextureVertex(f4, p_i1171_5_, p_i1171_6_, 0.0F, 8.0F);
        PositionTextureVertex positiontexturevertex1 = new PositionTextureVertex(f4, f5, p_i1171_6_, 8.0F, 8.0F);
        PositionTextureVertex positiontexturevertex2 = new PositionTextureVertex(p_i1171_4_, f5, p_i1171_6_, 8.0F,
                0.0F);
        PositionTextureVertex positiontexturevertex3 = new PositionTextureVertex(p_i1171_4_, p_i1171_5_, f6, 0.0F,
                0.0F);
        PositionTextureVertex positiontexturevertex4 = new PositionTextureVertex(f4, p_i1171_5_, f6, 0.0F, 8.0F);
        PositionTextureVertex positiontexturevertex5 = new PositionTextureVertex(f4, f5, f6, 8.0F, 8.0F);
        PositionTextureVertex positiontexturevertex6 = new PositionTextureVertex(p_i1171_4_, f5, f6, 8.0F, 0.0F);
        this.vertices[0] = positiontexturevertex7;
        this.vertices[1] = positiontexturevertex;
        this.vertices[2] = positiontexturevertex1;
        this.vertices[3] = positiontexturevertex2;
        this.vertices[4] = positiontexturevertex3;
        this.vertices[5] = positiontexturevertex4;
        this.vertices[6] = positiontexturevertex5;
        this.vertices[7] = positiontexturevertex6;
        this.quads[0] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex4, positiontexturevertex,
                positiontexturevertex1, positiontexturevertex5 }, p_i1171_2_ + p_i1171_9_ + p_i1171_7_,
                p_i1171_3_ + p_i1171_9_, p_i1171_2_ + p_i1171_9_ + p_i1171_7_ + p_i1171_9_,
                p_i1171_3_ + p_i1171_9_ + p_i1171_8_, p_i1171_1_.textureWidth, p_i1171_1_.textureHeight);
        this.quads[1] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex7, positiontexturevertex3
                , positiontexturevertex6, positiontexturevertex2 }, p_i1171_2_, p_i1171_3_ + p_i1171_9_,
                p_i1171_2_ + p_i1171_9_, p_i1171_3_ + p_i1171_9_ + p_i1171_8_, p_i1171_1_.textureWidth,
                p_i1171_1_.textureHeight);
        this.quads[2] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex4, positiontexturevertex3
                , positiontexturevertex7, positiontexturevertex }, p_i1171_2_ + p_i1171_9_, p_i1171_3_,
                p_i1171_2_ + p_i1171_9_ + p_i1171_7_, p_i1171_3_ + p_i1171_9_, p_i1171_1_.textureWidth,
                p_i1171_1_.textureHeight);
        this.quads[3] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex1, positiontexturevertex2
                , positiontexturevertex6, positiontexturevertex5 }, p_i1171_2_ + p_i1171_9_ + p_i1171_7_,
                p_i1171_3_ + p_i1171_9_, p_i1171_2_ + p_i1171_9_ + p_i1171_7_ + p_i1171_7_, p_i1171_3_,
                p_i1171_1_.textureWidth, p_i1171_1_.textureHeight);
        this.quads[4] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex, positiontexturevertex7,
                positiontexturevertex2, positiontexturevertex1 }, p_i1171_2_ + p_i1171_9_, p_i1171_3_ + p_i1171_9_,
                p_i1171_2_ + p_i1171_9_ + p_i1171_7_, p_i1171_3_ + p_i1171_9_ + p_i1171_8_, p_i1171_1_.textureWidth,
                p_i1171_1_.textureHeight);
        this.quads[5] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex3, positiontexturevertex4
                , positiontexturevertex5, positiontexturevertex6 }, p_i1171_2_ + p_i1171_9_ + p_i1171_7_ + p_i1171_9_
                , p_i1171_3_ + p_i1171_9_, p_i1171_2_ + p_i1171_9_ + p_i1171_7_ + p_i1171_9_ + p_i1171_7_,
                p_i1171_3_ + p_i1171_9_ + p_i1171_8_, p_i1171_1_.textureWidth, p_i1171_1_.textureHeight);
        if (p_i1171_1_.mirror) {
            for (TexturedQuad quad : this.quads) {
                quad.flipFace();
            }
        }

    }

    public void updateVertexPositions(ModelRenderer p_i1171_1_) {
        float f4 = this.offsetX + this.resX;
        float f5 = this.offsetY + this.resY;
        float f6 = this.offsetZ + this.resZ;
        int p_i1171_2_ = (int) this.txOffsetX;
        int p_i1171_3_ = (int) this.txOffsetY;
        int p_i1171_7_ = (int) this.originalResX;
        int p_i1171_8_ = (int) this.originalResY;
        int p_i1171_9_ = (int) this.originalResZ;
        float p_x = this.offsetX;
        float p_y = this.offsetY;
        float p_z = this.offsetZ;
        p_x -= this.addSize;
        p_y -= this.addSize;
        p_z -= this.addSize;
        f4 += this.addSize;
        f5 += this.addSize;
        f6 += this.addSize;
        if (p_i1171_1_.mirror) {
            float f7 = f4;
            f4 = p_x;
            p_x = f7;
        }

        PositionTextureVertex positiontexturevertex7 = new PositionTextureVertex(p_x, p_y, p_z, 0.0F, 0.0F);
        PositionTextureVertex positiontexturevertex = new PositionTextureVertex(f4, p_y, p_z, 0.0F, 8.0F);
        PositionTextureVertex positiontexturevertex1 = new PositionTextureVertex(f4, f5, p_z, 8.0F, 8.0F);
        PositionTextureVertex positiontexturevertex2 = new PositionTextureVertex(p_x, f5, p_z, 8.0F, 0.0F);
        PositionTextureVertex positiontexturevertex3 = new PositionTextureVertex(p_x, p_y, f6, 0.0F, 0.0F);
        PositionTextureVertex positiontexturevertex4 = new PositionTextureVertex(f4, p_y, f6, 0.0F, 8.0F);
        PositionTextureVertex positiontexturevertex5 = new PositionTextureVertex(f4, f5, f6, 8.0F, 8.0F);
        PositionTextureVertex positiontexturevertex6 = new PositionTextureVertex(p_x, f5, f6, 8.0F, 0.0F);
        this.vertices[0] = positiontexturevertex7;
        this.vertices[1] = positiontexturevertex;
        this.vertices[2] = positiontexturevertex1;
        this.vertices[3] = positiontexturevertex2;
        this.vertices[4] = positiontexturevertex3;
        this.vertices[5] = positiontexturevertex4;
        this.vertices[6] = positiontexturevertex5;
        this.vertices[7] = positiontexturevertex6;
        this.quads[0] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex4, positiontexturevertex,
                positiontexturevertex1, positiontexturevertex5 }, p_i1171_2_ + p_i1171_9_ + p_i1171_7_,
                p_i1171_3_ + p_i1171_9_, p_i1171_2_ + p_i1171_9_ + p_i1171_7_ + p_i1171_9_,
                p_i1171_3_ + p_i1171_9_ + p_i1171_8_, p_i1171_1_.textureWidth, p_i1171_1_.textureHeight);
        this.quads[1] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex7, positiontexturevertex3
                , positiontexturevertex6, positiontexturevertex2 }, p_i1171_2_, p_i1171_3_ + p_i1171_9_,
                p_i1171_2_ + p_i1171_9_, p_i1171_3_ + p_i1171_9_ + p_i1171_8_, p_i1171_1_.textureWidth,
                p_i1171_1_.textureHeight);
        this.quads[2] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex4, positiontexturevertex3
                , positiontexturevertex7, positiontexturevertex }, p_i1171_2_ + p_i1171_9_, p_i1171_3_,
                p_i1171_2_ + p_i1171_9_ + p_i1171_7_, p_i1171_3_ + p_i1171_9_, p_i1171_1_.textureWidth,
                p_i1171_1_.textureHeight);
        this.quads[3] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex1, positiontexturevertex2
                , positiontexturevertex6, positiontexturevertex5 }, p_i1171_2_ + p_i1171_9_ + p_i1171_7_,
                p_i1171_3_ + p_i1171_9_, p_i1171_2_ + p_i1171_9_ + p_i1171_7_ + p_i1171_7_, p_i1171_3_,
                p_i1171_1_.textureWidth, p_i1171_1_.textureHeight);
        this.quads[4] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex, positiontexturevertex7,
                positiontexturevertex2, positiontexturevertex1 }, p_i1171_2_ + p_i1171_9_, p_i1171_3_ + p_i1171_9_,
                p_i1171_2_ + p_i1171_9_ + p_i1171_7_, p_i1171_3_ + p_i1171_9_ + p_i1171_8_, p_i1171_1_.textureWidth,
                p_i1171_1_.textureHeight);
        this.quads[5] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex3, positiontexturevertex4
                , positiontexturevertex5, positiontexturevertex6 }, p_i1171_2_ + p_i1171_9_ + p_i1171_7_ + p_i1171_9_
                , p_i1171_3_ + p_i1171_9_, p_i1171_2_ + p_i1171_9_ + p_i1171_7_ + p_i1171_9_ + p_i1171_7_,
                p_i1171_3_ + p_i1171_9_ + p_i1171_8_, p_i1171_1_.textureWidth, p_i1171_1_.textureHeight);
        if (p_i1171_1_.mirror) {
            for (TexturedQuad quad : this.quads) {
                quad.flipFace();
            }
        }

    }

    @Override
    public void render(WorldRenderer p_178780_1_, float p_78245_2_) {
        for (TexturedQuad quad : this.quads) {
            quad.draw(p_178780_1_, p_78245_2_);
        }
    }

    public void offsetTextureQuad(ModelRenderer argModel, int argID, float argX, float argY) {
        // 禁止逼逼
        //        System.out.println("Offseting the Texture");
        if (argID >= 0 & argID < this.quads.length) {
            PositionTextureVertex var10000 = this.quads[argID].vertexPositions[0];
            var10000.texturePositionX += argX / argModel.textureWidth;
            var10000 = this.quads[argID].vertexPositions[1];
            var10000.texturePositionX += argX / argModel.textureWidth;
            var10000 = this.quads[argID].vertexPositions[2];
            var10000.texturePositionX += argX / argModel.textureWidth;
            var10000 = this.quads[argID].vertexPositions[3];
            var10000.texturePositionX += argX / argModel.textureWidth;
            var10000 = this.quads[argID].vertexPositions[0];
            var10000.texturePositionY += argY / argModel.textureHeight;
            var10000 = this.quads[argID].vertexPositions[1];
            var10000.texturePositionY += argY / argModel.textureHeight;
            var10000 = this.quads[argID].vertexPositions[2];
            var10000.texturePositionY += argY / argModel.textureHeight;
            var10000 = this.quads[argID].vertexPositions[3];
            var10000.texturePositionY += argY / argModel.textureHeight;
        }

    }

    public void resize(float argX, float argY, float argZ) {
        this.resX = argX;
        this.resY = argY;
        this.resZ = argZ;
    }

    public void offset(float argX, float argY, float argZ) {
        this.offsetX = argX;
        this.offsetY = argY;
        this.offsetZ = argZ;
    }

    public void offset_add(float argX, float argY, float argZ) {
        this.offsetX += argX;
        this.offsetY += argY;
        this.offsetZ += argZ;
    }
}
