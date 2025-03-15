package com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.model;

import com.lawab1ders.nan7i.kali.module.impl.player.mobends.util.SmoothVector3f;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import org.lwjgl.opengl.GL11;

public class ModelRendererBends extends ModelRenderer {
    public final SmoothVector3f rotation = new SmoothVector3f();
    public final SmoothVector3f pre_rotation = new SmoothVector3f();
    public float scaleX;
    public float scaleY;
    public float scaleZ;
    public int txOffsetX;
    public int txOffsetY;
    public boolean compiled;
    public boolean showChildIfHidden = false;
    private int displayList;

    public ModelRendererBends(ModelBase argModel) {
        super(argModel);
        this.scaleX = this.scaleY = this.scaleZ = 1.0F;
    }

    public ModelRendererBends(ModelBase argModel, String argName) {
        super(argModel, argName);
        this.scaleX = this.scaleY = this.scaleZ = 1.0F;
    }

    public ModelRendererBends(ModelBase argModel, int argTexOffsetX, int argTexOffsetY) {
        super(argModel, argTexOffsetX, argTexOffsetY);
        this.txOffsetX = argTexOffsetX;
        this.txOffsetY = argTexOffsetY;
        this.scaleX = this.scaleY = this.scaleZ = 1.0F;
    }

    public void updateBends(float argTicksPerFrame) {
        this.rotateAngleX = (float) ((double) (this.rotation.getX() / 180.0F) * 3.141592653589793D);
        this.rotateAngleY = (float) ((double) (this.rotation.getY() / 180.0F) * 3.141592653589793D);
        this.rotateAngleZ = (float) ((double) (this.rotation.getZ() / 180.0F) * 3.141592653589793D);
    }

    public ModelRendererBends setShowChildIfHidden(boolean arg0) {
        this.showChildIfHidden = arg0;
        return this;
    }

