package org.burgerbude.labymod.addons.glintcolorizer;

import net.labymod.api.LabyModAddon;
import net.labymod.gui.elements.ColorPicker;
import net.labymod.main.lang.LanguageManager;
import net.labymod.settings.elements.*;
import net.labymod.utils.Material;
import net.labymod.utils.ModColor;

import java.awt.*;
import java.util.List;

/**
 * Main class of the <b>GlintColorizer</b> addon
 *
 * @author Robby
 */
public class GlintColorizerAddon extends LabyModAddon {

    public static GlintColorizerAddon instance;

    private int glintColor;
    private boolean rainbow;
    private int rainbowSpeed;

    @Override
    public void onEnable() {
        if (instance == null) instance = this;
    }

    @Override
    public void loadConfig() {
        this.glintColor = getConfig().has("glintColor") ?
                getConfig().get("glintColor").getAsInt() : ModColor.WHITE.getColor().hashCode();
        this.rainbow = getConfig().has("rainbow") && getConfig().get("rainbow").getAsBoolean();
        this.rainbowSpeed = getConfig().has("rainbowSpeed") ? getConfig().get("rainbowSpeed").getAsInt() : 1;
    }

    @Override
    protected void fillSettings(List<SettingsElement> subSettings) {
        //Creates a bulk element to add color pickers or check boxes
        ColorPickerCheckBoxBulkElement glintColorCheckBox = new ColorPickerCheckBoxBulkElement(
                this.translate("title_color_check_bulk", "Colors")
        );
        //Creates an advanced color picker to pick a custom color
        ColorPicker colorPicker = new ColorPicker(
                this.translate("title_color_picker", "Glint color"),
                new Color(glintColor),
                () -> new Color(255, 255, 255),
                0,
                0,
                0,
                0);
        colorPicker.setHasDefault(false);
        colorPicker.setHasAdvanced(true);
        colorPicker.setUpdateListener(updated -> {
            this.glintColor = updated.hashCode();
            getConfig().addProperty("glintColor", this.glintColor);
            saveConfig();
        });

        glintColorCheckBox.addColorPicker(colorPicker);
        subSettings.add(glintColorCheckBox);

        subSettings.add(new HeaderElement(this.translate("header_rainbow", "Rainbow")));
        //Creates a toggle element which enable or disable the rainbow color
        BooleanElement rainbowElement = new BooleanElement(
                this.translate("toggle_rainbow", "Enable Rainbow"),
                this,
                new ControlElement.IconData(Material.LEVER),
                "rainbow",
                this.rainbow);

        rainbowElement.addCallback(callback -> {
            this.rainbow = callback;
            getConfig().addProperty("rainbow", this.rainbow);
            saveConfig();
        });

        subSettings.add(rainbowElement);

        //Creates a slider which control the speed of the rainbow colors
        SliderElement rainbowSpeedElement = new SliderElement(
                this.translate("rainbow_speed", "Rainbow Speed"),
                this,
                new ControlElement.IconData(Material.SUGAR),
                "rainbowSpeed",
                this.rainbowSpeed);
        rainbowSpeedElement.setRange(1, 25);
        rainbowSpeedElement.addCallback(callback -> {
            this.rainbowSpeed = callback;
            getConfig().addProperty("rainbowSpeed", this.rainbowSpeed);
            saveConfig();
        });

        subSettings.add(rainbowSpeedElement);
    }

    /**
     * Translates the given key
     *
     * @param key      The key of the translation
     * @param fallback The fallback if the key doesn't translated
     * @return a translated key of the fallback
     */
    public String translate(String key, String fallback) {
        key = "glintcolorizer_" + key;
        String translate = LanguageManager.translate(key);
        return translate.equals(key) ? fallback : translate;
    }

    /**
     * Calculates a color spectrum that resembles a rainbow
     *
     * @param offset The offset of the spectrum
     * @return calculated color
     */
    private int rainbow(int offset) {
        long speed = this.rainbowSpeed * 1000L;
        float hue = (float) ((System.currentTimeMillis() + offset) % speed) / speed;
        return Color.HSBtoRGB(hue, .8F, .8F);
    }

    /**
     * Gets the glint color
     *
     * @return glint color
     */
    public int glintColor() {
        return this.rainbow ? this.rainbow(0) : this.glintColor;
    }

    /**
     * Converts a hexadecimal into a float array RGBA
     *
     * @return float array RGBA
     */
    public float[] hexadecimalToRGBA() {
        int color = glintColor();
        return new float[]{
                (float) (color >> 16 & 255) / 255.0F,
                (float) (color >> 8 & 255) / 255.0F,
                (float) (color & 255) / 255.0F,
                (float) (color >> 24 & 255) / 255.0F
        };
    }
}
