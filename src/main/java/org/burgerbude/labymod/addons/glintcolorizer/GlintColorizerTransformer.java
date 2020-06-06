package org.burgerbude.labymod.addons.glintcolorizer;

import net.labymod.core.asm.LabyModCoreMod;
import net.labymod.main.Source;
import net.minecraft.launchwrapper.IClassTransformer;
import org.burgerbude.labymod.addons.glintcolorizer.patcher.Patcher;
import org.burgerbude.labymod.addons.glintcolorizer.patcher.patch.LayerArmorBasePatcher;
import org.burgerbude.labymod.addons.glintcolorizer.patcher.patch.RenderItemPatcher;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.util.HashMap;
import java.util.Map;

/**
 * Transformer of the <b>GlintColorizer</b> addon which transforms the <b>LayerArmorBase</b>
 * and <b>RenderItem</b> classes.
 *
 * @author Robby
 */
public class GlintColorizerTransformer implements IClassTransformer {

    public static final boolean OLD_MC = Source.ABOUT_MC_VERSION.startsWith("1.8");

    private final String layerArmorBaseClass = (LabyModCoreMod.isForge() ?
            (OLD_MC ? "net.minecraft.client.renderer.entity.layers.LayerArmorBase" :
                    "net.minecraft.client.renderer.entity.layers.LayerArmorBase") :
            (OLD_MC ? "bkn" : "cbp"));
    private final String renderItemClass = (LabyModCoreMod.isForge() ?
            (OLD_MC ? "net.minecraft.client.renderer.entity.RenderItem" :
                    "net.minecraft.client.renderer.RenderItem") :
            (OLD_MC ? "bjh" : "bzw"));

    private final Map<String, Patcher> patchers;

    /**
     * Default constructor
     */
    public GlintColorizerTransformer() {
        this.patchers = new HashMap<>();

        this.patchers.put(this.layerArmorBaseClass, new LayerArmorBasePatcher());
        this.patchers.put(this.renderItemClass, new RenderItemPatcher());
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        //Checks if the name contains with the patcher classes
        if (this.patchers.containsKey(name)) {
            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(basicClass);
            classReader.accept(classNode, 0);
            this.patchers.get(name).patch(classNode);

            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            classNode.accept(classWriter);

            return classWriter.toByteArray();
        }

        return basicClass;
    }


}