    @Override
    public void render(float p_78785_1_) {
        this.updateBends(p_78785_1_);
        if (!this.compiled) {
            compileDisplayList(p_78785_1_);
        }

        GL11.glTranslatef(this.offsetX, this.offsetY, this.offsetZ);
        int i;
        if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F) {
            if (this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F) {
                GL11.glRotatef(-this.pre_rotation.getY(), 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(this.pre_rotation.getX(), 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(this.pre_rotation.getZ(), 0.0F, 0.0F, 1.0F);
                GL11.glScalef(this.scaleX, this.scaleY, this.scaleZ);
                if (!this.isHidden & this.showModel) {
                    GL11.glCallList(this.displayList);
                }

                if ((this.showChildIfHidden || !this.isHidden & this.showModel) && this.childModels != null) {
                    for (i = 0; i < this.childModels.size(); ++i) {
                        this.childModels.get(i).render(p_78785_1_);
                    }
                }
            } else {
                GL11.glTranslatef(this.rotationPointX * p_78785_1_, this.rotationPointY * p_78785_1_,
                        this.rotationPointZ * p_78785_1_);
                GL11.glRotatef(-this.pre_rotation.getY(), 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(this.pre_rotation.getX(), 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(this.pre_rotation.getZ(), 0.0F, 0.0F, 1.0F);
                GL11.glScalef(this.scaleX, this.scaleY, this.scaleZ);
                if (!this.isHidden & this.showModel) {
                    GL11.glCallList(this.displayList);
                }

                if ((this.showChildIfHidden || !this.isHidden & this.showModel) && this.childModels != null) {
                    for (i = 0; i < this.childModels.size(); ++i) {
                        this.childModels.get(i).render(p_78785_1_);
                    }
                }

                GL11.glTranslatef(-this.rotationPointX * p_78785_1_, -this.rotationPointY * p_78785_1_,
                        -this.rotationPointZ * p_78785_1_);
            }
        } else {
            GL11.glPushMatrix();
            GL11.glTranslatef(this.rotationPointX * p_78785_1_, this.rotationPointY * p_78785_1_,
                    this.rotationPointZ * p_78785_1_);
            GL11.glRotatef(-this.pre_rotation.getY(), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(this.pre_rotation.getX(), 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(this.pre_rotation.getZ(), 0.0F, 0.0F, 1.0F);
            if (this.rotateAngleZ != 0.0F) {
                GL11.glRotatef(this.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
            }

            if (this.rotateAngleY != 0.0F) {
                GL11.glRotatef(this.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
            }

            if (this.rotateAngleX != 0.0F) {
                GL11.glRotatef(this.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
            }

            GL11.glScalef(this.scaleX, this.scaleY, this.scaleZ);
            if (!this.isHidden & this.showModel) {
                GL11.glCallList(this.displayList);
            }

            if ((this.showChildIfHidden || !this.isHidden & this.showModel) && this.childModels != null) {
                for (i = 0; i < this.childModels.size(); ++i) {
                    this.childModels.get(i).render(p_78785_1_);
                }
            }

            GL11.glPopMatrix();
        }

        GL11.glTranslatef(-this.offsetX, -this.offsetY, -this.offsetZ);
    }

    private void compileDisplayList(float p_compileDisplayList_1_) {
        this.displayList = GLAllocation.generateDisplayLists(1);
        GL11.glNewList(this.displayList, 4864);
        WorldRenderer worldrenderer = Tessellator.getInstance().getWorldRenderer();

        for (ModelBox modelBox : this.cubeList) {
            modelBox.render(worldrenderer, p_compileDisplayList_1_);
        }

        GL11.glEndList();
        this.compiled = true;
    }

    public void update(float p_78785_1_) {
        this.rotation.update(p_78785_1_);
        this.pre_rotation.update(p_78785_1_);
        this.updateBends(p_78785_1_);
    }

    @Override
    public void renderWithRotation(float p_78791_1_) {
        this.updateBends(p_78791_1_);
        super.renderWithRotation(p_78791_1_);
    }

    @Override
    public void postRender(float p_78794_1_) {
        this.updateBends(p_78794_1_);
        if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F) {
            if (this.rotationPointX != 0.0F || this.rotationPointY != 0.0F || this.rotationPointZ != 0.0F) {
                GL11.glTranslatef(this.rotationPointX * p_78794_1_, this.rotationPointY * p_78794_1_,
                        this.rotationPointZ * p_78794_1_);
                GL11.glRotatef(-this.pre_rotation.getY(), 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(this.pre_rotation.getX(), 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(this.pre_rotation.getZ(), 0.0F, 0.0F, 1.0F);
                GL11.glScalef(this.scaleX, this.scaleY, this.scaleZ);
            }
        } else {
            GL11.glTranslatef(this.rotationPointX * p_78794_1_, this.rotationPointY * p_78794_1_,
                    this.rotationPointZ * p_78794_1_);
            GL11.glRotatef(-this.pre_rotation.getY(), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(this.pre_rotation.getX(), 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(this.pre_rotation.getZ(), 0.0F, 0.0F, 1.0F);
            if (this.rotateAngleZ != 0.0F) {
                GL11.glRotatef(this.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
            }

            if (this.rotateAngleY != 0.0F) {
                GL11.glRotatef(this.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
            }

            if (this.rotateAngleX != 0.0F) {
                GL11.glRotatef(this.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
            }

            GL11.glScalef(this.scaleX, this.scaleY, this.scaleZ);
        }

    }

    public ModelRendererBends setPosition(float argX, float argY, float argZ) {
        this.rotationPointX = argX;
        this.rotationPointY = argY;
        this.rotationPointZ = argZ;
        return this;
    }

    public ModelRendererBends setOffset(float argX, float argY, float argZ) {
        this.offsetX = argX;
        this.offsetY = argY;
        this.offsetZ = argZ;
        return this;
    }

    public ModelRendererBends setScale(float argX, float argY, float argZ) {
        this.scaleX = argX;
        this.scaleY = argY;
        this.scaleZ = argZ;
        return this;
    }

    public ModelRendererBends resetScale() {
        this.scaleX = this.scaleY = this.scaleZ = 1.0F;
        return this;
    }

    public void sync(ModelRendererBends argBox) {
        if (argBox != null) {
            this.rotateAngleX = argBox.rotateAngleX;
            this.rotateAngleY = argBox.rotateAngleY;
            this.rotateAngleZ = argBox.rotateAngleZ;
            this.rotation.vOld.set(argBox.rotation.vOld);
            this.rotation.completion.set(argBox.rotation.completion);
            this.rotation.vFinal.set(argBox.rotation.vFinal);
            this.rotation.vSmooth.set(argBox.rotation.vSmooth);
            this.rotation.smoothness.set(argBox.rotation.smoothness);
            this.pre_rotation.vOld.set(argBox.pre_rotation.vOld);
            this.pre_rotation.completion.set(argBox.pre_rotation.completion);
            this.pre_rotation.vFinal.set(argBox.pre_rotation.vFinal);
            this.pre_rotation.vSmooth.set(argBox.pre_rotation.vSmooth);
            this.pre_rotation.smoothness.set(argBox.pre_rotation.smoothness);
            this.scaleX = argBox.scaleX;
            this.scaleY = argBox.scaleY;
            this.scaleZ = argBox.scaleZ;
        }

    }

    @Override
    public void addBox(float p_78790_1_, float p_78790_2_, float p_78790_3_, int p_78790_4_, int p_78790_5_,
                       int p_78790_6_, float p_78790_7_) {
        this.cubeList.add(new ModelBoxBends(this, this.txOffsetX, this.txOffsetY, p_78790_1_, p_78790_2_, p_78790_3_,
                p_78790_4_, p_78790_5_, p_78790_6_, p_78790_7_));
    }

    public ModelBoxBends getBox() {
        return (ModelBoxBends) this.cubeList.get(0);
    }

    public ModelRendererBends offsetBox(float argX, float argY, float argZ) {
        this.getBox().offset(argX, argY, argZ);
        return this;
    }

    public ModelRendererBends offsetBox_Add(float argX, float argY, float argZ) {
        this.getBox().offset_add(argX, argY, argZ);
        return this;
    }

    public ModelRendererBends resizeBox(float argX, float argY, float argZ) {
        this.getBox().resize(argX, argY, argZ);
        return this;
    }

    public ModelRendererBends updateVertices() {
        this.getBox().updateVertexPositions(this);
        this.compiled = false;
        return this;
    }
}
