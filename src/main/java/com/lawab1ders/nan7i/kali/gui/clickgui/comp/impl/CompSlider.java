package com.lawab1ders.nan7i.kali.gui.clickgui.comp.impl;

import com.lawab1ders.nan7i.kali.color.AccentColor;
import com.lawab1ders.nan7i.kali.color.palette.ColorType;
import com.lawab1ders.nan7i.kali.gui.clickgui.comp.Comp;
import com.lawab1ders.nan7i.kali.module.setting.ModuleSetting;
import com.lawab1ders.nan7i.kali.module.setting.impl.IntSetting;
import com.lawab1ders.nan7i.kali.nanovg.Fonts;
import com.lawab1ders.nan7i.kali.utils.MathUtils;
import com.lawab1ders.nan7i.kali.utils.animation.Animation;
import com.lawab1ders.nan7i.kali.utils.mouse.MouseUtils;
import lombok.Getter;
import lombok.Setter;

public class CompSlider<T> extends Comp {

    private final ModuleSetting<T> setting;
    private final Animation animation = new Animation();
    private final Animation draggingAnimation = new Animation();

    @Getter
    @Setter
    private int width, height;
    private boolean dragging;

    @Setter
    private boolean circle;

    @Setter
    private boolean showValue;

    public CompSlider(ModuleSetting<T> setting) {
        super(0, 0);
        this.setting = setting;
        this.width = 90;
        this.height = 4;
        this.circle = true;
        this.showValue = true;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        AccentColor accentColor = col.getCurrentColor();

        T maxValue = setting.getMax();
        T minValue = setting.getMin();
        T value = setting.getValue();

        float min = Float.parseFloat(minValue.toString());
        float range = Float.parseFloat(maxValue.toString()) - min;

        double valueWidth = width * (Float.parseFloat(value.toString()) - min) / range;
        double diff = Math.min(width, Math.max(0, mouseX - (this.getX() - 1.5F)));

        if (dragging) {
            if (diff == 0) {
                setting.setValue(minValue);
            }
            else {
                double newValueRate = MathUtils.roundToPlace(((diff / width) * range + min), 2);

                // 转换为泛型
                @SuppressWarnings("ALL")
                T intValue = (T) Integer.valueOf((int) newValueRate);

                @SuppressWarnings("ALL")
                T floatValue = (T) Float.valueOf((float) newValueRate);

                setting.setValue(setting instanceof IntSetting ? intValue : floatValue);
            }
        }

        animation.setAnimation((float) valueWidth, 16);
        draggingAnimation.setAnimation(
                MouseUtils.isInside(
                        mouseX, mouseY, this.getX() - 6, this.getY() - 3,
                        width + 12, height * height
                ) ? 1.0F : 0.0F, 16
        );

        nvg.drawRoundedRect(
                this.getX(), this.getY(), (float) width, (float) height, 2F,
                palette.getBackgroundColor(ColorType.NORMAL)
        );
        nvg.drawGradientRoundedRect(
                this.getX(), this.getY(), animation.getValue(), (float) height, 2F,
                accentColor.getColor1(), accentColor.getColor2()
        );

        if (circle) {
            nvg.drawGradientRoundedRect(
                    this.getX() + animation.getValue() - 6, this.getY() - 2, 8, 8, 4,
                    accentColor.getColor1(), accentColor.getColor2()
            );
        }

        if (showValue) {
            nvg.save();
            nvg.translate(0, 2 - (draggingAnimation.getValue() * 2));

            nvg.drawText(
                    String.valueOf(value),
                    this.getX() + animation.getValue() - (nvg.getTextWidth(
                            String.valueOf(value), 7, Fonts.REGULAR) / 2),
                    this.getY() - 10, palette.getFontColor(
                            ColorType.NORMAL,
                            (int) (draggingAnimation.getValue() * 255)
                    ), 7, Fonts.REGULAR
            );

            nvg.restore();
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {
        if (MouseUtils.isInside(mouseX, mouseY, this.getX() - 6, this.getY() - 3, width + 12, height * height)) {
            dragging = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        dragging = false;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
    }
}
