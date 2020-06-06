package org.burgerbude.labymod.addons.glintcolorizer.patcher.patch;

import net.labymod.core.asm.LabyModCoreMod;
import org.burgerbude.labymod.addons.glintcolorizer.GlintColorizerTransformer;
import org.burgerbude.labymod.addons.glintcolorizer.patcher.Patcher;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

/**
 * A patcher which patched the <b>RenderItem</b> class
 *
 * @author Robby
 */
public class RenderItemPatcher implements Patcher {

    private final String renderEffectDesc = (LabyModCoreMod.isForge() ? (GlintColorizerTransformer.OLD_MC ?
            "(Lnet/minecraft/client/resources/model/IBakedModel;)V" :
            "(Lnet/minecraft/client/renderer/block/model/IBakedModel;)V") :
            (GlintColorizerTransformer.OLD_MC ? "(Lboq;)V" : "(Lcfy;)V"));

    @Override
    public void patch(ClassNode node) {
        for (MethodNode methodNode : node.methods) {
            if (methodNode.desc.equals(this.renderEffectDesc)) {

                for (AbstractInsnNode abstractInsnNode : methodNode.instructions.toArray()) {
                    if (abstractInsnNode.getOpcode() == Opcodes.LDC) {
                        Object object = ((LdcInsnNode) abstractInsnNode).cst;

                        if (object.equals(-8372020)) {
                            InsnList insnList = new InsnList();
                            insnList.insert(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                                    "org/burgerbude/labymod/addons/glintcolorizer/GlintColorizerAddon",
                                    "glintColor",
                                    "()I",
                                    false));
                            insnList.insert(new FieldInsnNode(Opcodes.GETSTATIC,
                                    "org/burgerbude/labymod/addons/glintcolorizer/GlintColorizerAddon",
                                    "instance",
                                    "Lorg/burgerbude/labymod/addons/glintcolorizer/GlintColorizerAddon;"));

                            methodNode.instructions.insert(abstractInsnNode, insnList);
                            methodNode.instructions.remove(abstractInsnNode);
                        }

                    }

                }

            }
        }

    }
}

